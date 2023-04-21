    private String getUrlContent(String address) throws MalformedURLException, IOException {
        URL url;
        Reader urlIn;
        if (address.startsWith("http:")) {
            url = new URL(address);
            urlIn = new InputStreamReader(url.openStream());
        } else {
            url = new URL(new URL(servReq.getScheme(), servReq.getServerName(), servReq.getServerPort(), servReq.getPathInfo()), address);
            System.out.println("Including: " + url.toString());
            urlIn = new InputStreamReader(url.openStream());
        }
        StringWriter out = new StringWriter();
        int ch = urlIn.read();
        while (ch != -1) {
            out.write(ch);
            ch = urlIn.read();
        }
        out.flush();
        return out.toString();
    }
