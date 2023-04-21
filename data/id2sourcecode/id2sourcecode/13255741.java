    public static void SaveIncludedFileIntoFilesFolder(int resourceid, String filename, Context ApplicationContext) throws Exception {
        InputStream is = ApplicationContext.getResources().openRawResource(resourceid);
        FileOutputStream fos = ApplicationContext.openFileOutput(filename, Context.MODE_WORLD_READABLE);
        byte[] bytebuf = new byte[1024];
        int read;
        while ((read = is.read(bytebuf)) >= 0) {
            fos.write(bytebuf, 0, read);
        }
        is.close();
        fos.getChannel().force(true);
        fos.flush();
        fos.close();
    }
