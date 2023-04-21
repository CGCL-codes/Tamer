    public static FloatBuffer readFileIntoBuffer(File file) throws FileNotFoundException, IOException {
        long s = file.length();
        System.out.println("Volume file size = " + Long.toString(s));
        FileChannel in = new FileInputStream(file).getChannel();
        ByteBuffer buffa = BufferUtil.newByteBuffer((int) s);
        in.read(buffa);
        buffa.rewind();
        FloatBuffer ret = buffa.asFloatBuffer();
        normalizeBuffer(ret);
        return ret;
    }
