    public void open(String name, int mode, boolean timeouts) throws IOException {
        if (name.charAt(0) != '/' || name.charAt(1) != '/') {
            throw new IllegalArgumentException("Protocol must start with \"//\" " + name);
        }
        name = name.substring(2);
        URL url = new URL(name);
        conn = url.openConnection();
        conn.connect();
    }
