    private static void copyLibraryToFS(File toLibTmpFile, InputStream fromLibInputStream) {
        FileOutputStream libTmpOutputStream = null;
        try {
            libTmpOutputStream = new FileOutputStream(toLibTmpFile);
            byte[] buffer = new byte[65536];
            while (true) {
                int read = fromLibInputStream.read(buffer);
                if (read > 0) {
                    libTmpOutputStream.write(buffer, 0, read);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing SevenZipJBinding native library: " + "can't copy native library out of a resource file to the temporary location: '" + toLibTmpFile.getAbsolutePath() + "'", e);
        } finally {
            try {
                fromLibInputStream.close();
            } catch (IOException e) {
            }
            try {
                if (fromLibInputStream != null) {
                    libTmpOutputStream.close();
                }
            } catch (IOException e) {
            }
        }
    }
