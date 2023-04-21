    private byte[] getData(ZipEntry entry, ZipFile zipFile) {
        InputStream in = null;
        byte[] data = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            in = zipFile.getInputStream(entry);
            int read = 0;
            while ((read = in.read()) != -1) {
                baos.write((byte) read);
            }
            data = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
        return data;
    }
