        public static void perform(Socket connection) throws IOException {
            final DataInputStream in = new DataInputStream(connection.getInputStream());
            final OutputStream out = connection.getOutputStream();
            final int io_error = 1;
            final int hk_error = 2;
            final int done = 4;
            Thread read = new Thread(new Runnable() {

                public void run() {
                    try {
                        synchronized (monitor) {
                            byte[] data = new byte[STR.getBytes().length];
                            in.readFully(data);
                            if (!new String(data).equals(STR)) {
                                status |= hk_error;
                            }
                            monitor.notifyAll();
                            status |= done;
                        }
                    } catch (IOException e) {
                        status |= io_error;
                        e.printStackTrace();
                    }
                }
            });
            Thread write = new Thread(new Runnable() {

                public void run() {
                    try {
                        synchronized (monitor) {
                            out.write(STR.getBytes());
                            out.flush();
                            monitor.notifyAll();
                            status |= done;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        status |= io_error;
                    }
                }
            });
            read.start();
            write.start();
            synchronized (monitor) {
                while ((status & done) == 1) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if ((status & hk_error) == hk_error) {
                    throw new IOException("JDWP-Handshake failed because of invalid string.");
                } else if ((status & io_error) == io_error) {
                    throw new IOException("JDWP-Handshake failed because of io errors.");
                }
            }
        }
