    @SuppressWarnings("unchecked")
    private void processEditAction(ActionRequest request, ActionResponse response, AlertHandler alertHandler, Resources resources, SettingsBean readBean, SettingsBean writeBean) throws PortletModeException {
        String[] checkedFeeds = request.getParameterValues(INPUT_FEEDS);
        if (checkedFeeds == null) {
            writeBean.setFeeds(new LinkedList());
        } else {
            LinkedList feeds = new LinkedList(Arrays.asList(checkedFeeds));
            writeBean.setFeeds(feeds);
            String startFeed = request.getParameter(INPUT_START_FEED);
            if (startFeed != null && feeds.contains(startFeed)) {
                writeBean.setStartFeed(startFeed);
            }
            if (readBean.getSelectedFeed() != null && !feeds.contains(readBean.getSelectedFeed())) {
                String selectedFeed = writeBean.getStartFeed();
                writeBean.setSelectedFeed(selectedFeed);
            }
            if (readBean.getFeeds().size() == 0) {
                writeBean.setSelectedFeed(writeBean.getStartFeed());
            }
        }
        String s = request.getParameter(INPUT_MAX_AGE);
        if (s != null && s.length() > 0) {
            try {
                int n = Integer.parseInt(s);
                if (n < 1) {
                    alertHandler.setError(resources.get("enter_a_whole_number_greater_than_zero"));
                } else {
                    writeBean.setMaxAge(n);
                }
            } catch (NumberFormatException nfe) {
                alertHandler.setError(resources.get("enter_a_whole_number_greater_than_zero"));
            }
        }
        String maxEntries = request.getParameter(INPUT_MAX_ENTRIES);
        if (maxEntries != null && maxEntries.length() > 0) {
            try {
                int n = Integer.parseInt(maxEntries);
                if (n < 1) {
                    alertHandler.setError(resources.get("enter_a_whole_number_greater_than_zero"));
                } else {
                    writeBean.setMaxEntries(n);
                }
            } catch (NumberFormatException nfe) {
                alertHandler.setError(resources.get("enter_a_whole_number_greater_than_zero"));
            }
        }
        String[] disableMaxAge = request.getParameterValues(INPUT_DISABLE_MAX_AGE);
        if (disableMaxAge != null && disableMaxAge.length > 0) {
            writeBean.setDisableMaxAge(true);
        } else {
            writeBean.setDisableMaxAge(false);
        }
        String[] newWindow = request.getParameterValues(INPUT_NEWWIN);
        if (newWindow != null && newWindow.length > 0) {
            writeBean.setNewWindow(true);
        } else {
            writeBean.setNewWindow(false);
        }
        if (!alertHandler.isErrorExists()) {
            response.setPortletMode(PortletMode.VIEW);
        }
    }
