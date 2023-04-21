    void buildThreads() {
        reader = new Thread() {

            public void run() {
                while (readLoop()) ;
            }
        };
        writer = new Thread() {

            public void run() {
                while (writeLoop()) ;
            }
        };
        writer.start();
        reader.start();
    }
