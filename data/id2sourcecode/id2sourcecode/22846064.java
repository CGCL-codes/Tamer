    public final void scheduleServiceArgs(IBinder token, int startId, int flags, Intent args) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(startId);
        data.writeInt(flags);
        if (args != null) {
            data.writeInt(1);
            args.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(SCHEDULE_SERVICE_ARGS_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
