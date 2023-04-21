    public final void bindApplication(String packageName, ApplicationInfo info, List<ProviderInfo> providers, ComponentName testName, String profileName, Bundle testArgs, IInstrumentationWatcher testWatcher, int debugMode, boolean restrictedBackupMode, Configuration config, Map<String, IBinder> services) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IApplicationThread.descriptor);
        data.writeString(packageName);
        info.writeToParcel(data, 0);
        data.writeTypedList(providers);
        if (testName == null) {
            data.writeInt(0);
        } else {
            data.writeInt(1);
            testName.writeToParcel(data, 0);
        }
        data.writeString(profileName);
        data.writeBundle(testArgs);
        data.writeStrongInterface(testWatcher);
        data.writeInt(debugMode);
        data.writeInt(restrictedBackupMode ? 1 : 0);
        config.writeToParcel(data, 0);
        data.writeMap(services);
        mRemote.transact(BIND_APPLICATION_TRANSACTION, data, null, IBinder.FLAG_ONEWAY);
        data.recycle();
    }
