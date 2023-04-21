    private long getLastModified(String location) throws IOException {
        URL url = null;
        if (location.startsWith("http://")) url = new URL(location); else {
            File f = new File(location);
            url = f.toURI().toURL();
        }
        URLConnection con = url.openConnection();
        return con.getLastModified();
    }
