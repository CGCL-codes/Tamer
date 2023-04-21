    public final void scheduleLowMemory() throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        mRemote.transact(SCHEDULE_LOW_MEMORY_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
