    public InputStream askNodeForKey(HttpServletResponse resp, String address, String path, int htl) throws IOException, MalformedURLException {
        System.out.println("Asking " + address + " for " + path);
        Config.mapper.request(Config.address, address, path);
        URL url = makeURL(address, path, htl);
        System.out.println("url: " + url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        System.out.println("conn: " + conn);
        int code = getResponseCode(conn);
        if (code == conn.HTTP_OK) {
            System.out.println("Key " + path + " found on " + address);
            return conn.getInputStream();
        } else {
            System.out.println("File could not be found in the network");
            printError(resp, code, "Not found.");
            return null;
        }
    }
