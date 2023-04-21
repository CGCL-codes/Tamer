    @Test
    public void testFindAllRelease_1_0_RC2() {
        TestEnvironmentConfiguration.setPerformanceInformationXmlFileName(LEGACY_RC2_PERFORMANCE_XML_FILE_NAME);
        Set<TaskTimer> list = dao.findAll();
        assertTrue("basic read/write test.  size=" + list.size(), list.size() == 55);
    }
