    private static void unzip(File srcFile, File dstDir) throws IOException {
        byte[] buffer = new byte[ZIP_BUFFER_SIZE];
        int bytes;
        ZipFile zipFile = new ZipFile(srcFile);
        Enumeration entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            if (entry.isDirectory()) {
                File newDirectory = new File(entry.getName());
                if (!newDirectory.exists()) newDirectory.mkdirs();
                continue;
            }
            File newFile = new File(dstDir, entry.getName());
            File newFileDir = newFile.getParentFile();
            if (!newFileDir.exists()) newFileDir.mkdirs();
            InputStream in = zipFile.getInputStream(entry);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(newFile));
            while ((bytes = in.read(buffer)) >= 0) out.write(buffer, 0, bytes);
            in.close();
            out.close();
        }
        zipFile.close();
    }
