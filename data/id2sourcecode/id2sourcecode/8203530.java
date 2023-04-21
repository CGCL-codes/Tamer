    public boolean putProperties(String key, Properties props) throws Exception {
        bamboo_put_arguments putArgs = new bamboo_put_arguments();
        putArgs.application = "Put";
        putArgs.client_library = "Remote Tea ONC/RPC";
        MessageDigest md = MessageDigest.getInstance("SHA");
        putArgs.key = new bamboo_key();
        putArgs.key.value = md.digest(key.getBytes());
        putArgs.value = new bamboo_value();
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        props.store(array, key);
        putArgs.value.value = array.toByteArray();
        putArgs.ttl_sec = Integer.parseInt(DHT_TTL);
        putArgs.secret_hash = new bamboo_hash();
        putArgs.secret_hash.algorithm = "SHA";
        putArgs.secret_hash.hash = md.digest(DHT_SECRET.getBytes());
        log.error("HASH " + md.digest(array.toByteArray()));
        int res = -1;
        int tries = 0;
        while (res == -1 && tries < DHT_RETRIES) {
            try {
                res = client.BAMBOO_DHT_PROC_PUT_3(putArgs);
            } catch (OncRpcTimeoutException e) {
                tries++;
                InetAddress inetAddr = InetAddress.getByName(this.gateways.elementAt((originalGatewayPos + tries) % (gateways.size() - 1)));
                client = new gateway_protClient(inetAddr, DHT_GW_PORT, ONCRPC_TCP);
            }
        }
        if (res == 0) {
            log.info("DhtInfo system replies for PUT: " + key + " : BAMBOO_OK");
            return true;
        } else {
            return false;
        }
    }
