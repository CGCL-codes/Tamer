    public final void testGetDefaultChannelSourceDAO() throws Exception {
        Object dao = DAOFactory.getChannelSourceDAO(testDefaultHTTPClientSessionFactory);
        assertEquals(dao.getClass(), ChannelHTTPClientDAO.class);
        assertEquals(((IDAO) dao).getSessionFactory(), testDefaultHTTPClientSessionFactory);
    }
