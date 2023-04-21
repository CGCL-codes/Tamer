    private static byte[] loadLocalResource(String name) {
        URL url = MogwaiRootImplementation.class.getClassLoader().getResource("resources/" + name);
        if (url != null) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                BufferedInputStream bis = new BufferedInputStream(url.openStream());
                byte[] data = new byte[5000];
                while (bis.available() > 0) {
                    int anz = bis.read(data);
                    if (anz > 0) bos.write(data, 0, anz);
                }
                bis.close();
                return bos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
