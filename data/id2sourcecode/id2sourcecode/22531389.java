    public void testReadUrl() throws IOException, DocumentException, SAXException {
        String urlStr = "";
        URL url;
        HttpURLConnection conn;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.getResponseCode();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println(conn.getURL().toString());
        if (urlStr.equals(conn.getURL().toString())) {
            String pageStr = WgetUtil.wgetTxt(conn);
            Document doc = DocumentHelper.parseText(pageStr);
            Element root = doc.getRootElement();
            System.out.println("size: " + root.elements().size());
        } else {
            System.out.println("redirect");
        }
    }
