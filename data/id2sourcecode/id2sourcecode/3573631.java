    public boolean handleSensorSpecificData(SensorMetaData smd, StructureInputStream sis) {
        try {
            int dataLen = sis.readUnsignedShort();
            Debugger.debug(Debugger.TRACE, "Sis:dataLen=" + dataLen);
            long ts = sis.readLong();
            Debugger.debug(Debugger.TRACE, "Sis:time=" + ts);
            Vector<Integer> datatypes = smd.getChannelDatatypes();
            for (int i = 0; i < smd.getNumChannels(); i++) {
                int dt = ((Integer) datatypes.elementAt(i)).intValue();
                switch(dt) {
                    case Constants.DATATYPE_DOUBLE:
                        double val[] = new double[1];
                        if (i != 0) {
                            val[0] = sis.readDouble();
                        } else {
                            val[0] = (double) ts;
                        }
                        smd.getSource().insertData(i, (Object) val);
                        Debugger.debug(Debugger.TRACE, "Channel[" + i + "]=" + val[0]);
                        break;
                    default:
                        throw new IllegalStateException("Currently, support is " + "provided only to double data type");
                }
            }
            smd.getSource().flush();
            return true;
        } catch (EOFException eof) {
            eof.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }
