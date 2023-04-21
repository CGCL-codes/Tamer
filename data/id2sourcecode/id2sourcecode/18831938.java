    void removeConnection(EmulatorConnection c) {
        connections.remove(new Long(c.getHandle()));
        if (c instanceof EmulatorRFCOMMService) {
            channels.remove(new Long(((EmulatorRFCOMMService) c).getChannel()));
        } else if (c instanceof EmulatorL2CAPService) {
            pcms.remove(new Long(((EmulatorL2CAPService) c).getPcm()));
        }
    }
