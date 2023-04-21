            public void run() {
                while (true) {
                    try {
                        int read = bis.read();
                        if (read == -1) {
                            return;
                        }
                        if (read == CTRL_SIGNAL) {
                            ctrlSigRecvTime = System.currentTimeMillis();
                            continue;
                        }
                        aos.write(read);
                        aos.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
