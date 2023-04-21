    public SnapshotAttribute(final SnapAttributeExtract extract, final SnapshotAttributes attrs) {
        this.snapshotAttributes = attrs;
        int _idAttr = extract.getId_att();
        int _format = extract.getData_format();
        int _dataType = extract.getData_type();
        int _writable = extract.getWritable();
        String _completeName = extract.getAttribute_complete_name();
        this.attribute_complete_name = _completeName;
        this.attribute_id = _idAttr;
        this.data_type = _dataType;
        this.data_format = _format;
        this.permit = _writable;
        this.display_format = "";
        SnapshotAttributeReadValue _readValue = null;
        SnapshotAttributeWriteValue _writeValue = null;
        Object val = extract.getValue();
        switch(_format) {
            case AttrDataFormat._SCALAR:
                switch(_writable) {
                    case AttrWriteType._READ:
                        _readValue = new SnapshotAttributeReadValue(_format, _dataType, val);
                        break;
                    case AttrWriteType._READ_WITH_WRITE:
                        _readValue = new SnapshotAttributeReadValue(_format, _dataType, getValue(val, 0));
                        _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, getValue(val, 1));
                        break;
                    case AttrWriteType._WRITE:
                        _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, val);
                        break;
                    case AttrWriteType._READ_WRITE:
                        _readValue = new SnapshotAttributeReadValue(_format, _dataType, getValue(val, 0));
                        _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, getValue(val, 1));
                        break;
                }
                break;
            case AttrDataFormat._SPECTRUM:
                switch(_writable) {
                    case AttrWriteType._READ:
                        switch(_dataType) {
                            case TangoConst.Tango_DEV_STATE:
                            case TangoConst.Tango_DEV_STRING:
                            case TangoConst.Tango_DEV_BOOLEAN:
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                _readValue = new SnapshotAttributeReadValue(_format, _dataType, val);
                                break;
                            default:
                        }
                        break;
                    case AttrWriteType._WRITE:
                        switch(_dataType) {
                            case TangoConst.Tango_DEV_STRING:
                            case TangoConst.Tango_DEV_BOOLEAN:
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, val);
                                break;
                            default:
                        }
                        break;
                    case AttrWriteType._READ_WITH_WRITE:
                    case AttrWriteType._READ_WRITE:
                        Object[] temp = (Object[]) val;
                        switch(_dataType) {
                            case TangoConst.Tango_DEV_STRING:
                                try {
                                    String[] readst = (String[]) temp[0];
                                    _readValue = new SnapshotAttributeReadValue(_format, _dataType, readst);
                                } catch (ClassCastException cce) {
                                    _readValue = new SnapshotAttributeReadValue(_format, _dataType, new String[0]);
                                }
                                try {
                                    String[] writest = (String[]) temp[1];
                                    _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, writest);
                                } catch (ClassCastException cce) {
                                    _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, new String[0]);
                                }
                                break;
                            case TangoConst.Tango_DEV_BOOLEAN:
                                try {
                                    Boolean[] readb = (Boolean[]) temp[0];
                                    _readValue = new SnapshotAttributeReadValue(_format, _dataType, readb);
                                } catch (ClassCastException cce) {
                                    _readValue = new SnapshotAttributeReadValue(_format, _dataType, new Boolean[0]);
                                }
                                try {
                                    Boolean[] writeb = (Boolean[]) temp[1];
                                    _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, writeb);
                                } catch (ClassCastException cce) {
                                    _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, new Boolean[0]);
                                }
                                break;
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                                try {
                                    Byte[] readc = (Byte[]) temp[0];
                                    _readValue = new SnapshotAttributeReadValue(_format, _dataType, readc);
                                } catch (ClassCastException cce) {
                                    _readValue = new SnapshotAttributeReadValue(_format, _dataType, new Byte[0]);
                                }
                                try {
                                    Byte[] writec = (Byte[]) temp[1];
                                    _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, writec);
                                } catch (ClassCastException cce) {
                                    _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, new Byte[0]);
                                }
                                break;
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                                try {
                                    Integer[] readl = (Integer[]) temp[0];
                                    _readValue = new SnapshotAttributeReadValue(_format, _dataType, readl);
                                } catch (ClassCastException cce) {
                                    _readValue = new SnapshotAttributeReadValue(_format, _dataType, new Integer[0]);
                                }
                                try {
                                    Integer[] writel = (Integer[]) temp[1];
                                    _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, writel);
                                } catch (ClassCastException cce) {
                                    _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, new Integer[0]);
                                }
                                break;
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                                try {
                                    Short[] reads = (Short[]) temp[0];
                                    _readValue = new SnapshotAttributeReadValue(_format, _dataType, reads);
                                } catch (ClassCastException cce) {
                                    _readValue = new SnapshotAttributeReadValue(_format, _dataType, new Short[0]);
                                }
                                try {
                                    Short[] writes = (Short[]) temp[1];
                                    _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, writes);
                                } catch (ClassCastException cce) {
                                    _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, new Short[0]);
                                }
                                break;
                            case TangoConst.Tango_DEV_FLOAT:
                                try {
                                    Float[] readf = (Float[]) temp[0];
                                    _readValue = new SnapshotAttributeReadValue(_format, _dataType, readf);
                                } catch (ClassCastException cce) {
                                    _readValue = new SnapshotAttributeReadValue(_format, _dataType, new Float[0]);
                                }
                                try {
                                    Float[] writef = (Float[]) temp[1];
                                    _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, writef);
                                } catch (ClassCastException cce) {
                                    _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, new Float[0]);
                                }
                                break;
                            case TangoConst.Tango_DEV_DOUBLE:
                                try {
                                    Double[] readd = (Double[]) temp[0];
                                    _readValue = new SnapshotAttributeReadValue(_format, _dataType, readd);
                                } catch (ClassCastException cce) {
                                    _readValue = new SnapshotAttributeReadValue(_format, _dataType, new Double[0]);
                                }
                                try {
                                    Double[] writed = (Double[]) temp[1];
                                    _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, writed);
                                } catch (ClassCastException cce) {
                                    _writeValue = new SnapshotAttributeWriteValue(_format, _dataType, new Double[0]);
                                }
                                break;
                            default:
                        }
                        break;
                    default:
                }
                break;
            case AttrDataFormat._IMAGE:
                switch(_writable) {
                    case AttrWriteType._READ:
                        switch(_dataType) {
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                _readValue = new SnapshotAttributeReadValue(_format, _dataType, val);
                                break;
                            case TangoConst.Tango_DEV_STATE:
                            case TangoConst.Tango_DEV_STRING:
                            case TangoConst.Tango_DEV_BOOLEAN:
                            default:
                        }
                        break;
                    default:
                }
                break;
            default:
        }
        if (_readValue != null && _writeValue != null) {
            if (_format == AttrDataFormat._SCALAR || _format == AttrDataFormat._SPECTRUM) {
                this.deltaValue = SnapshotAttributeDeltaValue.getInstance(_writeValue, _readValue);
            } else {
                this.deltaValue = new SnapshotAttributeDeltaValue(SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT, null);
            }
        } else {
            this.deltaValue = new SnapshotAttributeDeltaValue(SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT, null);
        }
        if (_readValue != null) {
            this.readValue = _readValue;
        } else {
            this.readValue = new SnapshotAttributeReadValue(SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT, 0, null);
        }
        if (_writeValue != null) {
            this.writeValue = _writeValue;
        } else {
            this.writeValue = new SnapshotAttributeWriteValue(SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT, 0, null);
        }
        updateDisplayFormat();
    }
