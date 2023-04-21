        @Override
        public void run() {
            if (this.dest == null) {
                return;
            }
            try {
                final long start = System.currentTimeMillis();
                if (!this.dest.exists()) {
                    this.dest.createNewFile();
                }
                System.out.println("Start recieving: " + start);
                final FileOutputStream fos = new FileOutputStream(this.dest);
                final FileChannel fc = fos.getChannel();
                final ServerSocketChannel ssc = ServerSocketChannel.open();
                ssc.socket().bind(new InetSocketAddress(Main.fileReceivePort));
                final SocketChannel sc = ssc.accept();
                sc.configureBlocking(true);
                final ByteBuffer bb = ByteBuffer.allocate(FileReceiveThread.buff);
                final long middle = System.currentTimeMillis();
                long sizeremaining = this.size;
                int n = -1;
                while (sizeremaining > 0 && this.weiter) {
                    if ((n = sc.read(bb)) != -1) {
                        bb.flip();
                        fc.write(bb);
                        bb.compact();
                        sizeremaining -= n;
                        this.pd.setProgress(this.size - sizeremaining);
                    }
                }
                bb.clear();
                fc.close();
                fos.flush();
                fos.close();
                sc.close();
                ssc.close();
                final long end = System.currentTimeMillis();
                System.out.println("Preparation: " + (middle - start));
                System.out.println("Reading/Sending: " + (end - middle));
                System.out.println("Sum: " + (end - start));
                System.gc();
                this.curSess.receivedFile(true);
            } catch (final IOException e) {
                e.printStackTrace();
                this.pd.setVisible(false);
                JOptionPane.showMessageDialog(this.pd, "An error occured while receiving a file", "Error", JOptionPane.ERROR_MESSAGE);
                this.curSess.receivedFile(false);
            }
            this.pd.setVisible(false);
            RecieveThread.this.frt = null;
        }
