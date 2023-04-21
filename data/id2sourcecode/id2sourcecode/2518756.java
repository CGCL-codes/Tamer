    public static Channel getChannel(HttpSession session) {
        synchronized (session) {
            Channel channel = (Channel) session.getAttribute(CHANNEL_SESSION_ATTRIBUTE);
            if (channel == null) {
                channel = JspApplication.getEngine(session.getServletContext()).newChannel();
                session.setAttribute(CHANNEL_SESSION_ATTRIBUTE, channel);
            }
            return channel;
        }
    }
