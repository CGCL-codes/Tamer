    private NceReply accessoryCommand(NceMessage m, NceReply reply) {
        if (m.getElement(3) == 0x03 || m.getElement(3) == 0x04) {
            String operation = "close";
            if (m.getElement(3) == 0x04) operation = "throw";
            int nceAccessoryAddress = getNceAddress(m);
            log.debug("Accessory command " + operation + " NT" + nceAccessoryAddress);
            if (nceAccessoryAddress > 2044) {
                log.error("Turnout address greater than 2044, address: " + nceAccessoryAddress);
                return null;
            }
            int bit = (nceAccessoryAddress - 1) & 0x07;
            int setMask = 0x01;
            for (int i = 0; i < bit; i++) {
                setMask = setMask << 1;
            }
            int clearMask = 0x0FFF - setMask;
            int offset = (nceAccessoryAddress - 1) >> 3;
            int read = turnoutMemory[offset];
            byte write = (byte) (read & clearMask & 0xFF);
            if (operation.equals("close")) write = (byte) (write + setMask);
            turnoutMemory[offset] = write;
        }
        reply.setElement(0, NCE_OKAY);
        return reply;
    }
