    private ByteArrayOutputStream getBytes(String filename) throws FileNotFoundException {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(filename);
        if (inputStream == null) throw new FileNotFoundException("File " + filename + " not found.");
        byte[] bytes = new byte[1024];
        int read;
        try {
            while ((read = inputStream.read(bytes)) > -1) byteOutput.write(bytes, 0, read);
        } catch (Exception e) {
            log.error(e);
        }
        return byteOutput;
    }
