    @Override
    public void setIOPortByte(int portAddress, byte value) throws UnknownPortException {
        ModuleMotherboard motherboard = (ModuleMotherboard) super.getConnection(Module.Type.MOTHERBOARD);
        logger.log(Level.CONFIG, "[" + super.getType() + "]" + " Port 0x" + Integer.toHexString(portAddress).toUpperCase() + " received write of 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase());
        switch(portAddress) {
            case 0x60:
                if (keyboard.controller.expectingPort60h != 0) {
                    keyboard.controller.expectingPort60h = 0;
                    keyboard.controller.commandData = 0;
                    if (keyboard.controller.inputBuffer != 0) {
                        logger.log(Level.WARNING, "[" + super.getType() + "]" + " Port 0x60 write but input buffer is not ready");
                    }
                    switch(keyboard.controller.lastCommand) {
                        case 0x60:
                            byte disableKeyboard, disableAux;
                            keyboard.controller.translateScancode = (byte) ((value >> 6) & 0x01);
                            disableAux = (byte) ((value >> 5) & 0x01);
                            disableKeyboard = (byte) ((value >> 4) & 0x01);
                            keyboard.controller.systemFlag = (byte) ((value >> 2) & 0x01);
                            keyboard.controller.allowIRQ12 = (byte) ((value >> 1) & 0x01);
                            keyboard.controller.allowIRQ1 = (byte) ((value >> 0) & 0x01);
                            this.setKeyboardClock(disableKeyboard == 0 ? true : false);
                            this.setAuxClock(disableAux == 0 ? true : false);
                            if ((keyboard.controller.allowIRQ12 != 0) && (keyboard.controller.auxBuffer != 0)) {
                                keyboard.controller.irq12Requested = 1;
                                logger.log(Level.INFO, "[" + super.getType() + "]" + " IRQ12 (mouse) allowance set to " + keyboard.controller.allowIRQ12);
                            } else if ((keyboard.controller.allowIRQ1 != 0) && (keyboard.controller.outputBuffer != 0)) {
                                keyboard.controller.irq1Requested = 1;
                                logger.log(Level.INFO, "[" + super.getType() + "]" + " IRQ1 (keyboard) allowance set to " + keyboard.controller.allowIRQ1);
                            }
                            if (keyboard.controller.translateScancode == 0) {
                                logger.log(Level.WARNING, "[" + super.getType() + "]" + " Scancode translation turned off");
                            }
                            return;
                        case (byte) 0xD1:
                            logger.log(Level.INFO, "[" + super.getType() + "]" + " Writing value 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase() + " to output port P2");
                            motherboard.setA20((value & 0x02) != 0 ? true : false);
                            logger.log(Level.INFO, "[" + super.getType() + "]" + (((value & 0x02) == 2) ? "En" : "Dis") + "abled A20 gate");
                            if (!((value & 0x01) != 0)) {
                                logger.log(Level.WARNING, "[" + super.getType() + "]" + " System reset requested (is not implemented yet)");
                            }
                            return;
                        case (byte) 0xD2:
                            this.enqueueControllerBuffer(value, KEYBOARD);
                            return;
                        case (byte) 0xD3:
                            this.enqueueControllerBuffer(value, MOUSE);
                            return;
                        case (byte) 0xD4:
                            logger.log(Level.INFO, "[keyboard] writing value to mouse: " + value);
                            ModuleMouse mouse = (ModuleMouse) super.getConnection(Module.Type.MOUSE);
                            mouse.controlMouse(value);
                            return;
                        default:
                            logger.log(Level.WARNING, "[" + super.getType() + "]" + " does not recognise command [" + Integer.toHexString(keyboard.controller.lastCommand).toUpperCase() + "] writing value " + Integer.toHexString(value).toUpperCase() + " to port " + Integer.toHexString(portAddress).toUpperCase());
                            throw new UnknownPortException(super.getType() + " -> does not recognise command " + keyboard.controller.lastCommand + " writing value " + Integer.toHexString(value).toUpperCase() + " to port " + Integer.toHexString(portAddress).toUpperCase());
                    }
                } else {
                    keyboard.controller.commandData = 0;
                    keyboard.controller.expectingPort60h = 0;
                    if (keyboard.controller.kbdClockEnabled == 0) {
                        setKeyboardClock(true);
                    }
                    this.dataPortToInternalKB(value);
                }
                return;
            case 0x64:
                keyboard.controller.commandData = 1;
                keyboard.controller.lastCommand = value;
                keyboard.controller.expectingPort60h = 0;
                switch(value) {
                    case 0x20:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + "Read keyboard controller command byte");
                        if (keyboard.controller.outputBuffer != 0) {
                            logger.log(Level.WARNING, "[" + super.getType() + "]" + " command 0x" + Integer.toHexString(value).toUpperCase() + " encountered but output buffer not empty!");
                            return;
                        }
                        byte commandByte = (byte) ((keyboard.controller.translateScancode << 6) | ((keyboard.controller.auxClockEnabled == 0 ? 1 : 0) << 5) | ((keyboard.controller.kbdClockEnabled == 0 ? 1 : 0) << 4) | (0 << 3) | (keyboard.controller.systemFlag << 2) | (keyboard.controller.allowIRQ12 << 1) | (keyboard.controller.allowIRQ1 << 0));
                        this.enqueueControllerBuffer(commandByte, KEYBOARD);
                        return;
                    case 0x60:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + " Write keyboard controller command byte");
                        keyboard.controller.expectingPort60h = 1;
                        return;
                    case (byte) 0xA0:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + "Unsupported command on port 0x64: 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase());
                        return;
                    case (byte) 0xA1:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + " Controller firmware version request: ignored");
                        return;
                    case (byte) 0xA7:
                        this.setAuxClock(false);
                        logger.log(Level.INFO, "[" + super.getType() + "]" + " Aux device (mouse) disabled");
                        return;
                    case (byte) 0xA8:
                        this.setAuxClock(true);
                        logger.log(Level.INFO, "[" + super.getType() + "]" + " Aux device (mouse) enabled");
                        return;
                    case (byte) 0xA9:
                        if (keyboard.controller.outputBuffer != 0) {
                            logger.log(Level.WARNING, "[" + super.getType() + "]" + " command 0x" + Integer.toHexString(value).toUpperCase() + " encountered but output buffer not empty!");
                            return;
                        }
                        this.enqueueControllerBuffer((byte) 0xFF, KEYBOARD);
                        return;
                    case (byte) 0xAA:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + " Controller self test");
                        if (kbdInitialised == 0) {
                            keyboard.getControllerQueue().clear();
                            keyboard.controller.outputBuffer = 0;
                            kbdInitialised++;
                        }
                        if (keyboard.controller.outputBuffer != 0) {
                            logger.log(Level.WARNING, "[" + super.getType() + "]" + " command 0x" + Integer.toHexString(value).toUpperCase() + " encountered but output buffer not empty!");
                            return;
                        }
                        keyboard.controller.systemFlag = 1;
                        this.enqueueControllerBuffer((byte) 0x55, KEYBOARD);
                        return;
                    case (byte) 0xAB:
                        if (keyboard.controller.outputBuffer != 0) {
                            logger.log(Level.WARNING, "[" + super.getType() + "]" + " command 0x" + Integer.toHexString(value).toUpperCase() + " encountered but output buffer not empty!");
                            return;
                        }
                        this.enqueueControllerBuffer((byte) 0x00, KEYBOARD);
                        return;
                    case (byte) 0xAD:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + " Keyboard disabled");
                        this.setKeyboardClock(false);
                        return;
                    case (byte) 0xAE:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + " Keyboard enabled");
                        this.setKeyboardClock(true);
                        return;
                    case (byte) 0xAF:
                        logger.log(Level.WARNING, "[" + super.getType() + "]" + "Unsupported command on port 0x64: 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase());
                        return;
                    case (byte) 0xC0:
                        if (keyboard.controller.outputBuffer != 0) {
                            logger.log(Level.WARNING, "[" + super.getType() + "]" + " command 0x" + Integer.toHexString(value).toUpperCase() + " encountered but output buffer not empty!");
                            return;
                        }
                        this.enqueueControllerBuffer((byte) 0x80, KEYBOARD);
                        return;
                    case (byte) 0xC1:
                    case (byte) 0xC2:
                        logger.log(Level.WARNING, "[" + super.getType() + "]" + "Unsupported command on port 0x64: 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase());
                        return;
                    case (byte) 0xD0:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + "Partially supported command on port 0x64: 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase());
                        if (keyboard.controller.outputBuffer != 0) {
                            logger.log(Level.WARNING, "[" + super.getType() + "]" + " command 0x" + Integer.toHexString(value).toUpperCase() + " encountered but output buffer not empty!");
                            return;
                        }
                        byte p2 = (byte) ((keyboard.controller.irq12Requested << 5) | (keyboard.controller.irq1Requested << 4) | (motherboard.getA20() ? 1 : 0 << 1) | 0x01);
                        this.enqueueControllerBuffer(p2, KEYBOARD);
                        return;
                    case (byte) 0xD1:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + " Port 0x64: write output port P2");
                        keyboard.controller.expectingPort60h = 1;
                        return;
                    case (byte) 0xD2:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + " Port 0x64: write keyboard output buffer");
                        keyboard.controller.expectingPort60h = 1;
                        return;
                    case (byte) 0xD3:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + " Port 0x64: write mouse output buffer");
                        keyboard.controller.expectingPort60h = 1;
                        return;
                    case (byte) 0xD4:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + " Port 0x64: write to mouse");
                        keyboard.controller.expectingPort60h = 1;
                        return;
                    case (byte) 0xDD:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + " Port 0xDD: A20 address line disabled");
                        motherboard.setA20(false);
                        return;
                    case (byte) 0xDF:
                        logger.log(Level.INFO, "[" + super.getType() + "]" + " Port 0xDF: A20 address line enabled");
                        motherboard.setA20(true);
                        return;
                    case (byte) 0xE0:
                        logger.log(Level.WARNING, "[" + super.getType() + "]" + " Unsupported command to port 0x64: 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase());
                        return;
                    case (byte) 0xFE:
                        logger.log(Level.WARNING, "[" + super.getType() + "]" + " Port 0x64: system reset (not implemented yet)");
                        return;
                    default:
                        if ((value >= 0xF0 && value <= 0xFD) || value == 0xFF) {
                            logger.log(Level.INFO, "[" + super.getType() + "]" + " Port 0x64: pulse output bits");
                            return;
                        }
                        logger.log(Level.WARNING, "[" + super.getType() + "]" + " Unsupported command to port 0x64: 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase());
                        return;
                }
            default:
                throw new UnknownPortException("[" + super.getType() + "]" + " does not recognise OUT port 0x" + Integer.toHexString(portAddress).toUpperCase());
        }
    }
