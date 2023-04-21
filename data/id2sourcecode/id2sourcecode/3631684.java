    public static void unZipFile(File inputFile, File outputDirectory) throws IOException {
        logger.debug("unZipFile(in:" + inputFile.toString() + ", out:" + outputDirectory.toString() + ")");
        ZipFile zipFile = new ZipFile(inputFile);
        Enumeration zipEntries = zipFile.entries();
        while (zipEntries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement();
            logger.debug("Unpacking: " + zipEntry.getName());
            File file = new File(outputDirectory, zipEntry.getName());
            if (zipEntry.isDirectory()) {
                file.mkdirs();
            } else {
                InputStream inputStream = zipFile.getInputStream(zipEntry);
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                File dir = new File(file.getParent());
                dir.mkdirs();
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int readByte;
                while ((readByte = bis.read()) != -1) {
                    bos.write((byte) readByte);
                }
                bos.close();
                fos.close();
            }
            logger.debug(zipEntry.getName() + " : Unpacked.");
        }
    }
