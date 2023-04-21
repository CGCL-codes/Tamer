    public final void testGetChannelDAO() throws Exception {
        Object dao = DAOFactory.getChannelDAO(testHibernateSessionFactory);
        assertEquals(dao.getClass(), ChannelHibernateDAO.class);
        assertEquals(((IChannelDAO) dao).getSessionFactory(), testHibernateSessionFactory);
    }
