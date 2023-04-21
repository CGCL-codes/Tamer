    public void test() throws IOException {
        fin2 = new FileInputStream(f);
        channel2 = fin2.getChannel();
        bb = new BigByteBuffer2(channel2, FileChannel.MapMode.READ_ONLY, 1024 * 8);
        MyThread th = new MyThread("T1:");
        th.start();
        MyThread th2 = new MyThread("T2: ");
        th2.start();
        System.out.println("Fin de la prueba. " + numPruebas + " iteraciones.");
    }
