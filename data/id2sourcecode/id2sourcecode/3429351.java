    protected MultipartElement createTextMultipartElement(String encoding) throws IOException {
        MultipartElement element;
        int read = 0;
        byte[] buffer = new byte[TEXT_BUFFER_SIZE];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((read = this.inputStream.read(buffer, 0, TEXT_BUFFER_SIZE)) > 0) {
            baos.write(buffer, 0, read);
        }
        String value = baos.toString(encoding);
        element = new MultipartElement(this.inputStream.getElementName(), value);
        return element;
    }
