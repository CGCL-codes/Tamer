    public InputStream getStream() throws Exception {
        if (content == null) {
            if (storeLocation != null) {
                try {
                    return new FileInputStream(storeLocation);
                } catch (FileNotFoundException e) {
                    throw new Exception("FileItem: stored item was lost");
                }
            } else {
                content = byteStream.toByteArray();
                byteStream = null;
            }
        }
        return new ByteArrayInputStream(content);
    }
