    public final void testSetDAOs() {
        IBroadcastDAO broadcastDAO = DAOFactory.getBroadcastDAO(TestUtils.getHibernateSessionFactory());
        IChannelDAO channelDAO = DAOFactory.getChannelDAO(TestUtils.getHibernateSessionFactory());
        TVListService service = new TVListService(DAOFactory.getChannelDAO(TestUtils.getHibernateSessionFactory()), DAOFactory.getBroadcastDAO(TestUtils.getHibernateSessionFactory()));
        assertNotNull("service is null", service);
        service.setBroadcastDAO(broadcastDAO);
        service.setChannelDAO(channelDAO);
        assertEquals("broadcastDAO", service.getBroadcastDAO(), broadcastDAO);
        assertEquals("channelDAO", service.getChannelDAO(), channelDAO);
    }
