    public final void scheduleDestroyBackupAgent(ApplicationInfo app) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        app.writeToParcel(data, 0);
        mRemote.transact(SCHEDULE_DESTROY_BACKUP_AGENT_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
