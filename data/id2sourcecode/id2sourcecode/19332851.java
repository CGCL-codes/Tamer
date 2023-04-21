    public void sendCookie(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Cookie", "User.username=tmblue; User.password=maleand");
            conn.connect();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    }
