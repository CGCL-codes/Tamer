    public static void copyFile(File inFile, File outFile) throws IOException {
        ReadableByteChannel inChannel = new FileInputStream(inFile).getChannel();
        WritableByteChannel outChannel = new FileOutputStream(outFile).getChannel();
        copyChannel(inChannel, outChannel);
        inChannel.close();
        outChannel.close();
    }
