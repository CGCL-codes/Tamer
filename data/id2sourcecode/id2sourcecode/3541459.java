    public static boolean urlExists(String strUrl) {
        NoMuleRuntime.showDebug("UrlExists : " + strUrl);
        try {
            URL url = new URL(strUrl);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            in.close();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }
        return false;
    }
