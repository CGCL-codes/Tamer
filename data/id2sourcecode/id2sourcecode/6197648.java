    byte[] fileToByteArray(File file) throws FileNotFoundException, IOException {
        byte[] buffer = new byte[(int) file.length()];
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
        input.read(buffer, 0, buffer.length);
        input.close();
        return buffer;
    }
