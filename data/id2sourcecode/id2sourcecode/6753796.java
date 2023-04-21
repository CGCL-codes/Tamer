    private Document getDocument(String searchWord) {
        Document document = null;
        try {
            URL url = new URL("http://dict.cn/ws.php?utf8=true&q=" + searchWord);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
                document = documentBuilder.parse(in);
            }
        } catch (SAXException ex) {
            Logger.getLogger(Jict.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Jict.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Jict.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Jict.class.getName()).log(Level.SEVERE, null, ex);
        }
        return document;
    }
