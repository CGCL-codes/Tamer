    public void run() {
        byte[] buf = new byte[1024];
        int bytes_read;
        try {
            while ((bytes_read = in.read(buf)) > 0) {
                System.out.write(buf, 0, bytes_read);
                System.out.flush();
            }
        } catch (IOException io_ex) {
            io_ex.printStackTrace();
        }
    }
