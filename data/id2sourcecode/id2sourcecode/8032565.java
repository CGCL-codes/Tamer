    public static boolean isExternalLinkWorking(ExternalLink link) {
        if (link.getUrl() == null) return false;
        try {
            URL url = new URL(link.getUrl());
            url.openStream().close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
