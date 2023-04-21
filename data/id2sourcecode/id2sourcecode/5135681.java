    public NodeInfo[] searchNodes(String searchQuery, int loadProperties) {
        NodeInfo[] nodes = null;
        try {
            String query = mServer + "search.php" + ("?query=" + URLEncoder.encode(searchQuery, "UTF-8")) + ("&mask=" + loadProperties);
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setAllowUserInteraction(false);
            conn.setRequestMethod("GET");
            setCredentials(conn);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream stream = conn.getInputStream();
                MimeType contentType = new MimeType(conn.getContentType());
                if (contentType.getBaseType().equals("text/xml")) {
                    try {
                        JAXBContext context = JAXBContext.newInstance(NetProcessorInfo.class);
                        Unmarshaller unm = context.createUnmarshaller();
                        NetProcessorInfo root = (NetProcessorInfo) unm.unmarshal(stream);
                        if (root != null) {
                            nodes = root.getNodes();
                        }
                    } catch (Exception ex) {
                    }
                }
                stream.close();
            }
        } catch (Exception ex) {
        }
        return (nodes != null) ? nodes : new NodeInfo[0];
    }
