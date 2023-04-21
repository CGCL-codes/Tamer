    public static byte[] asBytes(URL url) {
        byte[] data = cache.get(url);
        if (data == null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedInputStream bis = new BufferedInputStream(url.openStream());
                data = new byte[8192];
                int count;
                while ((count = bis.read(data)) != -1) {
                    baos.write(data, 0, count);
                }
                bis.close();
                data = baos.toByteArray();
                cache.put(url, data);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return data;
    }
