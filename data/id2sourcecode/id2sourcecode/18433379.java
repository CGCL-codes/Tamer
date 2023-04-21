    public static int ZipDateSource(Context mCtx, int mDBRawResource) {
        int len = 1024;
        int readCount = 0, readSum = 0;
        byte[] buffer = new byte[len];
        int StreamLen = 0;
        InputStream inputStream;
        OutputStream output;
        try {
            inputStream = mCtx.getResources().openRawResource(mDBRawResource);
            output = new FileOutputStream(databaseFilename);
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            BufferedInputStream b = new BufferedInputStream(zipInputStream);
            StreamLen = (int) zipEntry.getSize();
            while ((readCount = b.read(buffer)) != -1) {
                output.write(buffer, 0, readCount);
                readSum = readSum + readCount;
            }
            output.flush();
            output.close();
            inputStream.close();
        } catch (IOException e) {
            Log.i("zip io", e.toString());
        }
        return readSum;
    }
