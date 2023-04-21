    public static void writeSprite(OutputStream output, InputStream xmlInput, InputStream imageInput, String imageFormat) throws ResourceException {
        try {
            ObjectOutputStream data = new ObjectOutputStream(output);
            ByteArrayOutputStream xmlByteOutput = new ByteArrayOutputStream();
            while (xmlInput.available() > 0) xmlByteOutput.write(xmlInput.read());
            data.writeObject(xmlByteOutput.toByteArray());
            ByteArrayOutputStream imageByteOutput = new ByteArrayOutputStream();
            while (imageInput.available() > 0) imageByteOutput.write(imageInput.read());
            data.writeObject(imageByteOutput.toByteArray());
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }
