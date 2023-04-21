    public static void saveDescr(String descr, File descrFile) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(descrFile);
            FileChannel fc = fos.getChannel();
            fc.write(Charset.forName("UTF-8").encode(descr));
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }
