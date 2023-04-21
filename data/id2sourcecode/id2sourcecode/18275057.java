    public void handleSensorSpecificData(SensorMetaData smd, StructureInputStream sis) {
        try {
            int dataLen = sis.readUnsignedShort();
            Debugger.debug(Debugger.TRACE, "Sis:dataLen=" + dataLen);
            Vector<Integer> datatypes = smd.getChannelDatatypes();
            for (int i = 0; i < smd.getNumChannels(); i++) {
                int dt = ((Integer) datatypes.elementAt(i)).intValue();
                switch(dt) {
                    case Constants.DATATYPE_DOUBLE:
                        double val[] = new double[1];
                        val[0] = sis.readDouble();
                        smd.getSource().insertData(i, (Object) val);
                        Debugger.debug(Debugger.TRACE, "Channel[" + i + "]=" + val[0]);
                        break;
                    default:
                        throw new IllegalStateException("Currently, support is " + "provided only to double data type");
                }
            }
            smd.getSource().flush();
        } catch (EOFException eof) {
            eof.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
