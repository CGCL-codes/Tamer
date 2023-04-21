    @Override
    public byte getIOPortByte(int portAddress) throws ModuleException, UnknownPortException, WriteOnlyPortException {
        ModuleCPU cpu = (ModuleCPU) super.getConnection(Module.Type.CPU);
        byte returnValue = 0;
        if ((portAddress >= 0x3B0) && (portAddress <= 0x3BF) && (videocard.miscOutputRegister.ioAddressSelect != 0)) {
            return (byte) (0xFF);
        }
        if ((portAddress >= 0x3D0) && (portAddress <= 0x3DF) && (videocard.miscOutputRegister.ioAddressSelect == 0)) {
            return (byte) (0xFF);
        }
        switch(portAddress) {
            case 0x3BA:
            case 0x3CA:
            case 0x3DA:
                long microSeconds;
                short vertResolution;
                videocard.attributeController.dataAddressFlipFlop = false;
                videocard.displayDisabled = 0;
                microSeconds = (long) ((((double) cpu.getCurrentInstructionNumber() / cpu.getIPS())) * 1000000);
                switch((videocard.miscOutputRegister.verticalSyncPol << 1) | videocard.miscOutputRegister.horizontalSyncPol) {
                    case 0:
                        vertResolution = 200;
                        break;
                    case 1:
                        vertResolution = 400;
                        break;
                    case 2:
                        vertResolution = 350;
                        break;
                    default:
                        vertResolution = 480;
                        break;
                }
                if ((microSeconds % 13888) < 70) {
                    videocard.vertRetrace = 1;
                    videocard.displayDisabled = 1;
                } else {
                    videocard.vertRetrace = 0;
                }
                if ((microSeconds % (13888 / vertResolution)) == 0) {
                    videocard.horizRetrace = 1;
                    videocard.displayDisabled = 1;
                } else {
                    videocard.horizRetrace = 0;
                }
                return (byte) (videocard.vertRetrace << 3 | videocard.displayDisabled);
            case 0x3C0:
                if (!videocard.attributeController.dataAddressFlipFlop) {
                    return (byte) ((videocard.attributeController.paletteAddressSource << 5) | videocard.attributeController.index);
                } else {
                    logger.log(Level.SEVERE, "[" + super.getType() + "]" + " Port [0x3C0] read, but flipflop not set to address mode");
                    return 0;
                }
            case 0x3C1:
                switch(videocard.attributeController.index) {
                    case 0x00:
                    case 0x01:
                    case 0x02:
                    case 0x03:
                    case 0x04:
                    case 0x05:
                    case 0x06:
                    case 0x07:
                    case 0x08:
                    case 0x09:
                    case 0x0A:
                    case 0x0B:
                    case 0x0C:
                    case 0x0D:
                    case 0x0E:
                    case 0x0F:
                        returnValue = videocard.attributeController.paletteRegister[videocard.attributeController.index];
                        return (returnValue);
                    case 0x10:
                        return ((byte) ((videocard.attributeController.modeControlReg.graphicsEnable << 0) | (videocard.attributeController.modeControlReg.monoColourEmu << 1) | (videocard.attributeController.modeControlReg.lineGraphicsEnable << 2) | (videocard.attributeController.modeControlReg.blinkIntensity << 3) | (videocard.attributeController.modeControlReg.pixelPanningMode << 5) | (videocard.attributeController.modeControlReg.colour8Bit << 6) | (videocard.attributeController.modeControlReg.paletteBitsSelect << 7)));
                    case 0x11:
                        return (videocard.attributeController.overscanColour);
                    case 0x12:
                        return (videocard.attributeController.colourPlaneEnable);
                    case 0x13:
                        return (videocard.attributeController.horizPixelPanning);
                    case 0x14:
                        return (videocard.attributeController.colourSelect);
                    default:
                        logger.log(Level.SEVERE, "[" + super.getType() + "]" + " Port [0x3C1] reads unknown register 0x" + Integer.toHexString(videocard.attributeController.index).toUpperCase());
                        return 0;
                }
            case 0x3C2:
                logger.log(Level.INFO, "[" + super.getType() + "]" + " Port [0x3C1] reads Input Status #0; ignored");
                return 0;
            case 0x3C3:
                return (videocard.vgaEnabled) ? (byte) 1 : 0;
            case 0x3C4:
                return (videocard.sequencer.index);
            case 0x3C5:
                switch(videocard.sequencer.index) {
                    case 0:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + " Port [0x3C5] reads sequencer reset");
                        return (byte) (videocard.sequencer.aSynchReset | (videocard.sequencer.synchReset << 1));
                    case 1:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + " Port [0x3C5] reads sequencer clocking mode");
                        return (videocard.sequencer.clockingMode);
                    case 2:
                        return (videocard.sequencer.mapMask);
                    case 3:
                        return (videocard.sequencer.characterMapSelect);
                    case 4:
                        return ((byte) ((videocard.sequencer.extendedMemory << 1) | (videocard.sequencer.oddEvenDisable << 2) | (videocard.sequencer.chainFourEnable << 3)));
                    default:
                        logger.log(Level.SEVERE, "[" + super.getType() + "]" + " Port [0x3C5] reads unknown register 0x" + Integer.toHexString(videocard.sequencer.index).toUpperCase());
                        return 0;
                }
            case 0x3C6:
                return (videocard.colourRegister.pixelMask);
            case 0x3C7:
                return (videocard.colourRegister.dacState);
            case 0x3C8:
                return (videocard.colourRegister.dacWriteAddress);
            case 0x3C9:
                if (videocard.colourRegister.dacState == 0x03) {
                    switch(videocard.colourRegister.dacReadCounter) {
                        case 0:
                            returnValue = videocard.pixels[((int) videocard.colourRegister.dacReadAddress) & 0xFF].red;
                            break;
                        case 1:
                            returnValue = videocard.pixels[((int) videocard.colourRegister.dacReadAddress) & 0xFF].green;
                            break;
                        case 2:
                            returnValue = videocard.pixels[((int) videocard.colourRegister.dacReadAddress) & 0xFF].blue;
                            break;
                        default:
                            returnValue = 0;
                    }
                    videocard.colourRegister.dacReadCounter++;
                    if (videocard.colourRegister.dacReadCounter >= 3) {
                        videocard.colourRegister.dacReadCounter = 0;
                        videocard.colourRegister.dacReadAddress++;
                    }
                } else {
                    returnValue = 0x3F;
                }
                return (returnValue);
            case 0x3CC:
                return ((byte) (((videocard.miscOutputRegister.ioAddressSelect & 0x01) << 0) | ((videocard.miscOutputRegister.ramEnable & 0x01) << 1) | ((videocard.miscOutputRegister.clockSelect & 0x03) << 2) | ((videocard.miscOutputRegister.lowHighPage & 0x01) << 5) | ((videocard.miscOutputRegister.horizontalSyncPol & 0x01) << 6) | ((videocard.miscOutputRegister.verticalSyncPol & 0x01) << 7)));
            case 0x3CD:
                logger.log(Level.INFO, "[" + super.getType() + "]" + " Port [0x3CD] read; unknown register, returned 0x00");
                return 0x00;
            case 0x3CE:
                return (byte) (videocard.graphicsController.index);
            case 0x3CF:
                switch(videocard.graphicsController.index) {
                    case 0:
                        return (videocard.graphicsController.setReset);
                    case 1:
                        return (videocard.graphicsController.enableSetReset);
                    case 2:
                        return (videocard.graphicsController.colourCompare);
                    case 3:
                        return ((byte) (((videocard.graphicsController.dataOperation & 0x03) << 3) | ((videocard.graphicsController.dataRotate & 0x07) << 0)));
                    case 4:
                        return (videocard.graphicsController.readMapSelect);
                    case 5:
                        returnValue = (byte) (((videocard.graphicsController.shift256Reg & 0x03) << 5) | ((videocard.graphicsController.hostOddEvenEnable & 0x01) << 4) | ((videocard.graphicsController.readMode & 0x01) << 3) | ((videocard.graphicsController.writeMode & 0x03) << 0));
                        if (videocard.graphicsController.hostOddEvenEnable != 0 || videocard.graphicsController.shift256Reg != 0) logger.log(Level.INFO, "[" + super.getType() + "]" + " io read 0x3cf: reg 05 = " + returnValue);
                        return (returnValue);
                    case 6:
                        return ((byte) (((videocard.graphicsController.memoryMapSelect & 0x03) << 2) | ((videocard.graphicsController.hostOddEvenEnable & 0x01) << 1) | ((videocard.graphicsController.alphaNumDisable & 0x01) << 0)));
                    case 7:
                        return (videocard.graphicsController.colourDontCare);
                    case 8:
                        return (videocard.graphicsController.bitMask);
                    default:
                        logger.log(Level.SEVERE, "[" + super.getType() + "]" + " Port [0x3CF] reads unknown register 0x" + Integer.toHexString(videocard.graphicsController.index).toUpperCase());
                        return (0);
                }
            case 0x3D4:
                return (videocard.crtControllerRegister.index);
            case 0x3B5:
            case 0x3D5:
                if (videocard.crtControllerRegister.index > 0x18) {
                    logger.log(Level.INFO, "[" + super.getType() + "]" + " Port [0x" + Integer.toHexString(portAddress).toUpperCase() + "] reads unknown register 0x" + Integer.toHexString(videocard.crtControllerRegister.index).toUpperCase());
                    return (0);
                }
                return videocard.crtControllerRegister.regArray[videocard.crtControllerRegister.index];
            case 0x3B4:
            case 0x3CB:
            default:
                logger.log(Level.INFO, "[" + super.getType() + "]" + " Port [0x" + Integer.toHexString(portAddress).toUpperCase() + "] read; unknown register, returned 0x00");
                return 0;
        }
    }
