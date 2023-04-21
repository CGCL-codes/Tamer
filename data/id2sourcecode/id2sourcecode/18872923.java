    public static File streamToFile(String fileName, InputStream input) throws IOException {
        File F = new File(outputDir + File.separator + fileName);
        FileOutputStream output = new FileOutputStream(F);
        int read;
        byte[] buffer = new byte[1024];
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }
        output.close();
        input.close();
        return F;
    }
