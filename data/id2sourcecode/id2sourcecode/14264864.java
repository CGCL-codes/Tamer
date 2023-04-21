    public DeviceAttribute[] write_read_attribute(DeviceProxy arg0, DeviceAttribute[] devattr) throws DevFailed {
        return (DeviceAttribute[]) WebServerClientUtil.getResponse(this, classParam, "write_read_attribute", new Object[] { devattr }, new Class[] { DeviceAttribute[].class });
    }
