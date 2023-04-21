    @Test
    public final void analyzeSimplestRequest() throws Exception {
        FileChannel fc = new FileInputStream("j:\\neembuu\\realfiles\\test120k.rmvb").getChannel();
        ByteBuffer actualContent = ByteBuffer.allocate(1024);
        fc.position(0);
        fc.read(actualContent);
        fc.close();
        FileChannel fcVirtual = null;
        int trial = 0;
        while (fcVirtual == null) {
            try {
                fcVirtual = new FileInputStream("j:\\neembuu\\virtual\\monitored.nbvfs\\test120k.http.rmvb").getChannel();
            } catch (Exception e) {
                System.out.println("trial=" + trial);
                trial++;
                Thread.sleep(1000);
            }
        }
        ByteBuffer virtualContent = ByteBuffer.allocate(1024);
        fcVirtual.position(0);
        fcVirtual.read(virtualContent);
        fcVirtual.close();
        virtualContent.equals(actualContent);
    }
