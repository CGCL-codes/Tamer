    public void write_attributes(final AttributeValue[] values) throws DevFailed {
        Util.out4.println("DeviceImpl.write_attributes arrived");
        blackbox.insert_op(Op_Write_Attr);
        final int nb_dev_attr = dev_attr.get_attr_nb();
        if (nb_dev_attr == 0) {
            Except.throw_exception("API_AttrNotFound", "The device does not have any attribute", "DeviceImpl.write_attributes");
        }
        final long nb_updated_attr = values.length;
        final Vector updated_attr = new Vector();
        int i;
        for (i = 0; i < nb_updated_attr; i++) {
            updated_attr.addElement(dev_attr.get_attr_ind_by_name(values[i].name));
        }
        for (i = 0; i < nb_updated_attr; i++) {
            if (dev_attr.get_attr_by_ind((Integer) updated_attr.elementAt(i)).get_writable() == AttrWriteType.READ || dev_attr.get_attr_by_ind((Integer) updated_attr.elementAt(i)).get_writable() == AttrWriteType.READ_WITH_WRITE) {
                final StringBuffer o = new StringBuffer("Attribute ");
                o.append(dev_attr.get_attr_by_ind((Integer) updated_attr.elementAt(i)).get_name());
                o.append(" is not writable");
                Except.throw_exception("API_AttrNotWritable", o.toString(), "DeviceImpl.write_attributes");
            }
        }
        for (i = 0; i < nb_updated_attr; i++) {
            try {
                dev_attr.get_w_attr_by_ind((Integer) updated_attr.elementAt(i)).set_write_value(values[i].value);
            } catch (final DevFailed ex) {
                for (int j = 0; j < i; j++) {
                    dev_attr.get_w_attr_by_ind((Integer) updated_attr.elementAt(j)).rollback();
                }
                throw ex;
            }
        }
        try {
            Util.increaseAccessConter();
            if (Util.getAccessConter() > Util.getPoaThreadPoolMax() - 2) {
                Util.decreaseAccessConter();
                Except.throw_exception("API_MemoryAllocation", Util.instance().get_ds_real_name() + ": No thread available to connect device", "DeviceImpl.write_attributes()");
            }
            always_executed_hook();
            switch(Util.get_serial_model()) {
                case BY_CLASS:
                    synchronized (device_class) {
                        write_attr_hardware(updated_attr);
                    }
                    break;
                case BY_DEVICE:
                    synchronized (this) {
                        write_attr_hardware(updated_attr);
                    }
                    break;
                default:
                    write_attr_hardware(updated_attr);
            }
        } catch (final DevFailed exc) {
            Util.decreaseAccessConter();
            throw exc;
        } catch (final Exception exc) {
            Util.decreaseAccessConter();
            Except.throw_exception("API_ExceptionCatched", exc.toString(), "DeviceImpl.write_attributes");
        }
        Util.decreaseAccessConter();
        Util.out4.println("Leaving DeviceImpl.write_attributes");
    }
