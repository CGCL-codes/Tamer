            public void run() {
                while (true) {
                    try {
                        int read = ais.read();
                        if (read == -1) {
                            return;
                        }
                        bos.write(read);
                        bos.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
