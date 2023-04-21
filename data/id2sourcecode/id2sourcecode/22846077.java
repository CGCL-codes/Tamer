    public final void requestPss() throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        mRemote.transact(REQUEST_PSS_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
