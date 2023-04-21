    public Any command_inout(final String in_cmd, final Any in_any) throws DevFailed {
        Util.out4.println("DeviceImpl.command_inout(): command received : " + in_cmd);
        blackbox.insert_cmd(in_cmd, 1);
        Any out_any = null;
        try {
            Util.increaseAccessConter();
            if (Util.getAccessConter() > Util.getPoaThreadPoolMax() - 2) {
                Util.decreaseAccessConter();
                Except.throw_exception("API_MemoryAllocation", Util.instance().get_ds_real_name() + ": No thread available to connect device", "DeviceImpl.write_attributes()");
            }
            switch(Util.get_serial_model()) {
                case BY_CLASS:
                    synchronized (device_class) {
                        out_any = device_class.command_handler(this, in_cmd, in_any);
                    }
                    break;
                case BY_DEVICE:
                    synchronized (this) {
                        out_any = device_class.command_handler(this, in_cmd, in_any);
                    }
                    break;
                default:
                    out_any = device_class.command_handler(this, in_cmd, in_any);
            }
        } catch (final DevFailed exc) {
            Util.decreaseAccessConter();
            throw exc;
        } catch (final Exception exc) {
            Util.decreaseAccessConter();
            Except.throw_exception("API_ExceptionCatched", exc.toString(), "DeviceImpl.command_inout");
        }
        Util.decreaseAccessConter();
        Util.out4.println("DeviceImpl.command_inout(): leaving method for command " + in_cmd);
        return out_any;
    }
