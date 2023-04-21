    public String[] uploadFile() {
        links = new String[fieldNames.length];
        try {
            URL url1 = new URL("www.zippyshare.com/upload");
            HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
            ClientHttpRequest req = new ClientHttpRequest(conn1);
            req.setParameter("file_0", new File(fileName));
            req.setParameter("terms", "1");
            req.post();
            Map<String, List<String>> field = conn1.getHeaderFields();
            String cookie = field.get("Set-Cookie").get(0);
            URL url2 = new URL("http://www1.zippyshare.com/links.jsp?link=1");
            HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
            conn2.setRequestMethod("GET");
            conn2.setRequestProperty("Cookie", cookie);
            conn2.setDoOutput(false);
            InputStream inStream = conn2.getInputStream();
            createLinks(inStream);
            conn1.disconnect();
            conn2.disconnect();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
            net.sourceforge.juploader.app.Error.showConnectionError();
        }
        return links;
    }
