    public final void scheduleReceiver(Intent intent, ActivityInfo info, int resultCode, String resultData, Bundle map, boolean sync) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        intent.writeToParcel(data, 0);
        info.writeToParcel(data, 0);
        data.writeInt(resultCode);
        data.writeString(resultData);
        data.writeBundle(map);
        data.writeInt(sync ? 1 : 0);
        mRemote.transact(SCHEDULE_RECEIVER_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
