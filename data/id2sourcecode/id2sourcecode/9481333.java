    @Override
    protected void encodeModeParameters(DataOutputStream output) {
        try {
            int b = 0;
            if (this.AWRE) {
                b |= 0x80;
            }
            if (this.ARRE) {
                b |= 0x40;
            }
            if (this.TB) {
                b |= 0x20;
            }
            if (this.RC) {
                b |= 0x10;
            }
            if (this.EER) {
                b |= 0x08;
            }
            if (this.PER) {
                b |= 0x04;
            }
            if (this.DTE) {
                b |= 0x02;
            }
            if (this.DCR) {
                b |= 0x01;
            }
            output.writeByte(b);
            output.writeByte(this.readRetryCount);
            output.writeByte(0);
            output.writeByte(0);
            output.writeByte(0);
            output.writeByte(0);
            output.write(this.writeRetryCount);
            output.writeByte(0);
            output.writeShort(this.recoveryTimeLimit);
        } catch (IOException e) {
            throw new RuntimeException("Unable to encode CDB.");
        }
    }
