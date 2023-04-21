        public void saveState(DataOutput output) throws IOException {
            output.writeInt(lastInterruptRequestRegister);
            output.writeInt(interruptRequestRegister);
            output.writeInt(interruptMaskRegister);
            output.writeInt(interruptServiceRegister);
            output.writeInt(priorityAdd);
            output.writeInt(irqBase);
            output.writeBoolean(readRegisterSelect);
            output.writeBoolean(poll);
            output.writeBoolean(specialMask);
            output.writeInt(initState);
            output.writeBoolean(autoEOI);
            output.writeBoolean(rotateOnAutoEOI);
            output.writeBoolean(specialFullyNestedMode);
            output.writeBoolean(fourByteInit);
            output.writeInt(elcr);
            output.writeInt(elcrMask);
            output.writeInt(ioPorts.length);
            for (int i = 0; i < ioPorts.length; i++) {
                int port = ioPorts[i];
                output.writeInt(port);
            }
        }
