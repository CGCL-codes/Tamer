            @Override
            public void run() {
                ServerSocket ss;
                try {
                    final long start = System.currentTimeMillis();
                    ss = new ServerSocket(1440);
                    final FileChannel fc = new FileOutputStream(new File("D:/Daniel/Eigene Dateien/test")).getChannel();
                    final Socket s = ss.accept();
                    final SocketChannel sc = s.getChannel();
                    final ByteBuffer bb = ByteBuffer.allocate(4096);
                    final long middle = System.currentTimeMillis();
                    while (sc.read(bb) != -1) {
                        bb.flip();
                        fc.write(bb);
                        bb.compact();
                    }
                    fc.close();
                    sc.close();
                    final long end = System.currentTimeMillis();
                    System.out.println("Vorbereitung: " + (middle - start));
                    System.out.println("Lesen/Senden: " + (end - middle));
                    System.out.println("Gesamt: " + (end - start));
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
