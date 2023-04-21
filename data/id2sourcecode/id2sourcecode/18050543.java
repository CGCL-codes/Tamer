    public ImageIcon getIcon(String name) {
        baos.reset();
        InputStream is = ImageIconLoader.class.getResourceAsStream(PATH + name);
        byte[] tmpBuf = new byte[4096];
        int readed = 0;
        if (is != null) {
            try {
                while ((readed = is.read(tmpBuf, 0, 4096)) > 0) {
                    baos.write(tmpBuf, 0, readed);
                }
                is.close();
            } catch (IOException ex) {
                return null;
            } finally {
                try {
                    is.close();
                } catch (IOException ex) {
                }
            }
            buffer = baos.toByteArray();
            ImageIcon icon = new ImageIcon(buffer);
            return icon;
        }
        return null;
    }
