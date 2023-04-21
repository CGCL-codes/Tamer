    public final void scheduleWindowVisibility(IBinder token, boolean showWindow) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(showWindow ? 1 : 0);
        mRemote.transact(SCHEDULE_WINDOW_VISIBILITY_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
