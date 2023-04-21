    public final void testGetChannelSourceDAO() throws Exception {
        Object dao = DAOFactory.getChannelSourceDAO(testHTTPClientSessionFactory);
        assertEquals(dao.getClass(), ChannelHTTPClientDAO.class);
        assertEquals(((IDAO) dao).getSessionFactory(), testHTTPClientSessionFactory);
    }
