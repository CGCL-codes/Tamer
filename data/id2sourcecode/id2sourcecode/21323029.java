    public static Image loadImage(InputStream imageStream) {
        byte[] buffer = new byte[1024];
        try {
            BufferedInputStream in = new BufferedInputStream(imageStream);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            int n;
            while ((n = in.read(buffer)) > 0) out.write(buffer, 0, n);
            in.close();
            out.flush();
            buffer = out.toByteArray();
            if (buffer.length == 0) {
                System.err.println("warning: image is zero-length");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Image image = Toolkit.getDefaultToolkit().createImage(buffer);
        if (image == null) return null;
        loadImage(image);
        return image;
    }
