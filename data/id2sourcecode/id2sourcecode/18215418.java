    @Override
    public void run() {
        try {
            char[] cbuf = new char[BUFFER_SIZE];
            int count;
            while ((count = in.read(cbuf, 0, BUFFER_SIZE)) >= 0) out.write(cbuf, 0, count);
            out.flush();
        } catch (IOException e) {
            System.err.println("StreamRedirecter: " + e);
        }
    }
