    public final void scheduleSendResult(IBinder token, List<ResultInfo> results) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeTypedList(results);
        mRemote.transact(SCHEDULE_SEND_RESULT_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
