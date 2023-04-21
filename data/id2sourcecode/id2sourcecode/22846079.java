    public void setSchedulingGroup(int group) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeInt(group);
        mRemote.transact(SET_SCHEDULING_GROUP_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
