    public void dataReceived(USART source, int data) {
        if (chipSelect) {
            if (DEBUG) {
                log("byte received: " + data);
            }
            switch(state) {
                case READ_STATUS:
                    if (DEBUG) {
                        log("Read status => " + getStatus() + " from $" + Utils.hex16(cpu.getPC()));
                    }
                    source.byteReceived(getStatus());
                    return;
                case READ_IDENT:
                    source.byteReceived(identity[pos]);
                    pos++;
                    if (pos >= identity.length) {
                        pos = 0;
                    }
                    return;
                case WRITE_STATUS:
                    status = data & STATUS_MASK;
                    source.byteReceived(0);
                    return;
                case READ_DATA:
                    if (pos < 3) {
                        readAddress = (readAddress << 8) + data;
                        source.byteReceived(0);
                        pos++;
                        if (DEBUG && pos == 3) {
                            log("reading from $" + Integer.toHexString(readAddress));
                        }
                    } else {
                        source.byteReceived(readMemory(readAddress++));
                        if (readAddress > 0xfffff) {
                            readAddress = 0;
                        }
                    }
                    return;
                case SECTOR_ERASE:
                    if (pos < 3) {
                        readAddress = (readAddress << 8) + data;
                        source.byteReceived(0);
                        pos++;
                        if (pos == 3) {
                            sectorErase(readAddress);
                        }
                    }
                    return;
                case PAGE_PROGRAM:
                    if (pos < 3) {
                        readAddress = (readAddress << 8) + data;
                        source.byteReceived(0);
                        pos++;
                        if (pos == 3) {
                            for (int i = 0; i < buffer.length; i++) {
                                buffer[i] = (byte) 0xff;
                            }
                            blockWriteAddress = readAddress & 0xfff00;
                            if (DEBUG) {
                                log("programming at $" + Integer.toHexString(readAddress));
                            }
                        }
                    } else {
                        source.byteReceived(0);
                        writeBuffer((readAddress++) & 0xff, data);
                    }
                    return;
            }
            if (DEBUG) {
                log("new command: " + data);
            }
            switch(data) {
                case WRITE_ENABLE:
                    if (DEBUG) {
                        log("Write Enable");
                    }
                    writeEnable = true;
                    break;
                case WRITE_DISABLE:
                    if (DEBUG) {
                        log("Write Disable");
                    }
                    writeEnable = false;
                    break;
                case READ_IDENT:
                    if (DEBUG) {
                        log("Read ident.");
                    }
                    state = READ_IDENT;
                    pos = 0;
                    source.byteReceived(0);
                    return;
                case READ_STATUS:
                    state = READ_STATUS;
                    source.byteReceived(0);
                    return;
                case WRITE_STATUS:
                    if (DEBUG) {
                        log("Write status");
                    }
                    state = WRITE_STATUS;
                    break;
                case READ_DATA:
                    if (DEBUG) {
                        log("Read Data");
                    }
                    state = READ_DATA;
                    pos = readAddress = 0;
                    break;
                case PAGE_PROGRAM:
                    if (DEBUG) {
                        log("Page Program");
                    }
                    state = PAGE_PROGRAM;
                    pos = readAddress = 0;
                    break;
                case SECTOR_ERASE:
                    if (DEBUG) {
                        log("Sector Erase");
                    }
                    state = SECTOR_ERASE;
                    pos = 0;
                    break;
                case BULK_ERASE:
                    log("Bulk Erase");
                    break;
            }
            source.byteReceived(0);
        }
    }
