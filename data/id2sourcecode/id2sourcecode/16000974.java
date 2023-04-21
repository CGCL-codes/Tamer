    public static void unZipDir(File zipFile, File destinationPath) throws IOException {
        ZipFile zip = new ZipFile(zipFile);
        Enumeration entries = zip.entries();
        File currentDirectory = destinationPath;
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            if (entry.isDirectory()) {
                currentDirectory = new File(currentDirectory, entry.getName());
                if (!currentDirectory.mkdir()) {
                    throw new IOException("Failed creating zipfile component: " + currentDirectory.getAbsolutePath());
                }
                continue;
            }
            byte[] buffer = new byte[2156];
            int len;
            InputStream in = zip.getInputStream(entry);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(currentDirectory, entry.getName())));
            while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
            in.close();
            out.close();
        }
    }
