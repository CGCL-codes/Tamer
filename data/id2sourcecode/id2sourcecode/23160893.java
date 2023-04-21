    public static void SaveIncludedZippedFileIntoFilesFolder(int resourceid, String filename, Context ApplicationContext) throws Exception {
        InputStream is = ApplicationContext.getResources().openRawResource(resourceid);
        FileOutputStream fos = ApplicationContext.openFileOutput(filename, Context.MODE_WORLD_READABLE);
        GZIPInputStream gzis = new GZIPInputStream(is);
        byte[] bytebuf = new byte[1024];
        int read;
        while ((read = gzis.read(bytebuf)) >= 0) {
            fos.write(bytebuf, 0, read);
        }
        gzis.close();
        fos.getChannel().force(true);
        fos.flush();
        fos.close();
    }
