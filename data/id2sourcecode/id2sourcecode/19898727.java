            public void run() {
                try {
                    Thread.sleep(4000);
                    synchronized (GuaranteedDeliveryTest.this) {
                        coos.getTransport("TCP1").stop();
                        coos.getChannel("out").disconnect();
                        System.out.println("Stopping transport. Rounds up tp now: " + testrounds);
                        coos3 = COOSFactory.createCOOS(this.getClass().getResourceAsStream("/coos3GD.xml"), null);
                        coos3.getRouter().setLoggingEnabled(true);
                        coos.getRouter().setLoggingEnabled(true);
                        coos2.getRouter().setLoggingEnabled(true);
                        coos3.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Coos3 started");
            }
