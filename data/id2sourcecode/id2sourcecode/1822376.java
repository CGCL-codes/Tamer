        InputStream openStream() throws IOException {
            if (value instanceof FileItem) {
                return ((FileItem) value).getInputStream();
            } else if (value instanceof String) {
                return new ByteArrayInputStream(((String) value).getBytes());
            } else if (value instanceof byte[]) {
                return new ByteArrayInputStream((byte[]) value);
            } else if (value instanceof InputStream) {
                return (InputStream) value;
            } else if (value instanceof RenderedImage) {
                java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
                ImageIO.write((RenderedImage) value, "png", output);
                byte[] buff = output.toByteArray();
                return new ByteArrayInputStream(buff);
            } else if (value instanceof File) {
                return new FileInputStream((File) value);
            }
            return null;
        }
