        protected void discardChannel(Exception e, SocketChannel channel, boolean outputTrace) {
            try {
                if (outputTrace && !keepSilent) e.printStackTrace();
                logging.debug(LOG_NAME, "NIO server: close conn from=" + channel.socket().getInetAddress());
                ConnFailedEvent fe = new ConnFailedEvent(channel.socket().getInetAddress());
                for (BlockingQueue<Event> q : failQueues) q.put(fe);
                synchronized (streamMap) {
                    streamMap.remove(channel);
                }
                synchronized (canceled) {
                    for (Iterator<CanceledEntry> i = canceled.iterator(); i.hasNext(); ) {
                        CanceledEntry ce = i.next();
                        if (ce.getChannel().equals(channel)) {
                            i.remove();
                            break;
                        }
                    }
                }
                channel.close();
            } catch (IOException e2) {
            } catch (InterruptedException e2) {
            }
        }
