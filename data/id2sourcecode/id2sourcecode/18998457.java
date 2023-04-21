    protected void generateTestCaseSpreadInfo(BufferedWriter buf, boolean isModule) throws IOException {
        buf.write("<a name=\"spread\"/><H3 class=\"title\">Test case verdict spread in this ");
        if (isModule) {
            buf.write("module");
        } else {
            buf.write("package");
        }
        buf.write("</H3>" + c_LF);
        buf.write("This list shows the test case verdicts in the order they occured during the test runs. If a regular HTML report has been generated click on a spread item to jump directly to the test report. Legend: (green=Pass, grey=No-run, red=Fail/Error)<br/><br/>" + c_LF);
    }
