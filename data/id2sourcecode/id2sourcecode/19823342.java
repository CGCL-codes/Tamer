    BrowseNodeModel loadTree(URL url) {
        BrowseNodeModel root = null;
        BrowseXMLElement mapElement = new BrowseXMLElement(getFrame());
        InputStreamReader urlStreamReader = null;
        URLConnection uc = null;
        try {
            urlStreamReader = new InputStreamReader(url.openStream());
        } catch (AccessControlException ex) {
            getFrame().getController().errorMessage("Could not open URL " + url.toString() + ". Access Denied.");
            System.err.println(ex);
            return null;
        } catch (Exception ex) {
            getFrame().getController().errorMessage("Could not open URL " + url.toString() + ".");
            System.err.println(ex);
            return null;
        }
        try {
            mapElement.parseFromReader(urlStreamReader);
        } catch (Exception ex) {
            System.err.println(ex);
            return null;
        }
        mapElement.processUnfinishedLinks(getLinkRegistry());
        root = (BrowseNodeModel) mapElement.getMapChild();
        return root;
    }
