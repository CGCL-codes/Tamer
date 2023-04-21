    public static void createZipFile(String zipFileName, File[] files, boolean includePathInfo) throws Exception {
        File zipFile = new File(zipFileName);
        checkFileWritability(zipFile, "zip File '" + zipFile + "'", true);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
        for (File file : files) {
            if (includePathInfo) out.putNextEntry(new ZipEntry(file.getPath())); else out.putNextEntry(new ZipEntry(file.getName()));
            copyFileToStream(file, out);
            out.closeEntry();
        }
        out.close();
    }
