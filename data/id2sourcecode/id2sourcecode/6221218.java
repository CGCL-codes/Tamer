    private String readInformationURL(URL url) throws IOException {
        BufferedInputStream is = (BufferedInputStream) url.openStream();
        StringBuffer text = new StringBuffer();
        int read = 0;
        while ((read = is.read()) != -1) {
            text.append((char) read);
        }
        is.close();
        return text.toString();
    }
