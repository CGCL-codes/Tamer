    public void setBase64Content(InputStream content, String contentType) throws SOAPException {
        if (content == null) {
            throw new SOAPException("Content is null");
        }
        OutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        try {
            while ((read = content.read(buffer, 0, buffer.length)) > 0) {
                outputStream.write(buffer, 0, read);
            }
            String contentString = outputStream.toString();
            if (Base64.isValidBase64Encoding(contentString)) {
                setContent(Base64.decode(contentString), contentType);
            } else {
                throw new SOAPException("Not a valid Base64 encoding");
            }
        } catch (IOException ex) {
            throw new SOAPException(ex);
        }
    }
