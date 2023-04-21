    private static void transferFile(String url, String toFilename) throws IOException {
        URL fileUrl = new URL(url);
        ReadableByteChannel rbc2 = Channels.newChannel(fileUrl.openStream());
        FileOutputStream fos2 = new FileOutputStream(toFilename);
        fos2.getChannel().transferFrom(rbc2, 0, 1 << 24);
        fos2.close();
    }
