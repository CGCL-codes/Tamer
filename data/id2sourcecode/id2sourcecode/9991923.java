    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String message = new Date().toString();
        ChannelServiceFactory.getChannelService().<String>getChannel("gwt-channel", true).write(new ChannelMessage<String>(message));
        resp.getWriter().write("<h1>" + message + "</h1>");
    }
