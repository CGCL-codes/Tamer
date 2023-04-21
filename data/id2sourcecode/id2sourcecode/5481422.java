            public void run() {
                super.run();
                while (true) {
                    receiver.update();
                    try {
                        Thread.sleep(writeWaitTimeCopy);
                    } catch (InterruptedException e) {
                    }
                }
            }
