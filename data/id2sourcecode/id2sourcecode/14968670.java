    @Override
    public void run() {
        try {
            SMQOMChannel channel = controller.getChannel("TESTCHANNEL");
            SMQOMQueue test1Out = channel.getQueue("TEST1.OUT");
            SMQOMQueue test2Out = channel.getQueue("TEST2.OUT");
            int test1OutSent = 0;
            int test2OutSent = 0;
            for (int i = 0; i < 600; ++i) {
                Message m1, m2;
                m1 = new Message(10000 + i, 10000 + i, "TEST2.IN", "System1", "TEST1.IN", new String("Test Message id" + (10000 + i)).getBytes(Charset.forName("UTF-8")));
                m2 = new Message(20000 + i, 20000 + i, "TEST1.IN", "System1", "TEST2.IN", new String("Test Message id" + (20000 + i)).getBytes(Charset.forName("UTF-8")));
                if (test1Out.offer(m1, 3000, TimeUnit.MILLISECONDS)) {
                    test1OutSent++;
                }
                if (test2Out.offer(m2, 3000, TimeUnit.MILLISECONDS)) {
                    test2OutSent++;
                }
            }
            System.out.println("\n\tSent to TEST1.OUT: " + test1OutSent + "\n\tSent to TEST2.OUT: " + test2OutSent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
