    public final void scheduleRelaunchActivity(IBinder token, List<ResultInfo> pendingResults, List<Intent> pendingNewIntents, int configChanges, boolean notResumed, Configuration config) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeTypedList(pendingResults);
        data.writeTypedList(pendingNewIntents);
        data.writeInt(configChanges);
        data.writeInt(notResumed ? 1 : 0);
        if (config != null) {
            data.writeInt(1);
            config.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(SCHEDULE_RELAUNCH_ACTIVITY_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
