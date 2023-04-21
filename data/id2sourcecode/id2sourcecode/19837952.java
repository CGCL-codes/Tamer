        public void run() {
            try {
                Thread.currentThread().setName("Writer for client " + ServeHTTPClient.this.thread.getName());
                while (alive) {
                    synchronized (this) {
                        if (writeValues.size() == 0) {
                            wait();
                        }
                        if (!alive) return;
                        for (int i = 0; i < writeValues.size(); ++i) {
                            HeaderAndData clientData = (HeaderAndData) writeValues.elementAt(i);
                            byte[] data = new byte[1024];
                            InputStream in = new StringBufferInputStream(clientData.header);
                            for (int read = in.read(data); read != -1; read = in.read(data)) {
                                clientStream.write(data, 0, read);
                            }
                            in = clientData.data;
                            for (int read = in.read(data); read != -1; read = in.read(data)) {
                                clientStream.write(data, 0, read);
                            }
                        }
                        clientStream.flush();
                        writeValues.removeAllElements();
                    }
                }
            } catch (ThreadDeath td) {
                System.out.println("ThreadDeath in gateway read " + td);
                throw td;
            } catch (Throwable t) {
                System.out.println("Exception in gateway read " + t);
            } finally {
                if (debug) System.out.println("ClientWriter is exiting.");
                Thread.currentThread().setName("Pooled Thread");
            }
        }
