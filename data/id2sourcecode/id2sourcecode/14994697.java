    private InputStream getXLSFromURL(String urlstring) throws MalformedURLException, IOException {
        URL url = new URL(urlstring);
        URLConnection uc = url.openConnection();
        String field = uc.getHeaderField(0);
        for (int i = 0; field != null; i++) {
            System.out.println(field);
            field = uc.getHeaderField(i);
        }
        BufferedInputStream is = new BufferedInputStream(uc.getInputStream());
        return is;
    }
