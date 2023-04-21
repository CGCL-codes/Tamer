    public final void scheduleResumeActivity(IBinder token, boolean isForward) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(isForward ? 1 : 0);
        mRemote.transact(SCHEDULE_RESUME_ACTIVITY_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
