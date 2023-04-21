    @Test
    public void testRetrieveMessages() throws JCouplingException {
        ArrayList<Message> array = new ArrayList<Message>();
        List<Integer> numberarray = Arrays.asList(new Integer(297), new Integer(298), new Integer(299));
        logger.debug("Retrieving messages ...");
        array = dMapper.retrieveMessages(numberarray);
        logger.debug("Retrieved Ids:");
        for (int i = 0; i < array.size(); i++) {
            logger.debug("Message " + (i + 1));
            logger.debug("ID: " + array.get(i).getID());
            logger.debug("ChannelID: " + array.get(i).getChannelID());
            logger.debug("TimeStamp: " + array.get(i).getTimeStamp());
        }
        logger.debug("Done");
    }
