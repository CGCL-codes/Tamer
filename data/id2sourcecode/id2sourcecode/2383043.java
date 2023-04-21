    @SuppressWarnings("unchecked")
    public ActionForward windowsLiveAuthReturn(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        WindowsLiveLogin windowsLiveLogin = getWindowsLiveLogin();
        WindowsLiveLogin.ConsentToken consentToken = windowsLiveLogin.processConsent(request.getParameterMap());
        if (consentToken != null && consentToken.isValid()) {
            try {
                URL url = new URL("https://livecontacts.services.live.com/users/@L@" + consentToken.getLocationID() + "/rest/LiveContacts/Contacts");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                setupConnection(connection, consentToken.getDelegationToken());
                connection.connect();
                WindowsLiveXmlParser windowsLiveXmlParser = WindowsLiveXmlParser.getInstance();
                InputStream inputStream = connection.getInputStream();
                if (inputStream == null) {
                    log.error("Input stream received from connection is null!");
                } else {
                    Contacts contacts = windowsLiveXmlParser.convertoXml2Object(inputStream);
                    request.setAttribute("contacts", contacts);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return mapping.findForward("windowsLiveContacts");
    }
