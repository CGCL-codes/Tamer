    private void getPlayerStatus() throws Exception {
        playerState = new PlayerState();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setIgnoringComments(true);
        DocumentBuilder docBuild;
        try {
            docBuild = dbf.newDocumentBuilder();
            docBuild.setErrorHandler(new XMLParseErrorHandler());
        } catch (Exception e) {
            throw new Exception("Error: could not query player status: " + e.getMessage());
        }
        URL url = new URL("http://" + httpHostVal + "/requests/status.xml");
        URLConnection conn = url.openConnection();
        conn.connect();
        Document state = docBuild.parse(conn.getInputStream());
        String random = state.getElementsByTagName(vlcRandom).item(0).getFirstChild().getNodeValue().trim();
        playerState.random = Integer.valueOf(random).intValue() == 1;
    }
