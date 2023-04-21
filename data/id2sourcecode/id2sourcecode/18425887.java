    private ActionMessage checkURL(String u) {
        if (u == null) {
            return new ActionMessage("error.mapserverurl.null");
        }
        URL url = null;
        try {
            url = new URL(u);
            url.openStream();
        } catch (Exception e) {
            return new ActionMessage("error.mapserverurl.invalid", e.getMessage());
        }
        return null;
    }
