    public final void scheduleSuicide() throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        mRemote.transact(SCHEDULE_SUICIDE_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
