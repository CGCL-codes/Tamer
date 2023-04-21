    public static void testPostShow(KyteSession session, String channelUri, String mediaUri) {
        Channel channel = ServiceFactory.getChannelService().fetchChannel(session, channelUri);
        Map<String, Object> showData = new HashMap<String, Object>();
        showData.put("title", "a test show");
        String showUri = channel.postShow(session, showData, "", mediaUri);
        LOGGER.info("postShow result: " + showUri);
    }
