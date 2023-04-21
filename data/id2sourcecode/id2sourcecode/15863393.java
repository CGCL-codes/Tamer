    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        Random randomGenerator = new Random();
        String clientid = Integer.toString(randomGenerator.nextInt(100000));
        String token = channelService.createChannel(clientid);
        persistId(clientid);
        resp.getWriter().println(token);
    }
