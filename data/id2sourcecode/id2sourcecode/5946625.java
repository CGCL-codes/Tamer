    public void testRun() {
        String ip = "10.0.0.1";
        Integer port = 5000;
        StoreRPC storeRPC = new StoreRPC();
        try {
            storeRPC.setKey(new BigInteger(digester.digest("0".getBytes())));
            storeRPC.setValue(new BigInteger("polaco!".getBytes()));
            RPCInfo<StoreRPC> rpcInfo = new RPCInfo(storeRPC, ip, port);
            StoreResponseHandler handler = new StoreResponseHandler();
            handler.setRPCInfo(rpcInfo);
            handler.run();
            DataManagerFacade<String> storage = DataManagerFacade.getDataManager();
            assertEquals("polaco!", storage.get(new BigInteger(digester.digest("0".getBytes()))));
        } catch (Exception e) {
            fail();
        }
    }
