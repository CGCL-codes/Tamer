    public void profilerControl(boolean start, String path, ParcelFileDescriptor fd) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeInt(start ? 1 : 0);
        data.writeString(path);
        if (fd != null) {
            data.writeInt(1);
            fd.writeToParcel(data, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(PROFILER_CONTROL_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
