    public void writeInputStream(InputStream is, String extension) {
        try {
            FileOutputStream fos = new FileOutputStream("c:/java/debug/file" + RandomStringUtils.randomAlphabetic(3) + extension);
            byte[] buf = new byte[256];
            int read = 0;
            while ((read = is.read(buf)) > 0) {
                fos.write(buf, 0, read);
            }
            fos.flush();
            fos.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
