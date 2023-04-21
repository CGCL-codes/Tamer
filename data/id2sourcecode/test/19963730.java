    private String getVersionFile() {
        String serverfile = "unknown";
        try {
            URL url;
            URLConnection urlConn;
            BufferedReader dis;
            url = new URL("http://www.stompd.org/stompd.current");
            urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            dis = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            serverfile = dis.readLine();
            dis.close();
        } catch (MalformedURLException mue) {
            serverfile = mue.getLocalizedMessage();
        } catch (IOException ioe) {
            serverfile = ioe.getLocalizedMessage();
        }
        return serverfile;
    }
