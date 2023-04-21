    public static InputStream httpPost(URL url, String path, String name, String value) throws IOException {
        dbg("httpPost: " + url + " path=" + path + " " + name + "=" + value);
        URLConnection c = url.openConnection();
        c.setDoOutput(true);
        PrintWriter out = new PrintWriter(c.getOutputStream());
        int index = path.indexOf("/");
        if (index != -1) {
            path = path.substring(index);
        } else {
            path = "/.";
        }
        dbg("  path: " + path);
        out.print("path=" + URLEncoder.encode(path, "UTF-8"));
        if (name != null) {
            out.print("&" + name + "=" + URLEncoder.encode(value, "UTF-8"));
        }
        out.close();
        return c.getInputStream();
    }
