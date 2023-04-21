    public void write3(int index, int value) {
        switch(index & 0x3f) {
            case 0x00:
                if (((value & 0x80) != 0) && !lcdController.operationEnabled) {
                    restart();
                }
                lcdController.setValue(value);
                break;
            case 0x01:
                STAT = (STAT & 0x87) | (value & 0x78);
                if (!isCGB && ((STAT & 2) == 0) && lcdController.operationEnabled) {
                    cpu.triggerInterrupt(1);
                }
                break;
            case 0x02:
                if (useSubscanlineRendering) renderScanLinePart.execute(this);
                SCY = value;
                break;
            case 0x03:
                if (useSubscanlineRendering) renderScanLinePart.execute(this);
                SCX = value;
                break;
            case 0x04:
                LY = 0;
                break;
            case 0x05:
                STAT &= ~(1 << 2);
                if (LYC != value && LY == value && (STAT & (1 << 6)) != 0) {
                    STAT |= (1 << 2);
                    cpu.triggerInterrupt(1);
                }
                LYC = value;
                break;
            case 0x06:
                {
                    cpu.last_memory_access = cpu.last_memory_access_internal;
                    for (int i = 0; i < 0xa0; ++i) {
                        cpu.write(0xfe00 | i, cpu.read(i + (value << 8)));
                    }
                    cpu.last_memory_access_internal = cpu.last_memory_access;
                }
                break;
            case 0x07:
            case 0x08:
            case 0x09:
                if (useSubscanlineRendering) renderScanLinePart.execute(this);
                cpu.IOP[index - 0xff00] = value;
                updateMonoColData(index - 0xff47);
                break;
            case 0x0a:
                WY = value;
                break;
            case 0x0b:
                WX = value;
                break;
            case 0x0d:
                cpu.speedswitch = ((value & 1) != 0);
                break;
            case 0x0f:
                selectVRAMBank(value & 1);
                break;
            case 0x11:
            case 0x12:
            case 0x13:
            case 0x14:
                cpu.IOP[index - 0xff00] = value;
                break;
            case 0x15:
                int mode = ((cpu.hblank_dma_state | value) & 0x80);
                if (mode == 0) {
                    int src = ((cpu.IOP[0x51] << 8) | cpu.IOP[0x52]) & 0xfff0;
                    int dst = (((cpu.IOP[0x53] << 8) | cpu.IOP[0x54]) & 0x1ff0) | 0x8000;
                    int len = ((value & 0x7f) + 1) << 4;
                    CPULogger.log("WARNING: cpu.write(): TODO: Untimed H-DMA Transfer");
                    for (int i = 0; i < len; ++i) write(dst++, cpu.read(src++));
                    cpu.IOP[0x51] = src >> 8;
                    cpu.IOP[0x52] = src & 0xF0;
                    cpu.IOP[0x53] = 0x1F & (dst >> 8);
                    cpu.IOP[0x54] = dst & 0xF0;
                    cpu.IOP[0x55] = 0xff;
                } else {
                    cpu.hblank_dma_state = value;
                    cpu.IOP[0x55] = value & 0x7f;
                }
                break;
            case 0x28:
                bgpTable.setIndex(value);
                break;
            case 0x29:
                bgpTable.setColor(imageRenderer, value);
                break;
            case 0x2a:
                obpTable.setIndex(value);
                break;
            case 0x2b:
                obpTable.setColor(imageRenderer, value);
                break;
            case 0x2c:
                CPULogger.printf("WARNING: VC.write(): Write %02x to *undocumented* IO port $%04x\n", value, index);
                cpu.IOP[index - 0xff00] = value;
                break;
            default:
                CPULogger.printf("TODO: VC.write(): Write %02x to IO port $%04x\n", value, index);
                break;
        }
    }
