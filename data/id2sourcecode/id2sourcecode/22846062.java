    public final void scheduleBindService(IBinder token, Intent intent, boolean rebind) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        intent.writeToParcel(data, 0);
        data.writeInt(rebind ? 1 : 0);
        mRemote.transact(SCHEDULE_BIND_SERVICE_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
