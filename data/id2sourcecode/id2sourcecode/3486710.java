            public void run() {
                int readCnt;
                final byte[] buf = new byte[65536];
                int cnt = 0;
                try {
                    while ((readCnt = inAIStreamPCM.read(buf, 0, buf.length)) != -1) {
                        outAOStreamWavPCM.write(buf, 0, readCnt);
                        cnt++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    fail("Exception pumping streams: " + e.getMessage());
                }
                isRunFinished = true;
            }
