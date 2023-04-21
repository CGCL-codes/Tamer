    public static void unzip(File fileFrom, File fileTo) throws Exception {
        Enumeration entries;
        ZipFile zipFile;
        zipFile = new ZipFile(fileFrom);
        entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            InputStream in = zipFile.getInputStream(entry);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileTo));
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
            in.close();
            out.close();
        }
        zipFile.close();
    }
