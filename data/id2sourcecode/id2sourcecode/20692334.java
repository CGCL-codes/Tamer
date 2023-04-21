    private static void rewriteFile(IPath path) throws IOException {
        File infile = new File(path.toOSString());
        File outfile = new File(path.toOSString());
        RandomAccessFile inraf = new RandomAccessFile(infile, "r");
        RandomAccessFile outraf = new RandomAccessFile(outfile, "rw");
        FileChannel finc = inraf.getChannel();
        FileChannel foutc = outraf.getChannel();
        MappedByteBuffer inmbb = finc.map(FileChannel.MapMode.READ_ONLY, 0, (int) infile.length());
        Charset inCharset = Charset.forName(WRONG_ENCODING);
        Charset outCharset = Charset.forName(RIGHT_ENCODING);
        CharsetDecoder inDecoder = inCharset.newDecoder();
        CharsetEncoder outEncoder = outCharset.newEncoder();
        CharBuffer cb = inDecoder.decode(inmbb);
        ByteBuffer outbb = outEncoder.encode(cb);
        foutc.write(outbb);
        inraf.close();
        outraf.close();
    }
