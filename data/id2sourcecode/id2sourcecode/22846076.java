    public final void scheduleActivityConfigurationChanged(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(SCHEDULE_ACTIVITY_CONFIGURATION_CHANGED_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
