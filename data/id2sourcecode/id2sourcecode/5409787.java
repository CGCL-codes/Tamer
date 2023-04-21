    public static boolean isOnline() {
        try {
            URL url = new URL(currentRevision);
            url.openStream().close();
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }
