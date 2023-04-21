    public void initFilesystems() throws IOException {
        String descriptor;
        Enumeration<URL> enm;
        URL url;
        InputStream src;
        Buffer buffer;
        String content;
        descriptor = "META-INF/de/ui/sushi/filesystems";
        buffer = new Buffer();
        enm = getClass().getClassLoader().getResources(descriptor);
        while (enm.hasMoreElements()) {
            url = enm.nextElement();
            src = url.openStream();
            content = buffer.readString(src, Settings.UTF_8);
            for (String line : Strings.split("\n", content)) {
                line = line.trim();
                if (line.length() > 0) {
                    initFilesystem(Strings.split(" ", line.trim()));
                }
            }
            src.close();
        }
    }
