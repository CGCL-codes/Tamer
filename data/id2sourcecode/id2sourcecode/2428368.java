    public static void testPostShow2(KyteSession session, String channelUri) throws Exception {
        Channel channel = ServiceFactory.getChannelService().fetchChannel(session, channelUri);
        String fileName = "c:\\Sunset.jpg";
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        Map<String, Object> showData = new HashMap<String, Object>();
        showData.put("title", "a test show");
        String showUri = channel.postShow(session, showData, fis, file.length(), "image/jpg");
        LOGGER.info("postShow result: " + showUri);
    }
