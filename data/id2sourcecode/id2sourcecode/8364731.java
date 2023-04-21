    public byte[] findValue(byte[] data) {
        FindValueHandler handler = new FindValueHandler();
        BigInteger rpcID = generateRPCID();
        handler.setRpcID(rpcID);
        handler.setStorage(DataManagerFacade.getDataManager());
        handler.setContacts(contacts);
        BigInteger dig = SHA1Digester.digest(data);
        handler.setValueKey(dig);
        synchronized (rpcIDMap) {
            rpcIDMap.put(rpcID, handler);
        }
        long maxWait = Long.parseLong(System.getProperty("jkad.findvalue.maxwait"));
        handler.run();
        while (handler.getStatus() != Status.ENDED && System.currentTimeMillis() - handler.getLastAccess() < (maxWait)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                linea = ("WARN : " + e);
                fich.writelog(linea);
            }
        }
        synchronized (rpcIDMap) {
            rpcIDMap.remove(rpcID);
        }
        if ((handler.getResult() != null) && (handler.getClosestNode() != null)) {
            storeValueOnNodes(handler.getClosestNode(), dig, handler.getResult());
        }
        return handler.getResult() != null ? handler.getResult().toByteArray() : null;
    }
