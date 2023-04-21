    @Test
    public void testFindAll() {
        dao.saveAll(timerSet);
        Set<TaskTimer> list = dao.findAll();
        assertTrue("basic read/write test", list.size() == 2);
    }
