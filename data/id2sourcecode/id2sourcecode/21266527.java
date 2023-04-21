    private static void unzip(byte[] buffer, File directory, ZipInputStream zis, ZipEntry entry) throws FileNotFoundException, IOException {
        int count;
        FileOutputStream fos = new FileOutputStream(touch(joinPath(directory.getAbsolutePath(), entry.getName())));
        BufferedOutputStream dest = new BufferedOutputStream(fos, buffer.length);
        while ((count = zis.read(buffer, 0, buffer.length)) != -1) dest.write(buffer, 0, count);
        dest.close();
    }
