    private int removeNS(short appId, byte[] key, byte[] value, int ttl, byte[] secret) {
        if (value.length > BAMBOO_BLOCK_SIZE - 2) {
            return bamboo_stat.BAMBOO_CAP;
        }
        bamboo_rm_arguments rmArgs = new bamboo_rm_arguments();
        rmArgs.application = DHT.class.getName();
        rmArgs.client_library = "Remote Tea ONC/RPC";
        rmArgs.key = new bamboo_key();
        rmArgs.key.value = key;
        byte[] v = new byte[value.length + 2];
        ByteBuffer bb = ByteBuffer.wrap(v);
        bb.putShort(appId);
        bb.put(value);
        rmArgs.value_hash = new bamboo_hash();
        rmArgs.value_hash.algorithm = "SHA";
        rmArgs.value_hash.hash = md.digest(bb.array());
        rmArgs.ttl_sec = ttl;
        if (secret != null) {
            rmArgs.secret_hash_alg = "SHA";
            rmArgs.secret = secret;
        } else {
            rmArgs.secret_hash_alg = "";
            rmArgs.secret = new byte[0];
        }
        gateway_protClient client = getRpcConnection();
        if (client == null) {
            return bamboo_stat.BAMBOO_AGAIN;
        }
        try {
            return client.BAMBOO_DHT_PROC_RM_3(rmArgs);
        } catch (IOException ex) {
            bootstrap.changeRelay(client.getClient().getHost());
            try {
                client.close();
            } catch (Exception ex2) {
            }
            return bamboo_stat.BAMBOO_AGAIN;
        } catch (OncRpcException ex) {
            return bamboo_stat.BAMBOO_AGAIN;
        }
    }
