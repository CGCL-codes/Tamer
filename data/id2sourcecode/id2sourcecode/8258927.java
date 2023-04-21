    public static void processKeys(Object id, Set<SelectionKey> keySet) {
        Iterator<SelectionKey> iter = keySet.iterator();
        while (iter.hasNext()) {
            SelectionKey key = null;
            try {
                key = iter.next();
                if (log.isLoggable(Level.FINE)) log.fine(key.attachment() + " ops=" + Helper.opType(key.readyOps()) + " acc=" + key.isAcceptable() + " read=" + key.isReadable() + " write" + key.isWritable());
                processKey(id, key);
            } catch (IOException e) {
                log.log(Level.WARNING, id + "" + key.attachment() + "Processing of key failed, closing channel", e);
                try {
                    if (key != null) key.channel().close();
                } catch (Throwable ee) {
                    log.log(Level.WARNING, id + "" + key.attachment() + "Close of channel failed", ee);
                }
            } catch (CancelledKeyException e) {
                log.log(Level.FINE, id + "" + key.attachment() + "Processing of key failed, but continuing channel manager loop", e);
            } catch (Throwable e) {
                log.log(Level.WARNING, id + "" + key.attachment() + "Processing of key failed, but continuing channel manager loop", e);
                try {
                    key.cancel();
                } catch (Throwable ee) {
                }
            }
        }
        keySet.clear();
    }
