    public static File createExternalJar(InputStream data, String nameWithoutJarSuffix) throws IOException, FileNotFoundException {
        File tempFile = File.createTempFile(nameWithoutJarSuffix, ".jar");
        tempFile.createNewFile();
        FileOutputStream stream = new FileOutputStream(tempFile);
        int i = -1;
        while ((i = data.read()) != -1) stream.write(i);
        stream.close();
        data.close();
        return tempFile;
    }
