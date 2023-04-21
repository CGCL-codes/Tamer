    public final void scheduleCreateService(IBinder token, ServiceInfo info) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        info.writeToParcel(data, 0);
        mRemote.transact(SCHEDULE_CREATE_SERVICE_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
