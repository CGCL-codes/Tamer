            public void run() {
                byte[] bytes = new byte[1024];
                try {
                    bin = new BufferedInputStream(target.getInputStream());
                    while (true) {
                        int read = bin.read(bytes);
                        if (read == -1) {
                            System.out.println("BytesRead " + read);
                            break;
                        }
                        printBytes("FROM: Gateway Target", bytes, read);
                        clientStream.write(bytes, 0, read);
                        clientStream.flush();
                    }
                } catch (Throwable t) {
                    System.out.println("Exception in gateway read " + t);
                }
            }
