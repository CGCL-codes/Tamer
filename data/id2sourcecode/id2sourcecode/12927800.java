    public void run() {
        try {
            byte buf[] = new byte[1000];
            while (!piperThread.isInterrupted()) {
                int readCount = in.read(buf);
                if (readCount == -1) {
                    notifyListener();
                    break;
                }
                if (readCount != 0) out.write(buf, 0, readCount);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
