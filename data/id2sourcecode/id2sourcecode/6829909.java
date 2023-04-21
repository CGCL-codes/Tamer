    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        String parameter = request.getParameter("type");
        if (StringUtils.isBlank(parameter)) {
            parameter = "spy";
        }
        StringBuffer sbuff = new StringBuffer(1000);
        sbuff.append("<a href='").append(path).append("?type=spy'>").append("SpyComponents").append("</a>&nbsp;");
        sbuff.append("<a href='").append(path).append("?type=alert'>").append("AlertComponents").append("</a>&nbsp;");
        sbuff.append("<a href='").append(path).append("?type=channel'>").append("ChannelAwareComponents").append("</a>&nbsp;");
        sbuff.append("<a href='").append(path).append("?type=rule'>").append("AlertRule").append("</a><br>");
        if (componentContext == null) {
            componentContext = getComponentContext();
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        List<Component> components = componentContext.getComponents();
        int size = components.size();
        List<SpyComponent> spyComponents = new ArrayList<SpyComponent>(size);
        List<AlertComponent> alertComponents = new ArrayList<AlertComponent>(size);
        List<MessageAlertChannelActiveAwareComponent> channelAwareComponents;
        channelAwareComponents = new ArrayList<MessageAlertChannelActiveAwareComponent>(size);
        CoreComponent coreComponent = null;
        for (Component component : components) {
            if (component instanceof SpyComponent) {
                spyComponents.add((SpyComponent) component);
            } else if (component instanceof AlertComponent) {
                alertComponents.add((AlertComponent) component);
            } else if (component instanceof MessageAlertChannelActiveAwareComponent) {
                channelAwareComponents.add((MessageAlertChannelActiveAwareComponent) component);
            } else if (component instanceof CoreComponent) {
                coreComponent = (CoreComponent) component;
            }
        }
        sbuff.append("<b>Components list:</b><br><br><br>");
        if ("spy".equals(parameter)) {
            getSpyHtmlView(spyComponents, sbuff);
        } else if ("alert".equals(parameter)) {
            getAlertHtmlView(alertComponents, sbuff);
        } else if ("channel".equals(parameter)) {
            getChannelHtmlView(channelAwareComponents, sbuff);
        } else if ("rule".equals(parameter)) {
            if (coreComponent != null) {
                getAlertRuleHtmlView(coreComponent.getAlertRule(), sbuff);
            }
        }
        response.getWriter().println(sbuff);
    }
