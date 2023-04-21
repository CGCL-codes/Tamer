    public boolean open() {
        try {
            URL url = new URL(resource);
            URLConnection conn = url.openConnection();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encode));
        } catch (MalformedURLException e) {
            log.error("Uable to connect URL:" + resource, e);
            return false;
        } catch (IOException e) {
            log.error("IOExeption when connecting to URL" + resource, e);
            return false;
        }
        return true;
    }
