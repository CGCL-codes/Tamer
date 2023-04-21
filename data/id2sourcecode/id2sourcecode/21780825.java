    private void generatePackageTestCaseSpreadList(BufferedWriter buf, String packageName) throws IOException {
        buf.write(t(1) + "<TABLE id=\"spread_table\" cellspacing=\"0\" cellpadding=\"2\">" + c_LF);
        buf.write(t(2) + "<THEAD>" + c_LF);
        generateTestCaseSpreadHeader(buf);
        buf.write(t(2) + "</THEAD>" + c_LF);
        buf.write(t(2) + "<TBODY>" + c_LF);
        generatePackageTestCaseSpreadItems(buf, packageName);
        buf.write(t(2) + "</TBODY>" + c_LF);
        buf.write(t(1) + "</TABLE>" + c_LF);
    }
