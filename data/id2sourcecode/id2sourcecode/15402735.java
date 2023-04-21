    public static String getDocumentAsString(URL url) throws IOException {
        StringBuffer result = new StringBuffer();
        InputStream in = url.openStream();
        int c;
        while ((c = in.read()) != -1) result.append((char) c);
        String result_to_string = result.toString();
        return result_to_string;
    }
