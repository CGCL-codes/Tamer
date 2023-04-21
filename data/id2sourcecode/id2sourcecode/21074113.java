    @Override
    public void run() {
        try {
            System.out.println("running");
            fc1 = new FileInputStream("j:\\neembuu\\virtual\\monitored.nbvfs\\test120k.http.rmvb").getChannel();
            fc1.position(readPosition);
            if (verify) {
                fc2 = new FileInputStream("j:\\neembuu\\realfiles\\test120k.rmvb").getChannel();
                fc2.position(readPosition);
            }
            for (int i = 1; i != readTimes; i++) {
                ByteBuffer fromVirtual_toCheck = ByteBuffer.allocateDirect(readCapacity);
                fc1.read(fromVirtual_toCheck);
                if (verify) {
                    ByteBuffer fromRealFile = ByteBuffer.allocateDirect(readCapacity);
                    fc2.read(fromRealFile);
                    BoundaryConditions.checkBuffers(fromVirtual_toCheck, fromRealFile);
                }
                if (speed > 0) {
                    System.out.println("sleeping=" + (readCapacity / (speed)));
                    Thread.sleep(readCapacity / (speed));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
