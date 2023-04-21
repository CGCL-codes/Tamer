    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String message = new Date().toString();
        ChannelServiceFactory.getChannelService().<ChannelMessage<String>>getChannel("gwt-channel", false).write(new ChannelMessage<String>(message));
        resp.getWriter().write("<html><body><h1>$</h1></body></html>".replace("$", message));
    }
