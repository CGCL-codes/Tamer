        public void saveState(DataOutput output) throws IOException {
            output.writeInt(countValue);
            output.writeInt(outputLatch);
            output.writeInt(inputLatch);
            output.writeInt(countLatched);
            output.writeBoolean(statusLatched);
            output.writeBoolean(gate);
            output.writeInt(status);
            output.writeInt(readState);
            output.writeInt(writeState);
            output.writeInt(rwMode);
            output.writeInt(mode);
            output.writeInt(bcd);
            output.writeLong(countStartTime);
            output.writeLong(nextTransitionTimeValue);
            if (irqTimer == null) {
                output.writeInt(0);
            } else {
                output.writeInt(1);
                irqTimer.saveState(output);
            }
        }
