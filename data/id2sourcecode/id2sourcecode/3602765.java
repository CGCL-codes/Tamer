    public void processAction(ActionRequest request, ActionResponse response) throws UnavailableException, PortletSecurityException, PortletException, IOException {
        Resources resources = new Resources("com.sun.portal.rssportlet.RssPortlet", request.getLocale());
        AlertHandler ah = (AlertHandler) request.getPortletSession().getAttribute("alertHandler", PortletSession.PORTLET_SCOPE);
        SettingsBean readBean = new SettingsBean();
        SettingsHandler handler = new SettingsHandler();
        handler.setPortletConfig(getPortletConfig());
        handler.setPortletRequest(request);
        handler.setSettingsBean(readBean);
        SettingsBean writeBean = new SettingsBean();
        if (request.getParameter(SUBMIT_ADD) != null) {
            processEditAddAction(request, response, ah, resources, readBean, writeBean);
        } else if (request.getParameter(SUBMIT_GO) != null || request.getParameter(INPUT_SELECT_FEED) != null) {
            processGoAction(request, response, writeBean);
        } else if (request.getParameter(SUBMIT_CANCEL) != null) {
            processEditCancelAction(request, response);
        } else if (request.getParameter(SUBMIT_EDIT) != null) {
            processEditAction(request, response, ah, resources, readBean, writeBean);
        }
        handler.persistSettingsBean(writeBean);
    }
