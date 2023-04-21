    private void buildExport() {
        buf = new StringBuffer();
        writeBOF(exportNetwork);
        for (UPBLinkI link : exportNetwork.getLinks()) {
            writeLink(link);
        }
        for (UPBDeviceI device : exportNetwork.getDevices()) {
            UPBProductI product = device.getProductInfo();
            writeId(device, product);
            for (int chan = 0; chan < device.getChannelCount(); chan++) {
                writeChannel(device, chan);
                switch(product.getProductKind()) {
                    case KEYPAD_K:
                        for (int comp = 0; comp < product.getTransmitComponentCount(); comp++) {
                            writeButton(device, chan, comp);
                        }
                        break;
                    case SWITCH_K:
                    case MODULE_K:
                    case INPUT_OUTPUT_K:
                        for (int comp = 0; comp < device.getReceiveComponentCount(); comp++) {
                            writePreset(device, chan, comp);
                        }
                        for (int comp = 0; comp < product.getTransmitComponentCount(); comp++) {
                            writeButton(device, chan, comp);
                        }
                        break;
                    case OTHER_K:
                    default:
                        break;
                }
            }
        }
        writeEOF();
    }
