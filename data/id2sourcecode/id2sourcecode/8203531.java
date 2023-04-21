    public boolean deleteProperties(String key) throws Exception {
        bamboo_rm_arguments removeArgs = new bamboo_rm_arguments();
        removeArgs.application = "Put";
        removeArgs.client_library = "Remote Tea ONC/RPC";
        MessageDigest md = MessageDigest.getInstance("SHA");
        removeArgs.key = new bamboo_key();
        removeArgs.key.value = md.digest(key.getBytes());
        removeArgs.value_hash = new bamboo_hash();
        removeArgs.value_hash.algorithm = "SHA";
        removeArgs.value_hash.hash = md.digest(getPropertiesArray(key));
        log.error("HASH " + removeArgs.value_hash.hash);
        removeArgs.ttl_sec = Integer.parseInt(DHT_TTL);
        removeArgs.secret_hash_alg = "SHA";
        removeArgs.secret = DHT_SECRET.getBytes();
        int res = -1;
        int tries = 0;
        while (res == -1 && tries < DHT_RETRIES) {
            try {
                res = client.BAMBOO_DHT_PROC_RM_3(removeArgs);
            } catch (OncRpcTimeoutException e) {
                tries++;
                InetAddress inetAddr = InetAddress.getByName(this.gateways.elementAt((originalGatewayPos + tries) % (gateways.size() - 1)));
                client = new gateway_protClient(inetAddr, DHT_GW_PORT, ONCRPC_TCP);
            }
        }
        if (res == 0) {
            log.info("DhtInfo system replies for DELETE: " + key + "  : BAMBOO_OK");
            return true;
        } else {
            return false;
        }
    }
