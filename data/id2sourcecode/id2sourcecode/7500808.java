    public synchronized void lockWrite() {
        Thread current = Thread.currentThread();
        int idx = getIndex(current);
        Node n = null;
        if (idx != -1) {
            n = (Node) waiters.elementAt(idx);
            if (n.state != Node.WRITER) {
                Log.debug("Upgrading from read to write lock. Node: " + n);
                idx = upgrade(idx, n);
            }
        } else {
            n = new Node(current, Node.WRITER);
            waiters.addElement(n);
            idx = waiters.size() - 1;
            if (firstWriter == Integer.MAX_VALUE) {
                firstWriter = idx;
            }
        }
        while (idx != 0) {
            try {
                long past = System.currentTimeMillis();
                Log.warn(current + " | Must wait for write lock!! Thread: " + current);
                wait();
                long now = System.currentTimeMillis();
                Log.warn(current + " | Waited for " + (now - past) + "ms to receive write lock. Thread: " + current);
            } catch (Exception e) {
            }
            idx = getIndex(current);
        }
        n.acquires++;
        n.granted = true;
    }
