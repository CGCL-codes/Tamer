    private void startTransfer(int direction) {
        currentDrive = fifo[1] & 1;
        FloppyDrive drive = getCurrentDrive();
        byte kt = fifo[2];
        byte kh = fifo[3];
        byte ks = fifo[4];
        boolean didSeek = false;
        switch(drive.seek(0xff & kh, 0xff & kt, 0xff & ks, drive.sectorCount)) {
            case 2:
                stopTransfer((byte) 0x40, (byte) 0x00, (byte) 0x00);
                fifo[3] = kt;
                fifo[4] = kh;
                fifo[5] = ks;
                return;
            case 3:
                stopTransfer((byte) 0x40, (byte) 0x80, (byte) 0x00);
                fifo[3] = kt;
                fifo[4] = kh;
                fifo[5] = ks;
                return;
            case 4:
                stopTransfer((byte) 0x40, (byte) 0x00, (byte) 0x00);
                fifo[3] = kt;
                fifo[4] = kh;
                fifo[5] = ks;
                return;
            case 1:
                didSeek = true;
                break;
            default:
                break;
        }
        dataDirection = direction;
        dataOffset = 0;
        dataState = (dataState & ~STATE_STATE) | STATE_DATA;
        if ((fifo[0] & 0x80) != 0) dataState |= STATE_MULTI; else dataState &= ~STATE_MULTI;
        if (didSeek) dataState |= STATE_SEEK; else dataState &= ~STATE_SEEK;
        if (fifo[5] == 0x00) dataLength = fifo[8]; else {
            dataLength = 128 << fifo[5];
            int temp = drive.sectorCount - ks + 1;
            if ((fifo[0] & 0x80) != 0) temp += drive.sectorCount;
            dataLength *= temp;
        }
        eot = fifo[6];
        if (dmaEnabled) {
            int dmaMode = dma.getChannelMode(DMA_CHANNEL & 3);
            dmaMode = (dmaMode >>> 2) & 3;
            if (((direction == DIRECTION_SCANE || direction == DIRECTION_SCANL || direction == DIRECTION_SCANH) && dmaMode == 0) || (direction == DIRECTION_WRITE && dmaMode == 2) || (direction == DIRECTION_READ && dmaMode == 1)) {
                state |= CONTROL_BUSY;
                dma.holdDmaRequest(DMA_CHANNEL & 3);
                return;
            } else LOGGING.log(Level.INFO, "DMA mode {0,number,integer}, direction {1,number,integer}", new Object[] { JInteger.valueOf(dmaMode), JInteger.valueOf(direction) });
        }
        raiseIRQ(0x00);
    }
