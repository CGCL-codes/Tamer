    public void openDocument(URL url) throws IOException {
        inputStream = url.openStream();
        String encoding = getCharsetFromHTML(inputStream);
        try {
            inputStream.close();
        } catch (IOException closeIOE) {
        }
        inputStream = url.openStream();
        if (encoding != null) {
            try {
                htmlParser = new HTMLParser(new InputStreamReader(inputStream, encoding));
            } catch (UnsupportedEncodingException uee) {
                htmlParser = new HTMLParser(new InputStreamReader(inputStream));
            }
        } else {
            htmlParser = new HTMLParser(new InputStreamReader(inputStream));
        }
    }
