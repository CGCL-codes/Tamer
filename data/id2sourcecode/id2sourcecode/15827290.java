    public final void testTVListService() {
        IChannelDAO channelDAO = DAOFactory.getChannelDAO(TestUtils.getHibernateSessionFactory());
        IBroadcastDAO broadcastDAO = DAOFactory.getBroadcastDAO(TestUtils.getHibernateSessionFactory());
        TVListService service = new TVListService(channelDAO, broadcastDAO);
        assertNotNull("service is null", service);
        assertEquals("channelDAO", service.getChannelDAO(), channelDAO);
        assertEquals("broadcastDAO", service.getBroadcastDAO(), broadcastDAO);
    }
