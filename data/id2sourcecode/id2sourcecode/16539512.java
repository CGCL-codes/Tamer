    public static File copyFileFromZip(final ZipFile zip, final ZipEntry zipEntry, final File tmpPath) throws IOException {
        OutputStream os = null;
        InputStream is = null;
        try {
            int folderSep = zipEntry.getName().lastIndexOf("/");
            if (folderSep < 0) {
                folderSep = zipEntry.getName().lastIndexOf("\\");
            }
            if (folderSep > 0) {
                final File entryPath = new File(tmpPath.getAbsolutePath() + File.separatorChar + zipEntry.getName().substring(0, folderSep) + File.separatorChar);
                entryPath.mkdirs();
            }
            final File out = new File(tmpPath.getAbsolutePath() + File.separatorChar + zipEntry.getName());
            if (!out.isDirectory()) {
                os = new FileOutputStream(out);
                final byte[] entryBytes = new byte[(int) zipEntry.getSize()];
                is = zip.getInputStream(zipEntry);
                int readSize = 0;
                while ((readSize = is.read(entryBytes)) > 0) {
                    os.write(entryBytes, 0, readSize);
                }
                os.flush();
            }
            return out;
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }
