    @Override
    public void run() {
        byte[] buffer = null;
        while (active) {
            if (suspend) try {
                wait();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            buffer = new byte[bufferSize];
            try {
                int read = source.read(buffer);
                if (read < 0) active = false; else {
                    if (read > bufferSize) read = bufferSize;
                    this.write(buffer, 0, read);
                }
            } catch (IOException e) {
                active = false;
                try {
                    this.close();
                } catch (IOException e1) {
                }
                System.err.println(e.getLocalizedMessage());
            }
        }
    }
