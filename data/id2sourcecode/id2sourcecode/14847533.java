    public DeviceAttribute[] write_read_attribute(final DeviceProxy deviceProxy, final DeviceAttribute[] devattr) throws DevFailed {
        checkIfTango(deviceProxy, "write_read_attribute");
        build_connection(deviceProxy);
        if (deviceProxy.access == TangoConst.ACCESS_READ) {
            ping(deviceProxy);
            throwNotAuthorizedException(deviceProxy.devname + ".write_read_attribute()", "DeviceProxy.write_read_attribute()");
        }
        AttributeValue_4[] in_attrval_4 = new AttributeValue_4[0];
        AttributeValue_4[] out_attrval_4 = new AttributeValue_4[0];
        if (deviceProxy.device_4 != null) {
            in_attrval_4 = new AttributeValue_4[devattr.length];
            for (int i = 0; i < devattr.length; i++) {
                in_attrval_4[i] = devattr[i].getAttributeValueObject_4();
            }
        } else {
            Except.throw_connection_failed("TangoApi_READ_ONLY_MODE", "Cannot execute write_read_attribute(), " + deviceProxy.devname + " is not a device_4Impl or above", "DeviceProxy.write_read_attribute()");
        }
        boolean done = false;
        final int retries = deviceProxy.transparent_reconnection ? 2 : 1;
        for (int i = 0; i < retries && !done; i++) {
            try {
                if (deviceProxy.device_4 != null) {
                    out_attrval_4 = deviceProxy.device_4.write_read_attributes_4(in_attrval_4, DevLockManager.getInstance().getClntIdent());
                }
                done = true;
            } catch (final DevFailed e) {
                throw e;
            } catch (final MultiDevFailed e) {
                throw new NamedDevFailedList(e, name(deviceProxy), "DeviceProxy.write_read_attribute", "MultiDevFailed");
            } catch (final Exception e) {
                manageExceptionReconnection(deviceProxy, retries, i, e, this.getClass() + ".write_read_attribute");
            }
        }
        final DeviceAttribute[] attr = new DeviceAttribute[out_attrval_4.length];
        if (deviceProxy.device_4 != null) {
            for (int i = 0; i < out_attrval_4.length; i++) {
                attr[i] = new DeviceAttribute(out_attrval_4[i]);
            }
        }
        return attr;
    }
