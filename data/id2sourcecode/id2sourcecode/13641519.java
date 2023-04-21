    public static File explodeJarFile(URL url, File directory, boolean localCopy, boolean createSubDirectory) throws IOException {
        ZipInputStream zipInputStream = null;
        try {
            if (localCopy) {
                String filename = FileUtils.getFileName(url);
                InputStream inputStream = null;
                FileOutputStream outputStream = null;
                File copyfile = new File(directory, filename);
                try {
                    inputStream = url.openStream();
                    outputStream = new FileOutputStream(copyfile);
                    StreamUtils.copyStream(inputStream, outputStream);
                } catch (IOException ex) {
                    StreamUtils.safeClose(outputStream);
                    StreamUtils.safeClose(inputStream);
                    throw ex;
                }
                zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(copyfile)));
            } else {
                InputStream inputStream = url.openStream();
                zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
            }
            if (createSubDirectory) {
                directory = new File(directory, FileUtils.removeFileExt(FileUtils.getFileName(url)));
                directory.mkdirs();
            }
            while (true) {
                ZipEntry zipEntry = zipInputStream.getNextEntry();
                if (zipEntry == null) {
                    break;
                }
                if (zipEntry.isDirectory()) {
                    File newDir = new File(directory, zipEntry.getName());
                    newDir.mkdirs();
                    if (!newDir.isDirectory()) {
                        throw new IOException("Unable to create the directory " + newDir.toString());
                    }
                    continue;
                }
                File file = new File(directory, zipEntry.getName());
                file.getParentFile().mkdirs();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                try {
                    StreamUtils.copyStream(zipInputStream, fileOutputStream);
                } finally {
                    StreamUtils.safeClose(fileOutputStream);
                }
            }
        } finally {
            StreamUtils.safeClose(zipInputStream);
        }
        return directory;
    }
