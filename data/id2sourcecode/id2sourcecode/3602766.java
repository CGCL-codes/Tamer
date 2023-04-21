    @SuppressWarnings("unchecked")
    private void processEditAddAction(ActionRequest request, ActionResponse response, AlertHandler alertHandler, Resources resources, SettingsBean readBean, SettingsBean writeBean) {
        String url = request.getParameter(INPUT_ADD_FEED);
        try {
            FeedHelper.getInstance().getFeed(readBean, url);
            LinkedList feeds = readBean.getFeeds();
            feeds.add(url);
            writeBean.setFeeds(feeds);
            writeBean.setSelectedFeed(url);
        } catch (MalformedURLException mue) {
            alertHandler.setError(resources.get("invalid_url"), mue.getMessage());
            getPortletConfig().getPortletContext().log("could not add feed", mue);
        } catch (UnknownHostException uhe) {
            alertHandler.setError(resources.get("invalid_url"), uhe.getMessage());
        } catch (FileNotFoundException fnfe) {
            alertHandler.setError(resources.get("invalid_url"), fnfe.getMessage());
            getPortletConfig().getPortletContext().log("could not add feed", fnfe);
        } catch (IllegalArgumentException iae) {
            alertHandler.setError(resources.get("invalid_url"), iae.getMessage());
            getPortletConfig().getPortletContext().log("could not add feed", iae);
        } catch (FeedException fe) {
            alertHandler.setError(resources.get("invalid_url"), fe.getMessage());
            getPortletConfig().getPortletContext().log("could not add feed", fe);
        } catch (IOException ioe) {
            alertHandler.setError(resources.get("invalid_url"), ioe.getMessage());
            getPortletConfig().getPortletContext().log("could not add feed", ioe);
        }
    }
