        @Override
        public void run() {
            final long start = System.currentTimeMillis();
            try {
                System.out.println("Starting sending:" + start);
                final FileInputStream fis = new FileInputStream(SendThread.this.aktFile);
                final FileChannel fc = fis.getChannel();
                final ByteBuffer bb = ByteBuffer.allocate(4096);
                final SocketChannel sc = SocketChannel.open(new InetSocketAddress(SendThread.this.ip, Main.fileReceivePort));
                final long middle = System.currentTimeMillis();
                int i = 0;
                while (fc.read(bb) != -1) {
                    i++;
                    bb.flip();
                    sc.write(bb);
                    bb.compact();
                }
                System.out.println("Send: " + i);
                fc.close();
                bb.clear();
                sc.close();
                final long end = System.currentTimeMillis();
                System.out.println("Preparation: " + (middle - start));
                System.out.println("Reaing/Sending: " + (end - middle));
                Connection.getConnection(false).getChatSession(SendThread.this.ip, false).sendFileFinished(true);
            } catch (final IOException e) {
                e.printStackTrace();
                Connection.getConnection(false).getChatSession(SendThread.this.ip, false).sendFileFinished(false);
            } finally {
                SendThread.this.aktFile = null;
                System.gc();
            }
        }
