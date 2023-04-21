    public Properties getProperties(String key) throws Exception {
        InetAddress gateway = InetAddress.getByName(DHT_GW_ADDR);
        int port = Integer.parseInt(DHT_GW_PORT);
        bamboo_get_args getArgs = new bamboo_get_args();
        getArgs.application = Get.class.getName();
        getArgs.client_library = "Remote Tea ONC/RPC";
        MessageDigest md = MessageDigest.getInstance("SHA");
        getArgs.key = new bamboo_key();
        getArgs.key.value = md.digest(key.getBytes());
        getArgs.maxvals = Integer.MAX_VALUE;
        getArgs.placemark = new bamboo_placemark();
        getArgs.placemark.value = new byte[0];
        gateway_protClient client = null;
        client = new gateway_protClient(gateway, port, ONCRPC_TCP);
        bamboo_get_res res = null;
        res = client.BAMBOO_DHT_PROC_GET_2(getArgs);
        if (res.values.length == 0) return null;
        System.out.println("** GET RESULT ** \n" + new String(res.values[0].value));
        Properties props = new Properties();
        props.load(new ByteArrayInputStream(res.values[0].value));
        return props;
    }
