    public void testLoadThreadCount() throws IOException, CruiseControlException {
        FileWriter configOut = new FileWriter(configFile);
        writeHeader(configOut);
        configOut.write("<system><configuration>" + "<threads count=\"2\"/>" + "</configuration></system>");
        writeFooterAndClose(configOut);
        ccController.setConfigFile(configFile);
        assertEquals(configFile, ccController.getConfigFile());
        assertTrue(ccController.getProjects().isEmpty());
    }
