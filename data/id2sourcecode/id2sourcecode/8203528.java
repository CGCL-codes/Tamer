    public byte[] getPropertiesArray(String key) throws Exception {
        bamboo_get_args getArgs = new bamboo_get_args();
        getArgs.application = "Get";
        getArgs.client_library = "Remote Tea ONC/RPC";
        MessageDigest md = MessageDigest.getInstance("SHA");
        getArgs.key = new bamboo_key();
        getArgs.key.value = md.digest(key.getBytes());
        getArgs.maxvals = Integer.MAX_VALUE;
        getArgs.placemark = new bamboo_placemark();
        getArgs.placemark.value = new byte[0];
        bamboo_get_res res = null;
        int tries = 0;
        while (res == null && tries < DHT_RETRIES) {
            try {
                res = client.BAMBOO_DHT_PROC_GET_2(getArgs);
            } catch (OncRpcTimeoutException e) {
                tries++;
                InetAddress inetAddr = InetAddress.getByName(this.gateways.elementAt((originalGatewayPos + tries) % (gateways.size() - 1)));
                client = new gateway_protClient(inetAddr, DHT_GW_PORT, ONCRPC_TCP);
            }
        }
        if (res.values.length == 0) return null;
        log.info("** DhtInfo system replies for GET: " + key + " \n" + new String(res.values[0].value));
        return res.values[0].value;
    }
