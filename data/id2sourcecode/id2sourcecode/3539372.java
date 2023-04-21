    private WebConnection getPooledConnection(InetAddress ia, int port, List<WebConnection> pool) throws IOException {
        WebConnection wc = null;
        synchronized (pool) {
            if (pool.size() > 0) {
                wc = pool.remove(pool.size() - 1);
                synchronized (selector) {
                    selector.selectNow();
                    Set<SelectionKey> set = selector.selectedKeys();
                    SelectionKey sk = wc.getSelectionKey();
                    wc.setSelectionKey(null);
                    if (!set.contains(sk)) {
                        logger.logError(Logger.WARN, "Keepalive connection not in selected set, ignoring");
                        wc.close();
                        return getPooledConnection(ia, port, pool);
                    }
                    sk.cancel();
                    wc.getChannel().configureBlocking(true);
                    return wc;
                }
            } else {
                wc = new WebConnection(ia, port, resolver.isProxyConnected(), resolver.getProxyAuthString(), counter, logger, nlsoHandler);
            }
            return wc;
        }
    }
