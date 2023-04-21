    public static boolean saveZipAs(Context context, int ressound, String dir, String filename) {
        int len = 1024;
        byte[] buffer = new byte[1024];
        int size = 0;
        boolean exists = (new File(dir)).exists();
        if (!exists) {
            new File(dir).mkdirs();
        }
        exists = (new File(dir + "/" + filename)).exists();
        if (exists) {
            return true;
        }
        InputStream inputStream = null;
        FileOutputStream output = null;
        try {
            int StreamLen, readCount, readSum;
            inputStream = context.getResources().openRawResource(ressound);
            output = new FileOutputStream(dir + "/" + filename);
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            BufferedInputStream b = new BufferedInputStream(zipInputStream);
            StreamLen = (int) zipEntry.getSize();
            while ((readCount = b.read(buffer)) != -1) {
                output.write(buffer, 0, readCount);
            }
            inputStream.close();
            output.flush();
            output.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
