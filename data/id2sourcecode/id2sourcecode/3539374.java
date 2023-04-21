    public void releaseConnection(WebConnection wc) {
        counter.inc("WebConnections released");
        if (wc.getInetAddress() == null) {
            wc.close();
            return;
        }
        Address a = new Address(wc.getInetAddress(), wc.getPort());
        if (!wc.getKeepAlive()) {
            removeFromPool(wc, pipelineConnections, a);
            wc.close();
            return;
        }
        boolean waiting = false;
        synchronized (wc) {
            waiting = wc.getWaiting();
            wc.setReleased();
        }
        if (!waiting) {
            removeFromPool(wc, pipelineConnections, a);
            synchronized (activeConnections) {
                List<WebConnection> pool = activeConnections.get(a);
                if (pool == null) {
                    pool = new ArrayList<WebConnection>();
                    activeConnections.put(a, pool);
                }
                try {
                    wc.getChannel().configureBlocking(false);
                    SelectionKey sk = wc.getChannel().register(selector, SelectionKey.OP_WRITE);
                    wc.setSelectionKey(sk);
                    pool.add(wc);
                    wc = null;
                } catch (CancelledKeyException cke) {
                    logger.logError(Logger.ALL, "Cancelled key, ignoring keepalive: " + cke);
                } catch (ClosedChannelException cce) {
                    logger.logError(Logger.ALL, "Closed channel, ignoring keepalive: " + cce);
                } catch (IOException e) {
                    logger.logError(Logger.ALL, "failed to configure blocking: " + e);
                } finally {
                    if (wc != null) {
                        wc.close();
                    }
                }
            }
        }
    }
