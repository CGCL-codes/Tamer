    private void executeCommand() {
        int drive, hds, cylinder, sector, eot;
        int sectorSize, sectorTime, logicalSector, dataLength;
        boolean ableToTransfer;
        commandPending = command[0] & 0xFF;
        switch(commandPending) {
            case 0x03:
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " CMD: specify");
                srt = (byte) ((command[1] >> 4) & 0x0F);
                hut = (byte) (command[1] & 0x0F);
                hlt = (byte) ((command[2] >> 1) & 0x7F);
                nonDMA = (byte) (command[2] & 0x01);
                this.enterIdlePhase();
                break;
            case 0x04:
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " CMD: sense drive status");
                drive = (command[1] & 0x03);
                drives[drive].hds = (command[1] >> 2) & 0x01;
                statusRegister3 = (byte) (0x28 | (drives[drive].hds << 2) | drive);
                statusRegister3 |= (drives[drive].writeProtected ? 0x40 : 0x00);
                if (drives[drive].cylinder == 0) {
                    statusRegister3 |= 0x10;
                }
                this.enterResultPhase();
                break;
            case 0x45:
            case 0x46:
            case 0x66:
            case 0xC5:
            case 0xC6:
            case 0xE6:
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " CMD: Read/write data");
                emu.statusChanged(Emulator.MODULE_FDC_TRANSFER_START);
                if ((dor & 0x08) == 0) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: read/write normal data -> DMA is disabled");
                }
                drive = command[1] & 0x03;
                dor &= 0xFC;
                dor |= drive;
                drives[drive].multiTrack = (((command[0] >> 7) & 0x01) == 0x01 ? true : false);
                cylinder = command[2];
                hds = command[3] & 0x01;
                sector = command[4];
                sectorSize = command[5];
                eot = command[6];
                dataLength = command[8];
                ableToTransfer = true;
                if (!(drives[drive].isMotorRunning())) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: read/write normal data -> drive motor of drive " + drive + " is not running.");
                    msr = FDC_CMD_BUSY;
                    ableToTransfer = false;
                }
                if (hds != ((command[1] >> 2) & 0x01)) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "] head number in command[1] doesn't match head field");
                    ableToTransfer = false;
                    statusRegister0 = 0x40 | (drives[drive].hds << 2) | drive;
                    statusRegister1 = 0x04;
                    statusRegister2 = 0x00;
                    enterResultPhase();
                }
                if (drives[drive].getDriveType() == FLOPPY_DRIVETYPE_NONE) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: read/write normal data -> incorrect drive type if drive " + drive + ".");
                    msr = FDC_CMD_BUSY;
                    ableToTransfer = false;
                }
                if (!drives[drive].containsFloppy()) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: read/write normal data -> floppy is not inserted in drive " + drive + ".");
                    msr = FDC_CMD_BUSY;
                    ableToTransfer = false;
                }
                if (sectorSize != 0x02) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: read/write normal data -> sector size (bytes per sector) not supported.");
                    ableToTransfer = false;
                }
                if (cylinder != drives[drive].cylinder) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: read/write normal data -> requested cylinder differs from selected cylinder on drive. Will proceed.");
                    drives[drive].resetChangeline();
                }
                if (cylinder >= drives[drive].tracks) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: read/write normal data -> cylinder number exceeds maximum number of tracks.");
                    ableToTransfer = false;
                }
                if (sector > drives[drive].sectorsPerTrack) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: read/write normal data -> sector number exceeds sectors per track.");
                    ableToTransfer = false;
                }
                logicalSector = (cylinder * drives[drive].heads * drives[drive].sectorsPerTrack) + (hds * drives[drive].sectorsPerTrack) + (sector - 1);
                if (logicalSector >= drives[drive].sectors) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: read/write normal data -> logical sectors exceeds total number of sectors on disk.");
                    ableToTransfer = false;
                }
                if (ableToTransfer == true) {
                    if (eot == 0) {
                        eot = drives[drive].sectorsPerTrack;
                    }
                    drives[drive].cylinder = cylinder;
                    drives[drive].hds = hds;
                    drives[drive].sector = sector;
                    drives[drive].eot = eot;
                    if ((command[0] & 0x4F) == 0x46) {
                        try {
                            drives[drive].readData(logicalSector * 512, 512, floppyBuffer);
                        } catch (StorageDeviceException e) {
                            logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " " + e.getMessage());
                        }
                        msr = FDC_CMD_BUSY;
                        sectorTime = 200000 / drives[drive].sectorsPerTrack;
                        motherboard.resetTimer(this, sectorTime);
                        motherboard.setTimerActiveState(this, true);
                    } else if ((command[0] & 0x7F) == 0x45) {
                        msr = FDC_CMD_BUSY;
                        dma.setDMARequest(FDC_DMA_CHANNEL, true);
                    } else {
                        logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: unknown read/write command");
                    }
                } else {
                    logger.log(Level.SEVERE, "[" + MODULE_TYPE + "]" + " CMD: not able to transfer data");
                }
                break;
            case 0x07:
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " CMD: recalibrate drive");
                drive = (command[1] & 0x03);
                dor &= 0xFC;
                dor |= drive;
                motherboard.resetTimer(this, calculateStepDelay(drive, 0));
                motherboard.setTimerActiveState(this, true);
                drives[drive].cylinder = 0;
                msr = (byte) (1 << drive);
                break;
            case 0x08:
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " CMD: sense interrupt status");
                if (resetSenseInterrupt > 0) {
                    drive = 4 - resetSenseInterrupt;
                    statusRegister0 &= 0xF8;
                    statusRegister0 |= (drives[drive].hds << 2) | drive;
                    resetSenseInterrupt--;
                } else if (!pendingIRQ) {
                    statusRegister0 = 0x80;
                }
                this.enterResultPhase();
                break;
            case 0x4A:
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " CMD: read ID");
                drive = command[1] & 0x03;
                drives[drive].hds = (command[1] >> 2) & 0x01;
                dor &= 0xFC;
                dor |= drive;
                if (drives[drive].isMotorRunning()) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: read ID -> drive motor is not running.");
                    msr = FDC_CMD_BUSY;
                    return;
                }
                if (drives[drive].getDriveType() == FLOPPY_DRIVETYPE_NONE) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: read ID -> incorrect drive type.");
                    msr = FDC_CMD_BUSY;
                    return;
                }
                if (!drives[drive].containsFloppy()) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: read ID -> floppy is not inserted.");
                    msr = FDC_CMD_BUSY;
                    return;
                }
                statusRegister0 = (drives[drive].hds << 2) | drive;
                sectorTime = 200000 / drives[drive].sectorsPerTrack;
                motherboard.resetTimer(this, sectorTime);
                motherboard.setTimerActiveState(this, true);
                msr = FDC_CMD_BUSY;
                this.enterResultPhase();
                break;
            case 0x4D:
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " CMD: format track");
                drive = command[1] & 0x03;
                dor &= 0xFC;
                dor |= drive;
                if (drives[drive].isMotorRunning()) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: format track -> drive motor is not running.");
                    msr = FDC_CMD_BUSY;
                    return;
                }
                drives[drive].hds = (command[1] >> 2) & 0x01;
                if (drives[drive].getDriveType() == FLOPPY_DRIVETYPE_NONE) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: format track -> incorrect drive type.");
                    msr = FDC_CMD_BUSY;
                    return;
                }
                if (!drives[drive].containsFloppy()) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: format track -> floppy is not inserted.");
                    msr = FDC_CMD_BUSY;
                    return;
                }
                sectorSize = command[2];
                formatCount = command[3];
                formatFillbyte = command[5];
                if (sectorSize != 0x02) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: format track -> sector size (bytes per sector) not supported.");
                }
                if (formatCount != drives[drive].sectorsPerTrack) {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " CMD: format track -> wrong number of sectors per track encountered.");
                }
                if (drives[drive].writeProtected) {
                    logger.log(Level.SEVERE, "[" + MODULE_TYPE + "]" + " CMD: format track -> floppy is write protected.");
                    statusRegister0 = 0x40 | (drives[drive].hds << 2) | drive;
                    statusRegister1 = 0x27;
                    statusRegister2 = 0x31;
                    this.enterResultPhase();
                    return;
                }
                formatCount = formatCount * 4;
                dma.setDMARequest(FDC.FDC_DMA_CHANNEL, true);
                msr = FDC_CMD_BUSY;
                break;
            case 0x0F:
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " CMD: seek");
                drive = command[1] & 0x03;
                dor &= 0xFC;
                dor |= drive;
                drives[drive].hds = (command[1] >> 2) & 0x01;
                motherboard.resetTimer(this, calculateStepDelay(drive, command[2]));
                motherboard.setTimerActiveState(this, true);
                drives[drive].cylinder = command[2];
                msr = (byte) (1 << drive);
                break;
            case 0x0E:
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " CMD: dump registers (EHD)");
                this.enterResultPhase();
                break;
            case 0x12:
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " CMD: perpendicular mode (EHD)");
                perpMode = command[1];
                this.enterIdlePhase();
                break;
            case 0x13:
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " CMD: configure (EHD)");
                config = command[2];
                preTrack = command[3];
                this.enterIdlePhase();
                break;
            default:
                logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " Unsupported FDC command 0x" + command[0]);
        }
    }
