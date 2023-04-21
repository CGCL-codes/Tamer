    public static void main(String[] args) throws IOException {
        FileOutputStream out = new FileOutputStream("out.dat");
        FileChannel ch = out.getChannel();
        DoubleBuffer db = bb.asDoubleBuffer();
        double t = 0.0;
        for (int i = 0; i < 10000; i++, t += 0.001) {
            put(db, ch, t);
            put(db, ch, 10 * Math.sin(2 * Math.PI / 10 * t));
        }
        out.close();
    }
