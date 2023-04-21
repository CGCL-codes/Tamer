    public boolean wget(String url, String location) {
        URL urlo;
        try {
            urlo = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        String path = getFileNameFromURL(urlo.getPath());
        if (location.substring(location.length() - 1).equals("/")) path = location + path; else path = location + "/" + path;
        URLConnection conn;
        try {
            conn = urlo.openConnection();
            InputStream in = conn.getInputStream();
            OutputStream out = new FileOutputStream(path);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
