    public void store(byte[] key, byte[] data) {
        BigInteger bKey = SHA1Digester.digest(key);
        BigInteger bData = new BigInteger(data);
        FindNodeHandler handler = new FindNodeHandler();
        BigInteger rpcID = generateRPCID();
        handler.setRpcID(rpcID);
        handler.setContacts(contacts);
        handler.setSearchedNode(bKey);
        synchronized (rpcIDMap) {
            rpcIDMap.put(rpcID, handler);
        }
        long maxWait = Long.parseLong(System.getProperty("jkad.findnode.maxwait"));
        handler.run();
        while (System.currentTimeMillis() - handler.getLastAccess() < maxWait) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                linea = ("WARN: " + e);
                fich.writelog(linea);
            }
        }
        synchronized (rpcIDMap) {
            rpcIDMap.remove(rpcID);
        }
        storeValueOnNodes(handler.getResults(), bKey, bData);
    }
