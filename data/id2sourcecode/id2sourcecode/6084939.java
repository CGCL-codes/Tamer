    private static void extractZipEntry(ZipEntry zipentry, String s) throws IOException, FileNotFoundException {
        File file;
        if (!(file = new File(s)).getParentFile().exists()) file.getParentFile().mkdirs();
        if (zipentry.isDirectory()) file.mkdir();
        if (!file.exists()) file.createNewFile();
        InputStream inputstream = zipFile.getInputStream(zipentry);
        byte buffer[] = new byte[2048];
        if (!file.isDirectory()) {
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            for (int i = 0; (i = inputstream.read(buffer)) != -1; ) fileoutputstream.write(buffer, 0, i);
            fileoutputstream.flush();
            fileoutputstream.close();
            inputstream.close();
        }
    }
