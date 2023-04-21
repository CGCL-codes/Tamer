    private static void extractEntry(ZipFile zipFile, ZipEntry zipEntry, String destDir) throws IOException {
        String fileName = zipEntry.getName();
        int idx;
        if ((idx = fileName.lastIndexOf("/")) != -1) {
            fileName = fileName.substring(idx + 1);
        }
        File file = new File(destDir, fileName);
        logger.info(file.getPath());
        new File(file.getParent()).mkdirs();
        InputStream is = null;
        OutputStream os = null;
        try {
            is = zipFile.getInputStream(zipEntry);
            os = new FileOutputStream(file);
            for (int len; (len = is.read(buffer)) != -1; ) os.write(buffer, 0, len);
        } finally {
            os.close();
            is.close();
        }
    }
