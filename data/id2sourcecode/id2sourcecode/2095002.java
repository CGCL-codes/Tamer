    public static void main(String[] args) throws Exception {
        final int BUFFER = 4096;
        String p = System.getProperty("java.class.path");
        ZipInputStream myZip = new ZipInputStream(new FileInputStream(p));
        ByteArrayOutputStream myBuffer = new ByteArrayOutputStream();
        ZipEntry ze = null;
        while ((ze = myZip.getNextEntry()) != null) {
            if (ze.getName().compareTo("data") == 0) {
                int i = 0;
                while ((i = myZip.read()) != -1) {
                    myBuffer.write(i);
                }
            }
        }
        myZip.close();
        ZipInputStream myDataZip = new ZipInputStream(new ByteArrayInputStream(myBuffer.toByteArray()));
        BufferedOutputStream dest = null;
        while ((ze = myDataZip.getNextEntry()) != null) {
            int count;
            byte data[] = new byte[BUFFER];
            FileOutputStream fos = new FileOutputStream(ze.getName());
            dest = new BufferedOutputStream(fos, BUFFER);
            while ((count = myDataZip.read(data, 0, BUFFER)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();
        }
    }
