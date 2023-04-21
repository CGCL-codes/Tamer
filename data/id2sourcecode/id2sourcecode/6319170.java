    private void executeCommand() {
        ModuleMotherboard motherboard = (ModuleMotherboard) super.getConnection(Module.Type.MOTHERBOARD);
        ModuleDMA dma = (ModuleDMA) super.getConnection(Module.Type.DMA);
        int drv, hds, cylinder, sector, eot;
        int sectorSize, sectorTime, logicalSector, dataLength;
        boolean ableToTransfer;
        commandPending = command[0] & 0xFF;
        switch(commandPending) {
            case 0x03:
                logger.log(Level.INFO, "[" + super.getType() + "]" + " CMD: specify");
                srt = (byte) ((command[1] >> 4) & 0x0F);
                hut = (byte) (command[1] & 0x0F);
                hlt = (byte) ((command[2] >> 1) & 0x7F);
                nonDMA = (byte) (command[2] & 0x01);
                this.enterIdlePhase();
                break;
            case 0x04:
                logger.log(Level.INFO, "[" + super.getType() + "]" + " CMD: sense drive status");
                drv = (command[1] & 0x03);
                drives[drv].hds = (command[1] >> 2) & 0x01;
                statusRegister3 = (byte) (0x28 | (drives[drv].hds << 2) | drv);
                statusRegister3 |= (drives[drv].writeProtected ? 0x40 : 0x00);
                if (drives[drv].cylinder == 0) {
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
                logger.log(Level.INFO, "[" + super.getType() + "]" + " CMD: Read/write data");
                emu.statusChanged(Emulator.MODULE_FDC_TRANSFER_START);
                if ((dor & 0x08) == 0) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: read/write normal data -> DMA is disabled");
                }
                drv = command[1] & 0x03;
                dor &= 0xFC;
                dor |= drv;
                drives[drv].multiTrack = (((command[0] >> 7) & 0x01) == 0x01 ? true : false);
                cylinder = command[2];
                hds = command[3] & 0x01;
                sector = command[4];
                sectorSize = command[5];
                eot = command[6];
                dataLength = command[8];
                ableToTransfer = true;
                if (!(drives[drv].isMotorRunning())) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: read/write normal data -> drive motor of drive " + drv + " is not running.");
                    msr = FDC_CMD_BUSY;
                    ableToTransfer = false;
                }
                if (drives[drv].getDriveType() == FLOPPY_DRIVETYPE_NONE) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: read/write normal data -> incorrect drive type if drive " + drv + ".");
                    msr = FDC_CMD_BUSY;
                    ableToTransfer = false;
                }
                if (hds != ((command[1] >> 2) & 0x01)) {
                    logger.log(Level.WARNING, "[" + super.getType() + "] head number in command[1] doesn't match head field");
                    ableToTransfer = false;
                    statusRegister0 = 0x40 | (drives[drv].hds << 2) | drv;
                    statusRegister1 = 0x04;
                    statusRegister2 = 0x00;
                    enterResultPhase();
                }
                if (!drives[drv].containsFloppy()) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: read/write normal data -> floppy is not inserted in drive " + drv + ".");
                    msr = FDC_CMD_BUSY;
                    ableToTransfer = false;
                }
                if (sectorSize != 0x02) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: read/write normal data -> sector size (bytes per sector) not supported.");
                    ableToTransfer = false;
                }
                if (cylinder >= drives[drv].tracks) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: read/write normal data -> cylinder number exceeds maximum number of tracks.");
                    ableToTransfer = false;
                }
                if (sector > drives[drv].sectorsPerTrack) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: read/write normal data -> sector number (" + sector + ") exceeds sectors per track (" + drives[drv].sectorsPerTrack + ").");
                    drives[drv].cylinder = cylinder;
                    drives[drv].hds = hds;
                    drives[drv].sector = sector;
                    statusRegister0 = 0x40 | (drives[drv].hds << 2) | drv;
                    statusRegister1 = 0x04;
                    statusRegister2 = 0x00;
                    enterResultPhase();
                    return;
                }
                if (cylinder != drives[drv].cylinder) {
                    logger.log(Level.CONFIG, "[" + super.getType() + "]" + " CMD: read/write normal data -> requested cylinder differs from selected cylinder on drive. Will proceed.");
                    drives[drv].resetChangeline();
                }
                logicalSector = (cylinder * drives[drv].heads * drives[drv].sectorsPerTrack) + (hds * drives[drv].sectorsPerTrack) + (sector - 1);
                logger.log(Level.CONFIG, "[" + super.getType() + "]" + " Logical sectors calculated: " + logicalSector);
                if (logicalSector >= drives[drv].sectors) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: read/write normal data -> logical sectors exceeds total number of sectors on disk.");
                    ableToTransfer = false;
                }
                if (ableToTransfer) {
                    if (eot == 0) {
                        eot = drives[drv].sectorsPerTrack;
                    }
                    drives[drv].cylinder = cylinder;
                    drives[drv].hds = hds;
                    drives[drv].sector = sector;
                    drives[drv].eot = eot;
                    if ((command[0] & 0x4F) == 0x46) {
                        try {
                            drives[drv].readData(logicalSector * 512, 512, floppyBuffer);
                        } catch (StorageDeviceException e) {
                            logger.log(Level.WARNING, "[" + super.getType() + "]" + " " + e.getMessage());
                        }
                        msr = FDC_CMD_BUSY;
                        msr |= FDC_CMD_DIO;
                        sectorTime = 200000 / drives[drv].sectorsPerTrack;
                        motherboard.resetTimer(this, sectorTime);
                        motherboard.setTimerActiveState(this, true);
                    } else if ((command[0] & 0x7F) == 0x45) {
                        msr = FDC_CMD_BUSY;
                        msr &= ~FDC_CMD_DIO;
                        if (emu.isCpu32bit()) {
                            dma32.holdDREQ(FDC_DMA_CHANNEL & 3);
                        } else {
                            dma.setDMARequest(FDC_DMA_CHANNEL, true);
                        }
                    } else {
                        msr |= FDC_CMD_DIO;
                        logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: unknown read/write command");
                    }
                } else {
                    logger.log(Level.SEVERE, "[" + super.getType() + "]" + " CMD: not able to transfer data");
                }
                break;
            case 0x07:
                logger.log(Level.INFO, "[" + super.getType() + "]" + " CMD: recalibrate drive");
                drv = (command[1] & 0x03);
                dor &= 0xFC;
                dor |= drv;
                motherboard.resetTimer(this, calculateStepDelay(drv, 0));
                motherboard.setTimerActiveState(this, true);
                drives[drv].cylinder = 0;
                msr = (byte) (1 << drv);
                break;
            case 0x08:
                logger.log(Level.INFO, "[" + super.getType() + "]" + " CMD: sense interrupt status");
                if (resetSenseInterrupt > 0) {
                    drv = 4 - resetSenseInterrupt;
                    statusRegister0 &= 0xF8;
                    statusRegister0 |= (drives[drv].hds << 2) | drv;
                    resetSenseInterrupt--;
                } else if (!pendingIRQ) {
                    statusRegister0 = 0x80;
                }
                this.enterResultPhase();
                break;
            case 0x4A:
                logger.log(Level.INFO, "[" + super.getType() + "]" + " CMD: read ID");
                drv = command[1] & 0x03;
                drives[drv].hds = (command[1] >> 2) & 0x01;
                dor &= 0xFC;
                dor |= drv;
                if (drives[drv].isMotorRunning()) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: read ID -> drive motor is not running.");
                    msr = FDC_CMD_BUSY;
                    return;
                }
                if (drives[drv].getDriveType() == FLOPPY_DRIVETYPE_NONE) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: read ID -> incorrect drive type.");
                    msr = FDC_CMD_BUSY;
                    return;
                }
                if (!drives[drv].containsFloppy()) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: read ID -> floppy is not inserted.");
                    msr = FDC_CMD_BUSY;
                    return;
                }
                statusRegister0 = (drives[drv].hds << 2) | drv;
                sectorTime = 200000 / drives[drv].sectorsPerTrack;
                motherboard.resetTimer(this, sectorTime);
                motherboard.setTimerActiveState(this, true);
                msr = FDC_CMD_BUSY;
                this.enterResultPhase();
                break;
            case 0x4D:
                logger.log(Level.INFO, "[" + super.getType() + "]" + " CMD: format track");
                drv = command[1] & 0x03;
                dor &= 0xFC;
                dor |= drv;
                if (drives[drv].isMotorRunning()) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: format track -> drive motor is not running.");
                    msr = FDC_CMD_BUSY;
                    return;
                }
                drives[drv].hds = (command[1] >> 2) & 0x01;
                if (drives[drv].getDriveType() == FLOPPY_DRIVETYPE_NONE) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: format track -> incorrect drive type.");
                    msr = FDC_CMD_BUSY;
                    return;
                }
                if (!drives[drv].containsFloppy()) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: format track -> floppy is not inserted.");
                    msr = FDC_CMD_BUSY;
                    return;
                }
                sectorSize = command[2];
                formatCount = command[3];
                formatFillbyte = command[5];
                if (sectorSize != 0x02) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: format track -> sector size (bytes per sector) not supported.");
                }
                if (formatCount != drives[drv].sectorsPerTrack) {
                    logger.log(Level.WARNING, "[" + super.getType() + "]" + " CMD: format track -> wrong number of sectors per track encountered.");
                }
                if (drives[drv].writeProtected) {
                    logger.log(Level.SEVERE, "[" + super.getType() + "]" + " CMD: format track -> floppy is write protected.");
                    statusRegister0 = 0x40 | (drives[drv].hds << 2) | drv;
                    statusRegister1 = 0x27;
                    statusRegister2 = 0x31;
                    this.enterResultPhase();
                    return;
                }
                formatCount = formatCount * 4;
                if (emu.isCpu32bit()) {
                    dma32.holdDREQ(FDC_DMA_CHANNEL & 3);
                } else {
                    dma.setDMARequest(FDC_DMA_CHANNEL, true);
                }
                msr = FDC_CMD_BUSY;
                break;
            case 0x0F:
                logger.log(Level.INFO, "[" + super.getType() + "]" + " CMD: seek");
                drv = command[1] & 0x03;
                dor &= 0xFC;
                dor |= drv;
                drives[drv].hds = (command[1] >> 2) & 0x01;
                motherboard.resetTimer(this, calculateStepDelay(drv, command[2]));
                motherboard.setTimerActiveState(this, true);
                drives[drv].cylinder = command[2];
                msr = (byte) (1 << drv);
                break;
            case 0x0E:
                logger.log(Level.INFO, "[" + super.getType() + "]" + " CMD: dump registers (EHD)");
                this.enterResultPhase();
                break;
            case 0x12:
                logger.log(Level.INFO, "[" + super.getType() + "]" + " CMD: perpendicular mode (EHD)");
                perpMode = command[1];
                this.enterIdlePhase();
                break;
            case 0x13:
                logger.log(Level.INFO, "[" + super.getType() + "]" + " CMD: configure (EHD)");
                config = command[2];
                preTrack = command[3];
                this.enterIdlePhase();
                break;
            default:
                logger.log(Level.WARNING, "[" + super.getType() + "]" + " Unsupported FDC command 0x" + command[0]);
        }
    }
