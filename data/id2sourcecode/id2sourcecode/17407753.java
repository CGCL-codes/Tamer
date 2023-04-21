    public ByteArrayDataSource(InputStream is, String type) {
        this.type = type;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) os.write(ch);
            data = os.toByteArray();
        } catch (IOException ioex) {
        }
    }
