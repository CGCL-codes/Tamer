    private final void init() {
        InputStream in = null;
        try {
            URL url = this.getClass().getClassLoader().getResource("padawan.conf.xml");
            if (url != null) {
                URLConnection conn = url.openConnection();
                in = conn.getInputStream();
                long newModified = conn.getLastModified();
                if ((lastModified != newModified) && (newModified != 0)) {
                    lastModified = newModified;
                    Document xmlDoc = CDocumentBuilderFactory.newParser().newDocumentBuilder().parse(in);
                    parseDocument(xmlDoc);
                }
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
    }
