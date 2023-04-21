    private void unpackSettings() throws Exception {
        ZipInputStream zipInputStream = new ZipInputStream(getClass().getResourceAsStream("/settings.zip"));
        int BUFFER_SIZE = 4096;
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            File destFile = new File(baseDirectory, zipEntry.getName());
            destFile.getParentFile().mkdirs();
            if (!zipEntry.isDirectory()) {
                BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(destFile), BUFFER_SIZE);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = zipInputStream.read(buffer, 0, BUFFER_SIZE)) != -1) output.write(buffer, 0, length);
                output.close();
                zipInputStream.closeEntry();
            }
        }
        zipInputStream.close();
        settingsDirectory = new File(baseDirectory.toString() + File.separator + "settings");
    }
