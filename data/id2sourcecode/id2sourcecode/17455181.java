    public void setupChannelTests() {
        try {
            getTestContext().setBaseUrl("http://localhost/springworkflow");
            beginAt("/index.go");
            assertLinkPresent("adminMethods");
            clickLink("adminMethods");
            assertFormElementPresent("username");
            assertFormElementPresent("password");
            assertButtonPresent("login");
            setFormElement("username", "system");
            setFormElement("password", "manager");
            clickButton("login");
            clickLink("getChannelsWithNews");
            clickButton("newChannel");
            assertFormElementPresent("title");
            assertFormElementPresent("url");
            setFormElement("title", "test new channel");
            setFormElement("url", "http://localhost/springworkflow/channel.xml");
            selectOption("html", "No");
            clickButton("saveAndPoll");
            assertTextNotPresent("test new channel");
            assertTextPresent("Updated title");
            int channelID = template.queryForInt("select channelID from channel where " + "url='http://localhost/springworkflow/channel.xml'");
            List items = template.queryForList("select * from item where channelID=" + channelID);
            assertTrue(items.size() == 2);
            IChannelDAO channelDao = (IChannelDAO) context.getBean("channelDAO");
            List results = channelDao.findChannelsByUrl("http://localhost/springworkflow/channel.xml");
            assertTrue(results.size() == 1);
            Channel insertedChannel = (Channel) results.get(0);
            assertTrue(insertedChannel.getNumberOfItems() == 2);
            assertTrue(insertedChannel.getNumberOfRead() == 0);
            assertTrue(!insertedChannel.isHtml());
        } catch (AssertionFailedError e) {
            dumpResponse(System.out);
            throw new AssertionFailedError(e.getMessage());
        }
    }
