    public void addFile(File file, String parentDir) throws IOException {
        byte[] buffer = new byte[BUF_SIZE];
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            String jarEntryName = null;
            if (parentDir != null && parentDir.length() > 0) {
                jarEntryName = parentDir + "/" + file.getName();
            } else {
                jarEntryName = file.getName();
            }
            getJarOutputStream().putNextEntry(new JarEntry(jarEntryName));
            for (int read = 0; read != -1; read = inputStream.read(buffer)) {
                getJarOutputStream().write(buffer, 0, read);
            }
            getJarOutputStream().closeEntry();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
