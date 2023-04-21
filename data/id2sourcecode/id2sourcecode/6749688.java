    @Test
    public void dragChannelName() throws AWTException {
        JTableOperator table = tester.getTable("channelTable");
        JTableHeaderOperator header = table.getHeaderOperator();
        header.selectColumn(0);
        Channels channels = tester.getContext().getShow().getChannels();
        channels.get(0).setName("Channel 1");
        channels.get(1).setName("Channel 2");
        channels.get(2).setName("Channel 3");
        Point start = tester.getTableCellLocation("channelTable", 2, 1);
        Point end = tester.getTableCellLocation("channelTable", 0, 1);
        Robot robot = new Robot();
        robot.setAutoWaitForIdle(true);
        robot.mouseMove(start.x, start.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseMove(10, start.y);
        Util.sleep(100);
        robot.mouseMove(end.x, end.y);
        Util.sleep(100);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        Util.sleep(100);
        assertEquals(channels.get(0).getName(), "Channel 3");
        assertEquals(channels.get(1).getName(), "Channel 1");
        assertEquals(channels.get(2).getName(), "Channel 2");
    }
