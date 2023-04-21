    public static URL saveToFile(InputStream inputStream, File file) throws IOException {
        new File(file.getParent()).mkdirs();
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(readInputStreamToByteArray(inputStream));
        fos.close();
        return file.toURI().toURL();
    }
