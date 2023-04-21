    public RemoteCall newCall(RemoteObject obj, Operation[] ops, int opnum, long hash) throws RemoteException {
        clientRefLog.log(Log.BRIEF, "get connection");
        Connection conn = ref.getChannel().newConnection();
        try {
            clientRefLog.log(Log.VERBOSE, "create call context");
            if (clientCallLog.isLoggable(Log.VERBOSE)) {
                logClientCall(obj, ops[opnum]);
            }
            RemoteCall call = new StreamRemoteCall(conn, ref.getObjID(), opnum, hash);
            try {
                marshalCustomCallData(call.getOutputStream());
            } catch (IOException e) {
                throw new MarshalException("error marshaling " + "custom call data");
            }
            return call;
        } catch (RemoteException e) {
            ref.getChannel().free(conn, false);
            throw e;
        }
    }
