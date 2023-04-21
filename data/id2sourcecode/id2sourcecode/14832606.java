    public static void writeFileToStream(OutputStream outStream, File inputFile, boolean closeOutputStream) throws IOException {
        FileChannel srcChannel = new FileInputStream(inputFile).getChannel();
        WritableByteChannel dstChannel = Channels.newChannel(outStream);
        srcChannel.transferTo(0, srcChannel.size(), dstChannel);
        srcChannel.close();
        if (closeOutputStream) dstChannel.close();
    }
