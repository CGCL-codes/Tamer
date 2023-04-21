    public final void scheduleConfigurationChanged(Configuration config) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        config.writeToParcel(data, 0);
        mRemote.transact(SCHEDULE_CONFIGURATION_CHANGED_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
