                public void run() {
                    if (key == null) return;
                    NioChannel nch = key.getChannel();
                    if (nch == null) return;
                    SocketChannel ch = nch.getIOChannel();
                    if (ch == null) return;
                    SelectionKey sk = ch.keyFor(selector);
                    try {
                        if (sk == null) {
                            if (SelectionKey.OP_WRITE == (ops & SelectionKey.OP_WRITE)) countDown(key.getWriteLatch());
                            if (SelectionKey.OP_READ == (ops & SelectionKey.OP_READ)) countDown(key.getReadLatch());
                        } else {
                            sk.interestOps(sk.interestOps() & (~ops));
                            if (SelectionKey.OP_WRITE == (ops & SelectionKey.OP_WRITE)) countDown(key.getWriteLatch());
                            if (SelectionKey.OP_READ == (ops & SelectionKey.OP_READ)) countDown(key.getReadLatch());
                            if (sk.interestOps() == 0) {
                                sk.cancel();
                                sk.attach(null);
                            }
                        }
                    } catch (CancelledKeyException cx) {
                        if (sk != null) {
                            sk.cancel();
                            sk.attach(null);
                        }
                    }
                }
