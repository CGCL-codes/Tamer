    private void performAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String strAction = null;
        try {
            if (req.getParameterMap().containsKey(AppConstants.ACTION)) strAction = req.getParameter(AppConstants.ACTION);
            if (strAction.equalsIgnoreCase(AppConstants.GET_RSS)) {
                getRSS(resp, req);
            } else if (strAction.equalsIgnoreCase(AppConstants.GET_MORE_RSS)) {
                getMoreRSS(resp, req);
            } else if (strAction.equalsIgnoreCase(AppConstants.GET_CHANNELS)) {
                getChannels(resp);
            } else if (strAction.equalsIgnoreCase(AppConstants.GET_MORE_CHANNELS)) {
                getMoreChannels(resp, req);
            } else if (strAction.equalsIgnoreCase(AppConstants.CREATE_CHANNEL)) {
                createChannel(req);
            }
        } catch (Exception e) {
        }
    }
