    public DbData[] splitDbData() throws DevFailed {
        final DbData[] argout = new DbData[2];
        final DbData dbData_r = new DbData(name);
        dbData_r.setData_type(data_type);
        dbData_r.setData_format(dataFormat);
        dbData_r.setWritable(writable);
        dbData_r.setMax_x(maxX);
        dbData_r.setMax_y(maxY);
        final DbData dbData_w = new DbData(name);
        dbData_w.setData_type(data_type);
        dbData_w.setData_format(dataFormat);
        dbData_w.setWritable(writable);
        dbData_w.setMax_x(0);
        dbData_w.setMax_y(maxY);
        if (timedData == null) {
            dbData_r.timedData = null;
            dbData_w.timedData = null;
            dbData_r.maxX = 0;
            dbData_r.maxY = 0;
            dbData_w.maxX = 0;
            dbData_w.maxY = 0;
            argout[0] = dbData_r;
            argout[1] = dbData_w;
            return argout;
        }
        final NullableTimedData[] timedAttrData_r = new NullableTimedData[timedData.length];
        final NullableTimedData[] timedAttrData_w = new NullableTimedData[timedData.length];
        for (int i = 0; i < timedData.length; i++) {
            int dimWrite = 0;
            switch(getData_type()) {
                case TangoConst.Tango_DEV_USHORT:
                case TangoConst.Tango_DEV_SHORT:
                case TangoConst.Tango_DEV_UCHAR:
                    final Short[] sh_ptr_init = (Short[]) timedData[i].value;
                    Short[] sh_ptr_read = null;
                    Short[] sh_ptr_write = null;
                    if (sh_ptr_init != null) {
                        if (dataFormat == AttrDataFormat._IMAGE) {
                            sh_ptr_read = new Short[timedData[i].x * timedData[i].y];
                            dimWrite = sh_ptr_init.length - timedData[i].x * timedData[i].y;
                        } else {
                            sh_ptr_read = new Short[timedData[i].x];
                            dimWrite = sh_ptr_init.length - timedData[i].x;
                            if (dimWrite > dbData_w.getMax_x()) {
                                dbData_w.setMax_x(dimWrite);
                            }
                        }
                        if (dimWrite < 0) {
                            dimWrite = 0;
                        }
                        sh_ptr_write = new Short[dimWrite];
                        if (dataFormat == AttrDataFormat._IMAGE) {
                            for (int j = 0; j < timedData[i].x * timedData[i].y; j++) {
                                sh_ptr_read[j] = sh_ptr_init[j];
                            }
                            for (int j = timedData[i].x * timedData[i].y; j < sh_ptr_init.length; j++) {
                                sh_ptr_write[j - timedData[i].x] = sh_ptr_init[j];
                            }
                        } else {
                            for (int j = 0; j < timedData[i].x; j++) {
                                sh_ptr_read[j] = sh_ptr_init[j];
                            }
                            for (int j = timedData[i].x; j < sh_ptr_init.length; j++) {
                                sh_ptr_write[j - timedData[i].x] = sh_ptr_init[j];
                            }
                        }
                    }
                    timedAttrData_r[i] = new NullableTimedData();
                    timedAttrData_r[i].data_type = data_type;
                    timedAttrData_r[i].x = timedData[i].x;
                    timedAttrData_r[i].y = timedData[i].y;
                    timedAttrData_r[i].time = timedData[i].time;
                    timedAttrData_w[i] = new NullableTimedData();
                    timedAttrData_w[i].data_type = data_type;
                    timedAttrData_w[i].x = timedData[i].x;
                    timedAttrData_w[i].y = timedData[i].y;
                    timedAttrData_w[i].time = timedData[i].time;
                    if (writable == AttrWriteType._WRITE) {
                        timedAttrData_r[i].value = sh_ptr_write;
                        timedAttrData_w[i].value = sh_ptr_read;
                    } else {
                        timedAttrData_r[i].value = sh_ptr_read;
                        timedAttrData_w[i].value = sh_ptr_write;
                    }
                    break;
                case TangoConst.Tango_DEV_DOUBLE:
                    final Double[] db_ptr_init = (Double[]) timedData[i].value;
                    Double[] db_ptr_read = null;
                    Double[] db_ptr_write = null;
                    if (db_ptr_init != null) {
                        if (dataFormat == AttrDataFormat._IMAGE) {
                            db_ptr_read = new Double[timedData[i].x * timedData[i].y];
                            dimWrite = db_ptr_init.length - timedData[i].x * timedData[i].y;
                        } else {
                            db_ptr_read = new Double[timedData[i].x];
                            dimWrite = db_ptr_init.length - timedData[i].x;
                            if (dimWrite > dbData_w.getMax_x()) {
                                dbData_w.setMax_x(dimWrite);
                            }
                        }
                        if (dimWrite < 0) {
                            dimWrite = 0;
                        }
                        db_ptr_write = new Double[dimWrite];
                        if (dataFormat == AttrDataFormat._IMAGE) {
                            for (int j = 0; j < timedData[i].x * timedData[i].y; j++) {
                                db_ptr_read[j] = db_ptr_init[j];
                            }
                            for (int j = timedData[i].x * timedData[i].y; j < db_ptr_init.length; j++) {
                                db_ptr_write[j - timedData[i].x] = db_ptr_init[j];
                            }
                        } else {
                            for (int j = 0; j < timedData[i].x; j++) {
                                db_ptr_read[j] = db_ptr_init[j];
                            }
                            for (int j = timedData[i].x; j < db_ptr_init.length; j++) {
                                db_ptr_write[j - timedData[i].x] = db_ptr_init[j];
                            }
                        }
                    }
                    timedAttrData_r[i] = new NullableTimedData();
                    timedAttrData_r[i].data_type = data_type;
                    timedAttrData_r[i].x = timedData[i].x;
                    timedAttrData_r[i].y = timedData[i].y;
                    timedAttrData_r[i].time = timedData[i].time;
                    timedAttrData_w[i] = new NullableTimedData();
                    timedAttrData_w[i].data_type = data_type;
                    timedAttrData_w[i].x = timedData[i].x;
                    timedAttrData_w[i].y = timedData[i].y;
                    timedAttrData_w[i].time = timedData[i].time;
                    if (writable == AttrWriteType._WRITE) {
                        timedAttrData_r[i].value = db_ptr_write;
                        timedAttrData_w[i].value = db_ptr_read;
                    } else {
                        timedAttrData_r[i].value = db_ptr_read;
                        timedAttrData_w[i].value = db_ptr_write;
                    }
                    break;
                case TangoConst.Tango_DEV_FLOAT:
                    final Float[] fl_ptr_init = (Float[]) timedData[i].value;
                    Float[] fl_ptr_read = null;
                    Float[] fl_ptr_write = null;
                    if (fl_ptr_init != null) {
                        if (dataFormat == AttrDataFormat._IMAGE) {
                            fl_ptr_read = new Float[timedData[i].x * timedData[i].y];
                            dimWrite = fl_ptr_init.length - timedData[i].x * timedData[i].y;
                        } else {
                            fl_ptr_read = new Float[timedData[i].x];
                            dimWrite = fl_ptr_init.length - timedData[i].x;
                            if (dimWrite > dbData_w.getMax_x()) {
                                dbData_w.setMax_x(dimWrite);
                            }
                        }
                        if (dimWrite < 0) {
                            dimWrite = 0;
                        }
                        fl_ptr_write = new Float[dimWrite];
                        if (dataFormat == AttrDataFormat._IMAGE) {
                            for (int j = 0; j < timedData[i].x * timedData[i].y; j++) {
                                fl_ptr_read[j] = fl_ptr_init[j];
                            }
                            for (int j = timedData[i].x * timedData[i].y; j < fl_ptr_init.length; j++) {
                                fl_ptr_write[j - timedData[i].x] = fl_ptr_init[j];
                            }
                        } else {
                            for (int j = 0; j < timedData[i].x; j++) {
                                fl_ptr_read[j] = fl_ptr_init[j];
                            }
                            for (int j = timedData[i].x; j < fl_ptr_init.length; j++) {
                                fl_ptr_write[j - timedData[i].x] = fl_ptr_init[j];
                            }
                        }
                    }
                    timedAttrData_r[i] = new NullableTimedData();
                    timedAttrData_r[i].data_type = data_type;
                    timedAttrData_r[i].x = timedData[i].x;
                    timedAttrData_r[i].y = timedData[i].y;
                    timedAttrData_r[i].time = timedData[i].time;
                    timedAttrData_w[i] = new NullableTimedData();
                    timedAttrData_w[i].data_type = data_type;
                    timedAttrData_w[i].x = timedData[i].x;
                    timedAttrData_w[i].y = timedData[i].y;
                    timedAttrData_w[i].time = timedData[i].time;
                    if (writable == AttrWriteType._WRITE) {
                        timedAttrData_r[i].value = fl_ptr_write;
                        timedAttrData_w[i].value = fl_ptr_read;
                    } else {
                        timedAttrData_r[i].value = fl_ptr_read;
                        timedAttrData_w[i].value = fl_ptr_write;
                    }
                    break;
                case TangoConst.Tango_DEV_STATE:
                case TangoConst.Tango_DEV_ULONG:
                case TangoConst.Tango_DEV_LONG:
                    final Integer[] lg_ptr_init = (Integer[]) timedData[i].value;
                    Integer[] lg_ptr_read = null;
                    Integer[] lg_ptr_write = null;
                    if (lg_ptr_init != null) {
                        if (dataFormat == AttrDataFormat._IMAGE) {
                            lg_ptr_read = new Integer[timedData[i].x * timedData[i].y];
                            dimWrite = lg_ptr_init.length - timedData[i].x * timedData[i].y;
                        } else {
                            lg_ptr_read = new Integer[timedData[i].x];
                            dimWrite = lg_ptr_init.length - timedData[i].x;
                            if (dimWrite > dbData_w.getMax_x()) {
                                dbData_w.setMax_x(dimWrite);
                            }
                        }
                        if (dimWrite < 0) {
                            dimWrite = 0;
                        }
                        lg_ptr_write = new Integer[dimWrite];
                        if (dataFormat == AttrDataFormat._IMAGE) {
                            for (int j = 0; j < timedData[i].x * timedData[i].y; j++) {
                                lg_ptr_read[j] = lg_ptr_init[j];
                            }
                            for (int j = timedData[i].x * timedData[i].y; j < lg_ptr_init.length; j++) {
                                lg_ptr_write[j - timedData[i].x] = lg_ptr_init[j];
                            }
                        } else {
                            for (int j = 0; j < timedData[i].x; j++) {
                                lg_ptr_read[j] = lg_ptr_init[j];
                            }
                            for (int j = timedData[i].x; j < lg_ptr_init.length; j++) {
                                lg_ptr_write[j - timedData[i].x] = lg_ptr_init[j];
                            }
                        }
                    }
                    timedAttrData_r[i] = new NullableTimedData();
                    timedAttrData_r[i].data_type = data_type;
                    timedAttrData_r[i].x = timedData[i].x;
                    timedAttrData_r[i].y = timedData[i].y;
                    timedAttrData_r[i].time = timedData[i].time;
                    timedAttrData_w[i] = new NullableTimedData();
                    timedAttrData_w[i].data_type = data_type;
                    timedAttrData_w[i].x = timedData[i].x;
                    timedAttrData_w[i].y = timedData[i].y;
                    timedAttrData_w[i].time = timedData[i].time;
                    if (writable == AttrWriteType._WRITE) {
                        timedAttrData_r[i].value = lg_ptr_write;
                        timedAttrData_w[i].value = lg_ptr_read;
                    } else {
                        timedAttrData_r[i].value = lg_ptr_read;
                        timedAttrData_w[i].value = lg_ptr_write;
                    }
                    break;
                case TangoConst.Tango_DEV_LONG64:
                case TangoConst.Tango_DEV_ULONG64:
                    final Long[] lg_ptr_init2 = (Long[]) timedData[i].value;
                    Long[] lg_ptr_read2 = null;
                    Long[] lg_ptr_write2 = null;
                    if (lg_ptr_init2 != null) {
                        if (dataFormat == AttrDataFormat._IMAGE) {
                            lg_ptr_read2 = new Long[timedData[i].x * timedData[i].y];
                            dimWrite = lg_ptr_init2.length - timedData[i].x * timedData[i].y;
                        } else {
                            lg_ptr_read2 = new Long[timedData[i].x];
                            dimWrite = lg_ptr_init2.length - timedData[i].x;
                            if (dimWrite > dbData_w.getMax_x()) {
                                dbData_w.setMax_x(dimWrite);
                            }
                        }
                        if (dimWrite < 0) {
                            dimWrite = 0;
                        }
                        lg_ptr_write2 = new Long[dimWrite];
                        if (dataFormat == AttrDataFormat._IMAGE) {
                            for (int j = 0; j < timedData[i].x * timedData[i].y; j++) {
                                lg_ptr_read2[j] = lg_ptr_init2[j];
                            }
                            for (int j = timedData[i].x * timedData[i].y; j < lg_ptr_init2.length; j++) {
                                lg_ptr_write2[j - timedData[i].x] = lg_ptr_init2[j];
                            }
                        } else {
                            for (int j = 0; j < timedData[i].x; j++) {
                                lg_ptr_read2[j] = lg_ptr_init2[j];
                            }
                            for (int j = timedData[i].x; j < lg_ptr_init2.length; j++) {
                                lg_ptr_write2[j - timedData[i].x] = lg_ptr_init2[j];
                            }
                        }
                    }
                    timedAttrData_r[i] = new NullableTimedData();
                    timedAttrData_r[i].data_type = data_type;
                    timedAttrData_r[i].x = timedData[i].x;
                    timedAttrData_r[i].y = timedData[i].y;
                    timedAttrData_r[i].time = timedData[i].time;
                    timedAttrData_w[i] = new NullableTimedData();
                    timedAttrData_w[i].data_type = data_type;
                    timedAttrData_w[i].x = timedData[i].x;
                    timedAttrData_w[i].y = timedData[i].y;
                    timedAttrData_w[i].time = timedData[i].time;
                    if (writable == AttrWriteType._WRITE) {
                        timedAttrData_r[i].value = lg_ptr_write2;
                        timedAttrData_w[i].value = lg_ptr_read2;
                    } else {
                        timedAttrData_r[i].value = lg_ptr_read2;
                        timedAttrData_w[i].value = lg_ptr_write2;
                    }
                    break;
                case TangoConst.Tango_DEV_BOOLEAN:
                    final Boolean[] bool_ptr_init = (Boolean[]) timedData[i].value;
                    Boolean[] bool_ptr_read = null;
                    Boolean[] bool_ptr_write = null;
                    if (bool_ptr_init != null) {
                        if (dataFormat == AttrDataFormat._IMAGE) {
                            bool_ptr_read = new Boolean[timedData[i].x * timedData[i].y];
                            dimWrite = bool_ptr_init.length - timedData[i].x * timedData[i].y;
                        } else {
                            bool_ptr_read = new Boolean[timedData[i].x];
                            dimWrite = bool_ptr_init.length - timedData[i].x;
                            if (dimWrite > dbData_w.getMax_x()) {
                                dbData_w.setMax_x(dimWrite);
                            }
                        }
                        if (dimWrite < 0) {
                            dimWrite = 0;
                        }
                        bool_ptr_write = new Boolean[dimWrite];
                        if (dataFormat == AttrDataFormat._IMAGE) {
                            for (int j = 0; j < timedData[i].x * timedData[i].y; j++) {
                                bool_ptr_read[j] = bool_ptr_init[j];
                            }
                            for (int j = timedData[i].x * timedData[i].y; j < bool_ptr_init.length; j++) {
                                bool_ptr_write[j - timedData[i].x] = bool_ptr_init[j];
                            }
                        } else {
                            for (int j = 0; j < timedData[i].x; j++) {
                                bool_ptr_read[j] = bool_ptr_init[j];
                            }
                            for (int j = timedData[i].x; j < bool_ptr_init.length; j++) {
                                bool_ptr_write[j - timedData[i].x] = bool_ptr_init[j];
                            }
                        }
                    }
                    timedAttrData_r[i] = new NullableTimedData();
                    timedAttrData_r[i].data_type = data_type;
                    timedAttrData_r[i].x = timedData[i].x;
                    timedAttrData_r[i].y = timedData[i].y;
                    timedAttrData_r[i].time = timedData[i].time;
                    timedAttrData_w[i] = new NullableTimedData();
                    timedAttrData_w[i].data_type = data_type;
                    timedAttrData_w[i].x = timedData[i].x;
                    timedAttrData_w[i].y = timedData[i].y;
                    timedAttrData_w[i].time = timedData[i].time;
                    if (writable == AttrWriteType._WRITE) {
                        timedAttrData_r[i].value = bool_ptr_write;
                        timedAttrData_w[i].value = bool_ptr_read;
                    } else {
                        timedAttrData_r[i].value = bool_ptr_read;
                        timedAttrData_w[i].value = bool_ptr_write;
                    }
                    break;
                case TangoConst.Tango_DEV_STRING:
                    final String[] str_ptr_init = (String[]) timedData[i].value;
                    String[] str_ptr_read = null;
                    String[] str_ptr_write = null;
                    if (str_ptr_init != null) {
                        if (dataFormat == AttrDataFormat._IMAGE) {
                            str_ptr_read = new String[timedData[i].x * timedData[i].y];
                            dimWrite = str_ptr_init.length - timedData[i].x * timedData[i].y;
                        } else {
                            str_ptr_read = new String[timedData[i].x];
                            dimWrite = str_ptr_init.length - timedData[i].x;
                            if (dimWrite > dbData_w.getMax_x()) {
                                dbData_w.setMax_x(dimWrite);
                            }
                        }
                        if (dimWrite < 0) {
                            dimWrite = 0;
                        }
                        str_ptr_write = new String[dimWrite];
                        if (dataFormat == AttrDataFormat._IMAGE) {
                            for (int j = 0; j < timedData[i].x * timedData[i].y; j++) {
                                str_ptr_read[j] = str_ptr_init[j];
                            }
                            for (int j = timedData[i].x * timedData[i].y; j < str_ptr_init.length; j++) {
                                str_ptr_write[j - timedData[i].x] = str_ptr_init[j];
                            }
                        } else {
                            for (int j = 0; j < timedData[i].x; j++) {
                                if (j < str_ptr_init.length) {
                                    str_ptr_read[j] = str_ptr_init[j];
                                } else {
                                    str_ptr_read[j] = "";
                                }
                            }
                            for (int j = str_ptr_read.length; j < str_ptr_init.length; j++) {
                                str_ptr_write[j - str_ptr_read.length] = str_ptr_init[j];
                            }
                        }
                    }
                    timedAttrData_r[i] = new NullableTimedData();
                    timedAttrData_r[i].data_type = data_type;
                    timedAttrData_r[i].x = timedData[i].x;
                    timedAttrData_r[i].y = timedData[i].y;
                    timedAttrData_r[i].time = timedData[i].time;
                    timedAttrData_w[i] = new NullableTimedData();
                    timedAttrData_w[i].data_type = data_type;
                    timedAttrData_w[i].x = timedData[i].x;
                    timedAttrData_w[i].y = timedData[i].y;
                    timedAttrData_w[i].time = timedData[i].time;
                    if (writable == AttrWriteType._WRITE) {
                        timedAttrData_r[i].value = str_ptr_write;
                        timedAttrData_w[i].value = str_ptr_read;
                    } else {
                        timedAttrData_r[i].value = str_ptr_read;
                        timedAttrData_w[i].value = str_ptr_write;
                    }
                    break;
            }
        }
        dbData_r.setData_timed(timedAttrData_r);
        dbData_w.setData_timed(timedAttrData_w);
        switch(writable) {
            case AttrWriteType._READ:
                argout[0] = dbData_r;
                argout[1] = null;
                break;
            case AttrWriteType._WRITE:
                argout[0] = null;
                argout[1] = dbData_w;
                break;
            default:
                argout[0] = dbData_r;
                argout[1] = dbData_w;
        }
        return argout;
    }
