        public void add(final KeyAttachment key, final int ops) {
            Runnable r = new Runnable() {

                public void run() {
                    if (key == null) return;
                    NioChannel nch = key.getChannel();
                    if (nch == null) return;
                    SocketChannel ch = nch.getIOChannel();
                    if (ch == null) return;
                    SelectionKey sk = ch.keyFor(selector);
                    try {
                        if (sk == null) {
                            sk = ch.register(selector, ops, key);
                        } else {
                            sk.interestOps(sk.interestOps() | ops);
                        }
                    } catch (CancelledKeyException cx) {
                        cancel(sk, key, ops);
                    } catch (ClosedChannelException cx) {
                        cancel(sk, key, ops);
                    }
                }
            };
            events.offer(r);
            wakeup();
        }
