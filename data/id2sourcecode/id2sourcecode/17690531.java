    public boolean login(String serverpath, String username, String password) {
        serverURL = serverpath;
        URL url;
        try {
            url = new URL(serverURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
