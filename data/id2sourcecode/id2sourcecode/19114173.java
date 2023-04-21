    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Channel channel = Channel.getDefaultChannel();
            PrintWriter writer = response.getWriter();
            if (channel == null) {
                Page page = Page.findPage(Property.getPropertyValue("HomePage"));
                channel = Channel.create(Channel.DEFAULT_CHANNEL, page, page.getTemplate());
                List<Page> roots = Page.getRootPages();
                for (Page rootPage : roots) {
                    writer.println("Adding Page " + rootPage.getID() + " " + rootPage.getName() + " to the default channel <br/>");
                    channel.addPage(rootPage);
                }
            } else {
                writer.println("Default Channel already created");
            }
        } catch (Exception e) {
            throw new ServletException("Unable to execute the bean", e);
        }
    }
