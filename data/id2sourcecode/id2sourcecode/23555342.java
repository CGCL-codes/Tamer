    public static void saveInputStreamAsFile(InputStream is, File file) {
        byte[] buf = new byte[1024];
        try {
            FileOutputStream fos = new FileOutputStream(file);
            int read = 0;
            while ((read = is.read(buf)) != -1) {
                fos.write(buf, 0, read);
            }
            fos.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
