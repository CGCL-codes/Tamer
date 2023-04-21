    @Test
    public void testFindAllRelease_1_0_RC2() {
        TestEnvironmentConfiguration.setExceptionInformationXmlFileName(LEGACY_RC2_EXCEPTION_XML_FILE_NAME);
        Set<ExceptionInfo> list = dao.findAll();
        assertTrue("basic read/write test", list.size() == 13);
    }
