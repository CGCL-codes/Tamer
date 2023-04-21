    public void dumpService(FileDescriptor fd, IBinder token, String[] args) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeFileDescriptor(fd);
        data.writeStrongBinder(token);
        data.writeStringArray(args);
        mRemote.transact(DUMP_SERVICE_TRANSACTION, data, null, 0);
        data.recycle();
    }
