            public void run() {
                while (true) {
                    try {
                        int read = aes.read();
                        if (read == -1) {
                            return;
                        }
                        bes.write(read);
                        bes.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
