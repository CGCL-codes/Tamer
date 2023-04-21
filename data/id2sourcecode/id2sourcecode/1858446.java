    @Test
    public void testFindAll() {
        dao.saveAll(exceptionList);
        Set<ExceptionInfo> list = dao.findAll();
        assertTrue("basic read/write test", list.size() == 1);
    }
