    public final void scheduleLaunchActivity(Intent intent, IBinder token, int ident, ActivityInfo info, Bundle state, List<ResultInfo> pendingResults, List<Intent> pendingNewIntents, boolean notResumed, boolean isForward) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        intent.writeToParcel(data, 0);
        data.writeStrongBinder(token);
        data.writeInt(ident);
        info.writeToParcel(data, 0);
        data.writeBundle(state);
        data.writeTypedList(pendingResults);
        data.writeTypedList(pendingNewIntents);
        data.writeInt(notResumed ? 1 : 0);
        data.writeInt(isForward ? 1 : 0);
        mRemote.transact(SCHEDULE_LAUNCH_ACTIVITY_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
