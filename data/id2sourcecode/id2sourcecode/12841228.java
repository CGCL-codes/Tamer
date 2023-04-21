    public void run() {
        try {
            while (true) {
                try {
                    while (!finishing && in.available() == 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new Interrupted(e);
                        }
                    }
                    if (finishing) {
                        return;
                    }
                } catch (IOException e) {
                    if (in instanceof BufferedInputStream && "Stream closed".equals(e.getMessage())) {
                        return;
                    }
                }
                out.write(in.read());
                out.flush();
            }
        } catch (IOException e) {
            exception = e;
        }
    }
