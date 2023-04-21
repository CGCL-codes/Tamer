    private void populateConsistentBuckets() {
        if (log.isDebugEnabled()) log.debug("++++ initializing internal hashing structure for consistent hashing");
        this.consistentBuckets = new TreeMap<Long, String>();
        MessageDigest md5 = MD5.get();
        if (this.totalWeight <= 0 && this.weights != null) {
            for (int i = 0; i < this.weights.length; i++) {
                this.totalWeight += (this.weights[i] == null) ? 1 : this.weights[i];
            }
        } else if (this.weights == null) {
            this.totalWeight = this.servers.length;
        }
        for (int i = 0; i < servers.length; i++) {
            int thisWeight = 1;
            if (this.weights != null && this.weights[i] != null) {
                thisWeight = this.weights[i];
            }
            double factor = Math.floor(((double) (40 * this.servers.length * thisWeight)) / (double) this.totalWeight);
            for (long j = 0; j < factor; j++) {
                byte[] d = md5.digest((servers[i] + "-" + j).getBytes());
                for (int h = 0; h < 4; h++) {
                    Long k = ((long) (d[3 + h * 4] & 0xFF) << 24) | ((long) (d[2 + h * 4] & 0xFF) << 16) | ((long) (d[1 + h * 4] & 0xFF) << 8) | ((long) (d[0 + h * 4] & 0xFF));
                    consistentBuckets.put(k, servers[i]);
                    if (log.isDebugEnabled()) log.debug("++++ added " + servers[i] + " to server bucket");
                }
            }
            if (log.isDebugEnabled()) log.debug("+++ creating initial connections (" + initConn + ") for host: " + servers[i]);
            for (int j = 0; j < initConn; j++) {
                SockIO socket = createSocket(servers[i]);
                if (socket == null) {
                    log.error("++++ failed to create connection to: " + servers[i] + " -- only " + j + " created.");
                    break;
                }
                addSocketToPool(availPool, servers[i], socket);
                if (log.isDebugEnabled()) log.debug("++++ created and added socket: " + socket.toString() + " for host " + servers[i]);
            }
        }
    }
