    private static void prueba1(File f, int numPruebas) throws FileNotFoundException, IOException {
        FileInputStream fin;
        fin = new FileInputStream(f);
        FileChannel channel = fin.getChannel();
        int size = (int) channel.size();
        ByteBuffer bbCorrect = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
        Random rnd = new Random();
        long t1 = System.currentTimeMillis();
        FileInputStream fin2 = new FileInputStream(f);
        FileChannel channel2 = fin2.getChannel();
        BigByteBuffer bb = new BigByteBuffer(channel2, FileChannel.MapMode.READ_ONLY, 1024 * 1024);
        for (int i = 0; i < numPruebas; i++) {
            int pos = rnd.nextInt(size - 10);
            byte bCorrect = bbCorrect.get(pos);
            byte bPrueba = bb.get(pos);
            if (bCorrect != bPrueba) {
                System.err.println("Error de lectura. " + bCorrect + " " + bPrueba);
            } else {
            }
        }
        close(channel2, fin2, bb);
        System.gc();
        long t2 = System.currentTimeMillis();
        System.out.println("T=" + (t2 - t1) + "mseconds");
    }
