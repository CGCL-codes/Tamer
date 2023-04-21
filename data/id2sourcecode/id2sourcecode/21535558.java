    public ByteArrayDataSource(InputStream is, String type, String name) {
        this.type = type;
        this.name = name;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) os.write(ch);
            data = os.toByteArray();
        } catch (IOException ioex) {
        }
    }
