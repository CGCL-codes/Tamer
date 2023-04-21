    protected void deleteFilePart(File file, long startpos, long length) throws IOException {
        RandomAccessFile ramFile = new RandomAccessFile(file, "rw");
        long oldlength = file.length();
        FileChannel channel1 = ramFile.getChannel();
        FileChannel channel2 = new FileInputStream(file).getChannel();
        channel2.position(startpos + length);
        channel1.transferFrom(channel2, startpos, oldlength - (length + startpos));
        channel2.close();
        channel1.truncate(oldlength - length);
        channel1.close();
    }
