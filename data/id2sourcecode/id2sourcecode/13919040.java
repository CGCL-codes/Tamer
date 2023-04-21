            @Override
            public void run() {
                try {
                    long startNanos = System.nanoTime();
                    while (true) {
                        int read = convertedAis.read(buf);
                        sdl.write(buf, 0, read);
                        long waitNanos = ((read / format.getFrameSize())) * 1000000000L / (long) format.getFrameRate();
                        while (System.nanoTime() - startNanos < waitNanos) {
                            if (waitNanos - (System.nanoTime() - startNanos) > 1000000L) Thread.sleep(0, 500000);
                        }
                        startNanos += waitNanos;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
