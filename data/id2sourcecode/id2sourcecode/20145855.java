    public void Process() {
        SocketChannel chan = getSocketChannelHandler().getSocketChannel();
        try {
            if (initstate) {
                System.out.println(" -- initstate");
                int read = chan.read(Buffer);
                if (read < 0) {
                    getSocketChannelHandler().Cancel();
                    Chan.close();
                    done = true;
                } else if (!Buffer.hasRemaining()) {
                    initstate = false;
                    Buffer.flip();
                    Size = Buffer.getLong();
                    System.out.println("SIZE READ: " + Size);
                    CurSize = 0;
                    readstate = true;
                    HoldFile = Fac.FM.createNewFile("tmp", "tmp");
                    FileOutputStream fos = new FileOutputStream(HoldFile);
                    Chan = fos.getChannel();
                }
            }
            if (readstate) {
                System.out.println(" -- readstate");
                Buffer.clear();
                int read = chan.read(Buffer);
                if (read < 0) {
                    getSocketChannelHandler().Cancel();
                    Chan.close();
                    done = true;
                } else {
                    System.out.println(" -- READ DONE!!");
                    CurSize += read;
                    System.out.println("CurSize! " + CurSize);
                    Buffer.flip();
                    Chan.write(Buffer);
                    SimpleSocketChannelHandler simp = (SimpleSocketChannelHandler) getSocketChannelHandler();
                    if (CurSize >= Size) {
                        Chan.close();
                        System.out.println("FINISHED READING FILE!! " + HoldFile.getPath());
                        readstate = false;
                        done = true;
                        SimpleState s = new SimpleState();
                        s.D = new Date();
                        s.F = HoldFile;
                        simp.State = s;
                        simp.Fac.AddToWaitList(simp);
                        simp.clearInterestOps();
                    } else {
                        simp.setInterestOps(SelectionKey.OP_READ);
                    }
                }
            }
            if (writestate) {
                System.out.println(" -- writing! --");
                SimpleSocketChannelHandler simp = (SimpleSocketChannelHandler) getSocketChannelHandler();
                if (!Buffer.hasRemaining()) {
                    Buffer.clear();
                    int len = Chan.read(Buffer);
                    if (len < 0) {
                        done = true;
                        simp.clearInterestOps();
                        Chan.close();
                    }
                    Buffer.flip();
                }
                if (!done) {
                    chan.write(Buffer);
                    simp.setInterestOps(SelectionKey.OP_WRITE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            getSocketChannelHandler().Cancel();
            done = true;
        }
    }
