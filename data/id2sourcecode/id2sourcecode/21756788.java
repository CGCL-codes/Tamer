    private static boolean extractFileFromZip(ZipInputStream zipReader, ZipEntry zipEntry, final byte[] readBuffer, String destinationPath) throws IOException {
        File file = new File(destinationPath + File.separatorChar + zipEntry.getName());
        if (file.exists()) {
            if (!file.isFile()) {
                return false;
            }
        } else if (!file.createNewFile()) {
            if (!file.delete() || !file.createNewFile()) {
                return false;
            }
        }
        OutputStream fileOutputStream = null;
        try {
            fileOutputStream = Channels.newOutputStream(new FileOutputStream(file).getChannel());
            int readBytes;
            while ((readBytes = zipReader.read(readBuffer)) > 0) {
                fileOutputStream.write(readBuffer, 0, readBytes);
                fileOutputStream.flush();
            }
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
        return true;
    }
