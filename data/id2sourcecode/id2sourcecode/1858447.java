    @Test
    public void testFindAllRelease_1_0_RC1() {
        TestEnvironmentConfiguration.setExceptionInformationXmlFileName(LEGACY_RC1_EXCEPTION_XML_FILE_NAME);
        Set<ExceptionInfo> list = dao.findAll();
        assertTrue("basic read/write test", list.size() == 13);
    }
