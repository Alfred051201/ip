#!/usr/bin/env python3
"""Run console UI tests described in a Markdown test plan."""

from __future__ import annotations

import argparse
import difflib
import re
import subprocess
import sys
from dataclasses import dataclass
from pathlib import Path


DEFAULT_TRANSCRIPT = Path("_temp/ui-test-session.md")
FENCE_RE = re.compile(r"```[^\n]*\n(.*?)\n```", re.DOTALL)


@dataclass
class TestCase:
    name: str
    aim: str
    input_text: str
    expected_output: str


@dataclass
class TestPlan:
    program_command: str
    compile_command: str | None
    test_cases: list[TestCase]


class PlanError(ValueError):
    """Raised when the Markdown test plan cannot be parsed."""


def extract_backtick_value(markdown: str, label: str, required: bool) -> str | None:
    pattern = re.compile(rf"^{re.escape(label)}:\s*`([^`]+)`\s*$", re.MULTILINE)
    match = pattern.search(markdown)
    if not match:
        if required:
            raise PlanError(f"Missing required '{label}: `...`' line")
        return None
    return match.group(1)


def extract_labeled_fence(section: str, label: str) -> str:
    label_match = re.search(rf"^{re.escape(label)}:\s*$", section, re.MULTILINE)
    if not label_match:
        raise PlanError(f"Missing '{label}:' block")
    fence_match = FENCE_RE.search(section, label_match.end())
    if not fence_match:
        raise PlanError(f"Missing fenced code block after '{label}:'")
    return fence_match.group(1)


def parse_test_cases(markdown: str) -> list[TestCase]:
    heading_re = re.compile(r"^##\s+Test Case:\s*(.+?)\s*$", re.MULTILINE)
    headings = list(heading_re.finditer(markdown))
    if not headings:
        raise PlanError("No '## Test Case: ...' sections found")

    test_cases: list[TestCase] = []
    for index, heading in enumerate(headings):
        start = heading.end()
        end = headings[index + 1].start() if index + 1 < len(headings) else len(markdown)
        section = markdown[start:end]

        aim_match = re.search(r"^Aim:\s*(.+?)\s*$", section, re.MULTILINE)
        if not aim_match:
            raise PlanError(f"Missing 'Aim:' line in test case '{heading.group(1)}'")

        test_cases.append(
            TestCase(
                name=heading.group(1),
                aim=aim_match.group(1),
                input_text=extract_labeled_fence(section, "Input"),
                expected_output=extract_labeled_fence(section, "Expected output"),
            )
        )
    return test_cases


def parse_plan(path: Path) -> TestPlan:
    markdown = path.read_text(encoding="utf-8")
    program_command = extract_backtick_value(markdown, "Program command", required=True)
    compile_command = extract_backtick_value(markdown, "Compile command", required=False)
    assert program_command is not None
    return TestPlan(program_command, compile_command, parse_test_cases(markdown))


def comparable(text: str) -> str:
    return text.replace("\r\n", "\n").rstrip("\n")


def run_shell(command: str, input_text: str | None = None, timeout: int = 10) -> subprocess.CompletedProcess[str]:
    return subprocess.run(
        command,
        input=input_text,
        capture_output=True,
        text=True,
        shell=True,
        timeout=timeout,
    )


def append_transcript(path: Path, test_case: TestCase, actual_output: str, passed: bool) -> None:
    with path.open("a", encoding="utf-8") as transcript:
        transcript.write(f"## {test_case.name}\n\n")
        transcript.write(f"Aim: {test_case.aim}\n\n")
        transcript.write("Console input:\n\n")
        transcript.write("```text\n")
        transcript.write(test_case.input_text)
        transcript.write("\n```\n\n")
        transcript.write("Console output:\n\n")
        transcript.write("```text\n")
        transcript.write(actual_output)
        transcript.write("\n```\n\n")
        transcript.write(f"Result: {'PASS' if passed else 'FAIL'}\n\n")


def print_failure(test_case: TestCase, expected: str, actual: str) -> None:
    print(f"FAIL: {test_case.name}")
    print()
    print("Expected output:")
    print("```text")
    print(expected)
    print("```")
    print()
    print("Actual output:")
    print("```text")
    print(actual)
    print("```")
    print()
    print("Unified diff:")
    expected_lines = comparable(expected).splitlines()
    actual_lines = comparable(actual).splitlines()
    for line in difflib.unified_diff(expected_lines, actual_lines, fromfile="expected", tofile="actual", lineterm=""):
        print(line)


def main() -> int:
    parser = argparse.ArgumentParser(description="Run console UI tests from a Markdown plan.")
    parser.add_argument("--plan", default="test/ui-test-plan.md", help="Path to the Markdown test plan")
    parser.add_argument("--transcript", default=str(DEFAULT_TRANSCRIPT), help="Path for the console session record")
    parser.add_argument("--timeout", type=int, default=10, help="Seconds before a command times out")
    args = parser.parse_args()

    plan_path = Path(args.plan)
    transcript_path = Path(args.transcript)
    transcript_path.parent.mkdir(parents=True, exist_ok=True)
    transcript_path.write_text("# UI Test Session\n\n", encoding="utf-8")

    try:
        plan = parse_plan(plan_path)
    except (OSError, PlanError) as error:
        print(f"Could not read test plan: {error}", file=sys.stderr)
        return 2

    if plan.compile_command:
        compile_result = run_shell(plan.compile_command, timeout=args.timeout)
        if compile_result.returncode != 0:
            print("Compile command failed", file=sys.stderr)
            print(compile_result.stdout, end="")
            print(compile_result.stderr, end="", file=sys.stderr)
            return compile_result.returncode

    for index, test_case in enumerate(plan.test_cases, start=1):
        try:
            result = run_shell(plan.program_command, input_text=test_case.input_text + "\n", timeout=args.timeout)
        except subprocess.TimeoutExpired:
            print(f"FAIL: {test_case.name}")
            print(f"Program timed out after {args.timeout} seconds")
            return 1

        actual = result.stdout
        if result.stderr:
            actual += result.stderr

        passed = result.returncode == 0 and comparable(actual) == comparable(test_case.expected_output)
        append_transcript(transcript_path, test_case, actual, passed)

        if not passed:
            if result.returncode != 0:
                print(f"Program exited with status {result.returncode}")
            print_failure(test_case, test_case.expected_output, actual)
            print(f"\nStopped after failing test {index}/{len(plan.test_cases)}.")
            print(f"Transcript: {transcript_path.resolve()}")
            return 1

    print(f"PASS: {len(plan.test_cases)} test(s)")
    print(f"Transcript: {transcript_path.resolve()}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
