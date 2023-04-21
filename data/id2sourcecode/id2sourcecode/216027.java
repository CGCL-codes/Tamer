    public static void main(String[] args) throws Exception {
        int port = 1234;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception exc) {
            System.out.println("To specify a port, include it as the first argument.");
        } finally {
            System.out.println("Telnet to this computer on port " + port + " and type the name of a file in this directory.");
        }
        NioServer ns = new NioServer();
        ns.setSingleTcpPort(port).setSingleUdpPort(port);
        ns.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String prop = evt.getPropertyName();
                Object oldVal = evt.getOldValue();
                Object newVal = evt.getNewValue();
                System.out.println("Property: " + prop + ", Old: " + oldVal + ", New: " + newVal);
                if (newVal instanceof Throwable) {
                    ((Throwable) newVal).printStackTrace();
                }
            }
        });
        ns.addNioServerListener(new NioServer.Listener() {

            private Charset charset = Charset.forName("US-ASCII");

            private CharsetEncoder encoder = charset.newEncoder();

            private CharsetDecoder decoder = charset.newDecoder();

            private CharBuffer greeting = CharBuffer.wrap("Greetings. Enter filename: ");

            private CharBuffer ack = CharBuffer.wrap("Sending...\r\n");

            private CharBuffer nackTooLong = CharBuffer.wrap("\r\nFilename too long. Try again: ");

            private CharBuffer nackNotFound = CharBuffer.wrap("\r\nFile not found. Try again: ");

            private CharBuffer request = CharBuffer.allocate(100);

            @Override
            public void newConnectionReceived(NioServer.Event evt) {
                SocketAddress local = evt.getLocalSocketAddress();
                SocketAddress remote = evt.getRemoteSocketAddress();
                System.out.println("New connection from " + remote + " to " + local);
                ByteBuffer buff = evt.getOutputBuffer();
                buff.clear();
                greeting.rewind();
                encoder.reset().encode(greeting, buff, true);
                buff.flip();
            }

            @Override
            public void connectionClosed(NioServer.Event evt) {
                System.out.println("Sorry to see you go: " + evt.getKey().channel());
            }

            @Override
            public void tcpDataReceived(NioServer.Event evt) {
                ByteBuffer inBuff = evt.getInputBuffer();
                ByteBuffer outBuff = evt.getOutputBuffer();
                request.clear();
                CoderResult cr = decoder.reset().decode(inBuff, request, true);
                request.flip();
                String s = request.toString();
                if (cr == CoderResult.OVERFLOW) {
                    outBuff.clear();
                    encoder.reset().encode((CharBuffer) nackTooLong.rewind(), outBuff, true);
                    outBuff.flip();
                    inBuff.clear().flip();
                    return;
                }
                if (s.contains("\r") || s.contains("\n")) {
                    FileInputStream fis = null;
                    try {
                        s = s.trim();
                        fis = new FileInputStream(s);
                        FileChannel fc = fis.getChannel();
                        evt.getKey().attach(fc);
                        evt.setNotifyOnTcpWritable(true);
                        outBuff.clear();
                        ack.rewind();
                        encoder.reset().encode(ack, outBuff, true);
                        outBuff.flip();
                    } catch (IOException ex) {
                        Logger.getLogger(this.getClass().getName()).warning(ex.getMessage());
                        outBuff.clear();
                        encoder.reset().encode((CharBuffer) nackNotFound.rewind(), outBuff, true);
                        outBuff.flip();
                        inBuff.clear().flip();
                    }
                } else {
                    inBuff.flip();
                }
            }

            @Override
            public void udpDataReceived(NioServer.Event evt) {
            }

            @Override
            public void tcpReadyToWrite(NioServer.Event evt) {
                ByteBuffer dst = evt.getOutputBuffer();
                FileChannel fc = (FileChannel) evt.getKey().attachment();
                if (fc == null) {
                    return;
                }
                dst.clear();
                try {
                    if (fc.read(dst) < 0) {
                        fc.close();
                        evt.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
                dst.flip();
            }
        });
        ns.start();
    }
