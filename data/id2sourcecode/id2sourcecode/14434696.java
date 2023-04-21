    public boolean reject(String uri) throws Exception {
        URISplit split = URISplit.parse(uri);
        if (split.file == null || split.file.equals("file:///")) {
            split.file = "http://www-pw.physics.uiowa.edu/das/das2Server";
        }
        String s = split.file.toString();
        if (s.equals("http://www-pw.physics.uiowa.edu/das/das2Server")) {
            return false;
        }
        URL url = new URL(s + "?server=logo");
        URLConnection connect = url.openConnection();
        try {
            InputStream in = connect.getInputStream();
            if (connect.getContentType().startsWith("image")) {
                in.close();
                return false;
            } else {
                in.close();
                return true;
            }
        } catch (IOException ex) {
            return false;
        }
    }
