    private void generateModuleTestCaseSpreadList(BufferedWriter buf, String moduleName) throws IOException {
        buf.write(t(1) + "<TABLE id=\"spread_table\" cellspacing=\"0\" cellpadding=\"2\">" + c_LF);
        buf.write(t(2) + "<THEAD>" + c_LF);
        generateTestCaseSpreadHeader(buf);
        buf.write(t(2) + "</THEAD>" + c_LF);
        buf.write(t(2) + "<TBODY>" + c_LF);
        generateModuleTestCaseSpreadItems(buf, moduleName);
        buf.write(t(2) + "</TBODY>" + c_LF);
        buf.write(t(1) + "</TABLE>" + c_LF);
    }
