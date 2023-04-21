    private static String readDocument(String location) throws MalformedURLException, IOException {
        String content;
        URL url = new URL(location);
        URLConnection con = url.openConnection();
        InputStream in = con.getInputStream();
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, read));
        }
        content = sb.toString();
        return content;
    }
