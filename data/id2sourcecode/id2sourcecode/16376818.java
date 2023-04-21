    private ImageIcon loadImage(InputStream resourceIn) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read = -1;
        while ((read = resourceIn.read(buffer)) >= 0) {
            out.write(buffer, 0, read);
        }
        ImageIcon icon = new ImageIcon(out.toByteArray());
        resourceIn.close();
        return icon;
    }
