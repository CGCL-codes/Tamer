    public static InputStream getInputStream(String urlAsString) {
        InputStream result = null;
        URL url;
        try {
            url = new URL(urlAsString);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if (con.getResponseCode() >= 300) {
                Log.warn(con.getResponseMessage());
            } else {
                result = con.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
