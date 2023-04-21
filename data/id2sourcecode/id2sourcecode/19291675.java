    public static LocalMovies FromLocalXML(File file) {
        int read;
        byte[] buffer = new byte[512 * 1024];
        FileInputStream fileInputStream;
        ByteArrayOutputStream byteOutputStream;
        try {
            byteOutputStream = new ByteArrayOutputStream();
            fileInputStream = new FileInputStream(file);
            while ((read = fileInputStream.read(buffer)) > 0) {
                byteOutputStream.write(buffer, 0, read);
            }
            fileInputStream.close();
            return FromLocalXML(new String(byteOutputStream.toByteArray()));
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
