    @Test
    public void testChannelServiceMXBean() throws Exception {
        ProfileConsumer cons = getCollector(serverNode).getConsumer(ProfileCollectorImpl.CORE_CONSUMER_PREFIX + "ChannelService");
        cons.setProfileLevel(ProfileLevel.MAX);
        ObjectName name = new ObjectName(ChannelServiceMXBean.MXBEAN_NAME);
        ChannelServiceMXBean bean = (ChannelServiceMXBean) profileCollector.getRegisteredMBean(ChannelServiceMXBean.MXBEAN_NAME);
        assertNotNull(bean);
        long create = (Long) mbsc.getAttribute(name, "CreateChannelCalls");
        long get = (Long) mbsc.getAttribute(name, "GetChannelCalls");
        ChannelServiceMXBean proxy = JMX.newMXBeanProxy(mbsc, name, ChannelServiceMXBean.class);
        assertTrue(create <= proxy.getCreateChannelCalls());
        assertTrue(get <= proxy.getGetChannelCalls());
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() {
                try {
                    serverNode.getChannelService().getChannel("foo");
                } catch (NameNotBoundException nnb) {
                    System.out.println("Got expected exception " + nnb);
                }
            }
        }, taskOwner);
        assertTrue(get < proxy.getGetChannelCalls());
        assertTrue(get < bean.getGetChannelCalls());
    }
