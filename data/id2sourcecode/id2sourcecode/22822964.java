    public CarpClobImpl(Reader reader) {
        if (reader == null) return;
        this.length = 0;
        java.io.StringWriter writer = new StringWriter();
        char[] c = new char[2048];
        try {
            for (int len = -1; (len = reader.read(c, 0, 2048)) != -1; ) {
                this.length += len;
                writer.write(c, 0, len);
            }
            this.reader = new StringReader(writer.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
