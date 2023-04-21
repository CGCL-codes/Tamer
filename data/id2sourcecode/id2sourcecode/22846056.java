    public void scheduleNewIntent(List<Intent> intents, IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeTypedList(intents);
        data.writeStrongBinder(token);
        mRemote.transact(SCHEDULE_NEW_INTENT_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
