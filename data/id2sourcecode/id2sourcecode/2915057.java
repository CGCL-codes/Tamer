        @Override
        public void run() {
            try {
                long start = System.currentTimeMillis();
                System.out.println("Start receiving: " + start);
                FileOutputStream fos = new FileOutputStream(dest);
                FileChannel fc = fos.getChannel();
                ServerSocketChannel ssc = ServerSocketChannel.open();
                ssc.socket().bind(new InetSocketAddress(Main.fileReceivePort));
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(true);
                ByteBuffer bb = ByteBuffer.allocate(buff);
                long middle = System.currentTimeMillis();
                long sizeremaining = size;
                int n = -1;
                while (sizeremaining > 0) if ((n = sc.read(bb)) != -1) {
                    bb.flip();
                    fc.write(bb);
                    bb.compact();
                    sizeremaining -= n;
                }
                bb.clear();
                fc.close();
                fos.flush();
                fos.close();
                sc.close();
                ssc.close();
                long end = System.currentTimeMillis();
                System.out.println("Vorbereitung: " + (middle - start));
                System.out.println("Lesen/Senden: " + (end - middle));
                System.out.println("Gesamt: " + (end - start));
                System.gc();
            } catch (Exception e) {
                e.printStackTrace();
            }
            frt = null;
        }
