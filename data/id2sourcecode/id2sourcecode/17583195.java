    int getChannel(UUID protocolUUID) {
        int channel = -1;
        DataElement protocolDescriptor = getAttributeValue(BluetoothConsts.ProtocolDescriptorList);
        if ((protocolDescriptor == null) || (protocolDescriptor.getDataType() != DataElement.DATSEQ)) {
            return -1;
        }
        for (Enumeration protocolsSeqEnum = (Enumeration) protocolDescriptor.getValue(); protocolsSeqEnum.hasMoreElements(); ) {
            DataElement elementSeq = (DataElement) protocolsSeqEnum.nextElement();
            if (elementSeq.getDataType() == DataElement.DATSEQ) {
                Enumeration elementSeqEnum = (Enumeration) elementSeq.getValue();
                if (elementSeqEnum.hasMoreElements()) {
                    DataElement protocolElement = (DataElement) elementSeqEnum.nextElement();
                    if (protocolElement.getDataType() != DataElement.UUID) {
                        continue;
                    }
                    Object uuid = protocolElement.getValue();
                    if (elementSeqEnum.hasMoreElements() && (protocolUUID.equals(uuid))) {
                        DataElement protocolPSMElement = (DataElement) elementSeqEnum.nextElement();
                        switch(protocolPSMElement.getDataType()) {
                            case DataElement.U_INT_1:
                            case DataElement.U_INT_2:
                            case DataElement.U_INT_4:
                            case DataElement.INT_1:
                            case DataElement.INT_2:
                            case DataElement.INT_4:
                            case DataElement.INT_8:
                                channel = (int) protocolPSMElement.getLong();
                                break;
                        }
                    }
                }
            }
        }
        return channel;
    }
