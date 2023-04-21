    public String readURL(URL url, GoogleSearch search) throws GoogleSearchFault, IOException {
        String txt;
        try {
            InputStream inputStream = url.openStream();
            txt = htmlToTxt(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getStackTrace().toString());
            logger.info("Exception Encountered: document will be retrieved from cache.");
            byte[] byteArray = search.doGetCachedPage(url.toString());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
            txt = htmlToTxt(inputStream);
            inputStream.close();
        }
        return txt;
    }
