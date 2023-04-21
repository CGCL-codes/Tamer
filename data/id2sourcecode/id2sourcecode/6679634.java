    public int getMemoryFromHexAddress(int address) throws IllegalStateException {
        int value = 0;
        value = this.getCpuMemory().getMemory()[address];
        if (address >= 0x2000 && address <= 0x3FFF) {
            if (address > 0x2007) {
                address = address & 0xFF;
                address = address % 8;
                address += 0x2000;
            }
            if (address == PPUControlRegister.REGISTER_ADDRESS) {
                throw new IllegalStateException("reading from write only register: " + Integer.toHexString(address));
            } else if (address == PPUMaskRegister.REGISTER_ADDRESS) {
                throw new IllegalStateException("reading from write only register: " + Integer.toHexString(address));
            } else if (address == PPUSprRamAddressRegister.REGISTER_ADDRESS) {
                throw new IllegalStateException("reading from write only register: " + Integer.toHexString(address));
            } else if (address == PPUSprRamIORegister.REGISTER_ADDRESS) {
                value = this.getCpuMemory().getPpuSprRamIORegister().getRegisterValue();
            } else if (address == PPUStatusRegister.REGISTER_ADDRESS) {
                value = this.getCpuMemory().getPpuStatusRegister().getRegisterValue();
            } else if (address == PPUScrollRegister.REGISTER_ADDRESS) {
                throw new IllegalStateException("reading from write only register: " + Integer.toHexString(address));
            } else if (address == PPUVramAddressRegister.REGISTER_ADDRESS) {
                throw new IllegalStateException("reading from write only register: " + Integer.toHexString(address));
            } else if (address == PPUVramIORegister.REGISTER_ADDRESS) {
                value = this.getCpuMemory().getPpuVramIORegister().getRegisterValue();
            }
            logger.debug("getting memory from control register 0x" + Integer.toHexString(address) + ": " + Integer.toHexString(value));
        } else if (address >= 0x4000 && address <= 0x401F) {
            if (address == PPUSpriteDMARegister.REGISTER_ADDRESS) {
                throw new IllegalStateException("reading from write only register");
            } else if (address == ControlRegister1.REGISTER_ADDRESS) {
                value = this.getCpuMemory().getControlRegister1().getRegisterValue();
            } else if (address == ControlRegister2.REGISTER_ADDRESS) {
                value = this.getCpuMemory().getControlRegister2().getRegisterValue();
            }
        }
        return value;
    }
