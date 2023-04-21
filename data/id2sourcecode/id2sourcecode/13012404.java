    public void testCreateChannelItems() throws Exception {
        ChannelBuilder builder = new ChannelBuilder(session);
        Transaction tx = null;
        int chId = -1;
        try {
            tx = session.beginTransaction();
            String chanName = "Foo Test Channel";
            ChannelIF channel = builder.createChannel(chanName, "http://www.nava.de/channelTest");
            channel.setDescription("Test Channel: " + chanName);
            session.saveOrUpdate(channel);
            ItemIF item1 = builder.createItem(channel, "Item 1 for " + chanName, "First in line", new URL("http://www.sf1.net"));
            session.saveOrUpdate(item1);
            ItemIF item2 = builder.createItem(channel, "Item 2 for " + chanName, "Second in line", new URL("http://www.sf1.net"));
            session.saveOrUpdate(item2);
            session.saveOrUpdate(channel);
            tx.commit();
            chId = (int) channel.getId();
        } catch (HibernateException he) {
            logger.warn("trying to rollback the transaction");
            if (tx != null) tx.rollback();
            throw he;
        }
        assertTrue("No valid channel created.", chId >= 0);
        try {
            logger.info("Searching for channel " + chId);
            Object result = session.get(Channel.class, new Long(chId));
            assertNotNull(result);
            ChannelIF c = (ChannelIF) result;
            logger.info("retrieved channel --> " + c);
            assertEquals(1, c.getItems().size());
            Iterator it_items = c.getItems().iterator();
            while (it_items.hasNext()) {
                ItemIF item = (ItemIF) it_items.next();
                logger.info("  * " + item);
                assertEquals(c, item.getChannel());
            }
        } catch (HibernateException he) {
            logger.warn("Error while querying for channel");
            throw he;
        }
    }
