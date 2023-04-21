    @Override
    public void parseInputSource(InputSource source) throws IOException, SAXException {
        InputStream is = source.getByteStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int read = 0;
        while (read > -1) {
            read = is.read();
            if (read != -1) {
                byteArrayOutputStream.write(read);
            }
        }
        parseString(new String(byteArrayOutputStream.toByteArray()));
    }
