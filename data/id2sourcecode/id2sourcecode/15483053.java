    public Node parse(URL locationURL) throws ParserException {
        try {
            HttpURLConnection urlCon = (HttpURLConnection) locationURL.openConnection();
            urlCon.setRequestMethod("GET");
            InputStream urlIn = urlCon.getInputStream();
            Node rootElem = parse(urlIn);
            urlIn.close();
            urlCon.disconnect();
            return rootElem;
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }
