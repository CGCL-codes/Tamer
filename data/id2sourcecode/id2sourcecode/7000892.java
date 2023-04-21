    public void setDevicePasswordEnable(boolean enableReadOnly, boolean enableReadWrite, boolean enableWriteOnly) throws OneWireException, OneWireIOException {
        if (enableWriteOnly) throw new OneWireException("The DS1922 does not have a write only password.");
        if (!isContainerReadOnlyPasswordSet() && enableReadOnly) throw new OneWireException("Container Read Password is not set");
        if (!isContainerReadWritePasswordSet()) throw new OneWireException("Container Read/Write Password is not set");
        if (enableReadOnly != enableReadWrite) throw new OneWireException("Both read only and read/write passwords " + "will both be disable or enabled");
        byte[] enableCommand = new byte[1];
        enableCommand[0] = (enableReadWrite ? ENABLE_BYTE : DISABLE_BYTE);
        register.write(PASSWORD_CONTROL_REGISTER & 0x3F, enableCommand, 0, 1);
        if (enableReadOnly) {
            readOnlyPasswordEnabled = true;
            readWritePasswordEnabled = true;
        } else {
            readOnlyPasswordEnabled = false;
            readWritePasswordEnabled = false;
        }
    }
