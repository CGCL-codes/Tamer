    public final void testGetDefaultChannelDAO() throws Exception {
        Object dao = DAOFactory.getChannelDAO(testDefaultHibernateSessionFactory);
        assertEquals(dao.getClass(), ChannelHibernateDAO.class);
        assertEquals(((IChannelDAO) dao).getSessionFactory(), testDefaultHibernateSessionFactory);
    }
