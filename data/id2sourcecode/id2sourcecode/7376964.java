    public DeviceAttribute[] write_read_attribute(DeviceAttribute[] devattr) throws DevFailed {
        return deviceProxy.write_read_attribute(this, devattr);
    }
