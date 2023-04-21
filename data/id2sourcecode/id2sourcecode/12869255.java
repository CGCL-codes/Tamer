    private void free(RemoteCall call, boolean reuse) throws RemoteException {
        Connection conn = ((StreamRemoteCall) call).getConnection();
        ref.getChannel().free(conn, reuse);
    }
