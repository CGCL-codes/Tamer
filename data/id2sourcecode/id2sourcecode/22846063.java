    public final void scheduleUnbindService(IBinder token, Intent intent) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        intent.writeToParcel(data, 0);
        mRemote.transact(SCHEDULE_UNBIND_SERVICE_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
