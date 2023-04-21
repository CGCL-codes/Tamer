    @Override
    protected String createInstance() throws IOException {
        InputStream input = this.resource.getInputStream();
        try {
            InputStreamReader reader = new InputStreamReader(input, this.charset);
            StringWriter writer = new StringWriter();
            char[] buf = new char[4096];
            int r;
            while ((r = reader.read(buf)) != -1) writer.write(buf, 0, r);
            return writer.toString();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
            }
        }
    }
