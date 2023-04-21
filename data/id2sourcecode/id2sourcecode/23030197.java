    public void test1() throws FilePersistenceException {
        session.open();
        final Long value = Long.valueOf("115465874564584225");
        session.setObject("key", value);
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        final Long valueReaded = (Long) session.getObject("key");
        assertEquals("readed must be equals to writed", value, valueReaded);
        session.close(EnumFilePersistenceCloseAction.DO_NOT_SAVE);
    }
