    public TestResult test(Map<String, String> parameters, ExecutionResult executionResult) throws TestingException {
        String expectedOutput = parameters.get("expected");
        boolean ignoreAllSpace = Boolean.valueOf(parameters.get("ignoreAllSpace"));
        boolean ignoreSpaceChange = Boolean.valueOf(parameters.get("ignoreSpaceChange"));
        boolean ignoreBlankLines = Boolean.valueOf(parameters.get("ignoreBlankLines"));
        boolean compareErrors = Boolean.valueOf(parameters.get("compareErrors"));
        boolean compareExitCode = Boolean.valueOf(parameters.get("compareExitCode"));
        LinkedList<String> outputStrings = null;
        LinkedList<String> errorStrings = null;
        LinkedList<String> expectedStrings = null;
        LinkedList<String> obtainedStrings = new LinkedList<String>();
        try {
            expectedStrings = readLines(new StringReader(expectedOutput), MAX_LINES, ignoreAllSpace, ignoreSpaceChange, ignoreBlankLines, "");
            outputStrings = readLines(new StringReader(executionResult.getOutput()), MAX_LINES, ignoreAllSpace, ignoreSpaceChange, ignoreBlankLines, "OUTPUT> ");
            errorStrings = readLines(new StringReader(executionResult.getErrors()), MAX_LINES, ignoreAllSpace, ignoreSpaceChange, ignoreBlankLines, "ERROR> ");
        } catch (IOException e) {
            throw new TestingException("IO error", e);
        }
        obtainedStrings.addAll(outputStrings);
        if (compareErrors) {
            obtainedStrings.addAll(errorStrings);
        }
        if (compareExitCode) {
            obtainedStrings.add("EXIT CODE> " + executionResult.getExitCode());
        }
        String expectedStringsArray[] = new String[expectedStrings.size()];
        String obtainedStringsArray[] = new String[obtainedStrings.size()];
        expectedStringsArray = expectedStrings.toArray(expectedStringsArray);
        obtainedStringsArray = obtainedStrings.toArray(obtainedStringsArray);
        Diff diff = new Diff(expectedStringsArray, obtainedStringsArray);
        Diff.change change = diff.diff_2(false);
        int insertions = countInsertions(change);
        int deletions = countDeletions(change);
        int averageChange = (insertions + deletions) / 2;
        int correctLines = Math.max(0, obtainedStringsArray.length - averageChange);
        TestResult testResult = new TestResult();
        testResult.setFinishTime(new Date());
        testResult.setMaxMark(Math.max(obtainedStringsArray.length, expectedStringsArray.length));
        testResult.setResult(correctLines);
        testResult.setComment("Output had " + insertions + " added line(s) and " + deletions + " removed line(s) compared to expected output, receiving a penalty of " + (averageChange) + ".");
        DiffPrint.Base print = new DiffPrint.UnifiedPrint(expectedStringsArray, obtainedStringsArray);
        StringWriter output = new StringWriter();
        output.append("Execution result\n");
        output.append("========= =====\n");
        for (String line : outputStrings) {
            output.append(line + "\n");
        }
        for (String line : errorStrings) {
            output.append(line + "\n");
        }
        output.append("EXIT CODE> " + executionResult.getExitCode());
        output.append("\n\n");
        output.append("Test result\n");
        output.append("==== ======\n");
        output.append(" - Comparing output lines\n");
        output.append(" - " + (compareErrors ? "Comparing" : "Ignoring") + " error lines\n");
        output.append(" - " + (compareExitCode ? "Comparing" : "Ignoring") + " exit code\n");
        print.setOutput(output);
        print.print_header("Missing output", "Extra output");
        try {
            print.print_script(change);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            aioobe.printStackTrace();
            testResult.setOutput("Diff output unavailable due to a bug in BOSS.");
        }
        testResult.setOutput(output.getBuffer().toString());
        return testResult;
    }
