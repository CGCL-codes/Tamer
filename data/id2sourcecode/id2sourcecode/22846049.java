    public final void schedulePauseActivity(IBinder token, boolean finished, boolean userLeaving, int configChanges) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(finished ? 1 : 0);
        data.writeInt(userLeaving ? 1 : 0);
        data.writeInt(configChanges);
        mRemote.transact(SCHEDULE_PAUSE_ACTIVITY_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
