    private void prueba2(String name, File f, int numPruebas) throws Exception {
        FileInputStream fin;
        fin = new FileInputStream(f);
        FileChannel channel = fin.getChannel();
        int size = (int) channel.size();
        ByteBuffer bbCorrect = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
        Random rnd = new Random();
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < numPruebas; i++) {
            int pos = rnd.nextInt(size - 10);
            bbCorrect.position(pos);
            int bCorrect = bbCorrect.getInt();
            int bPrueba = bb.getInt(pos);
            if (bCorrect != bPrueba) {
                System.err.println(name + "Error de lectura. " + bCorrect + " " + bPrueba);
                throw new Exception("Error con pos=" + pos);
            } else {
                System.out.println(name + "Correcto: pos=" + pos + " byte= " + bPrueba);
            }
        }
        close(channel2, fin2, bb);
        long t2 = System.currentTimeMillis();
        System.out.println("T=" + (t2 - t1) + "mseconds");
    }
