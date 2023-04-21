        public void loadState(DataInput input) throws IOException {
            countValue = input.readInt();
            outputLatch = input.readInt();
            inputLatch = input.readInt();
            countLatched = input.readInt();
            statusLatched = input.readBoolean();
            gate = input.readBoolean();
            status = input.readInt();
            readState = input.readInt();
            writeState = input.readInt();
            rwMode = input.readInt();
            mode = input.readInt();
            bcd = input.readInt();
            countStartTime = input.readLong();
            nextTransitionTimeValue = input.readLong();
            int test = input.readInt();
            if (test == 1) {
                irqTimer = timingSource.newTimer(this);
                irqTimer.loadState(input);
            }
        }
