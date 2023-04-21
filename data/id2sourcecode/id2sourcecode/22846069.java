    public final void requestThumbnail(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(REQUEST_THUMBNAIL_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
