    public static void main(String[] args) throws FileNotFoundException, IOException {
        File f = new File("/opt/project/isee/archive/a1977/77295.arc");
        FileChannel fc = new FileInputStream(f).getChannel();
        ByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY, 0, 10000);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        VaxFloat vf = new VaxFloat(1, 880, 12, 10, 1, 1, 1, buf);
        System.err.println(vf.value(0));
        Int i = new Int(1, 880, 0, 10, 1, 1, 1, buf);
        System.err.println(i.value(0));
    }
