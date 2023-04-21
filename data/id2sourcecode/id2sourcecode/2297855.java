    private void addFile(File file, File parentDir, JarOutputStream jarOut) throws IOException {
        byte[] buffer = new byte[1024];
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            String jarEntryName = parentDir.getName() + "/" + file.getName();
            jarOut.putNextEntry(new JarEntry(jarEntryName));
            for (int read = 0; read != -1; read = inputStream.read(buffer)) jarOut.write(buffer, 0, read);
            jarOut.closeEntry();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
