    public void updateTimeZone() throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        mRemote.transact(UPDATE_TIME_ZONE_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
