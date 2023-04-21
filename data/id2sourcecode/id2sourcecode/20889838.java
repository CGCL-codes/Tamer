        TaskSlot(MemoryReferee parent, int dataWidth, int addrWidth, boolean readUsed, boolean writeUsed) {
            this.readUsed = readUsed;
            this.writeUsed = writeUsed;
            Exit exit = parent.getExit(Exit.DONE);
            if (readUsed) {
                goR = parent.makeDataPort();
                dataOut = exit.makeDataBus();
            }
            if (writeUsed) {
                goW = parent.makeDataPort();
                dataIn = parent.makeDataPort();
            }
            address = parent.makeDataPort();
            size = parent.makeDataPort();
            done = exit.makeDataBus();
            setSizes(dataWidth, addrWidth, LogicalMemory.SIZE_WIDTH);
        }
