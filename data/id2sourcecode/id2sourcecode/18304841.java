    private final void instructionFetchExecute() {
        MemoryManager memory = nes.memory;
        int instCode = memory.read(PC++);
        int address;
        int writeVal;
        switch(instCode) {
            case 0x00:
                address = PC + 1;
                pushWord(address);
                push(P | 0x10);
                PC = readWord(0xFFFE);
                P |= 0x04;
                P |= 0x10;
                break;
            case 0xA9:
                A = byImmediate();
                setStatusFlags(A);
                break;
            case 0xA5:
                A = memory.read(byZeroPage());
                setStatusFlags(A);
                break;
            case 0xB5:
                A = memory.read(byZeroPageX());
                setStatusFlags(A);
                break;
            case 0xAD:
                A = memory.read(byAbsolute());
                setStatusFlags(A);
                break;
            case 0xBD:
                A = memory.read(byAbsoluteX());
                setStatusFlags(A);
                break;
            case 0xB9:
                A = memory.read(byAbsoluteY());
                setStatusFlags(A);
                break;
            case 0xA1:
                A = memory.read(byIndirectX());
                setStatusFlags(A);
                break;
            case 0xB1:
                A = memory.read(byIndirectY());
                setStatusFlags(A);
                break;
            case 0xA2:
                X = byImmediate();
                setStatusFlags(X);
                break;
            case 0xA6:
                X = memory.read(byZeroPage());
                setStatusFlags(X);
                break;
            case 0xB6:
                X = memory.read(byZeroPageY());
                setStatusFlags(X);
                break;
            case 0xAE:
                X = memory.read(byAbsolute());
                setStatusFlags(X);
                break;
            case 0xBE:
                X = memory.read(byAbsoluteY());
                setStatusFlags(X);
                break;
            case 0xA0:
                Y = byImmediate();
                setStatusFlags(Y);
                break;
            case 0xA4:
                Y = memory.read(byZeroPage());
                setStatusFlags(Y);
                break;
            case 0xB4:
                Y = memory.read(byZeroPageX());
                setStatusFlags(Y);
                break;
            case 0xAC:
                Y = memory.read(byAbsolute());
                setStatusFlags(Y);
                break;
            case 0xBC:
                Y = memory.read(byAbsoluteX());
                setStatusFlags(Y);
                break;
            case 0x85:
                address = byZeroPage();
                write(address, A);
                break;
            case 0x95:
                address = byZeroPageX();
                write(address, A);
                break;
            case 0x8D:
                address = byAbsolute();
                write(address, A);
                break;
            case 0x9D:
                address = byAbsoluteX();
                write(address, A);
                break;
            case 0x99:
                address = byAbsoluteY();
                write(address, A);
                break;
            case 0x81:
                address = byIndirectX();
                write(address, A);
                break;
            case 0x91:
                address = byIndirectY();
                write(address, A);
                break;
            case 0x86:
                address = byZeroPage();
                write(address, X);
                break;
            case 0x96:
                address = byZeroPageY();
                write(address, X);
                break;
            case 0x8E:
                address = byAbsolute();
                write(address, X);
                break;
            case 0x84:
                address = byZeroPage();
                write(address, Y);
                break;
            case 0x94:
                address = byZeroPageX();
                write(address, Y);
                break;
            case 0x8C:
                address = byAbsolute();
                write(address, Y);
                break;
            case 0xAA:
                X = A;
                setStatusFlags(X);
                break;
            case 0xA8:
                Y = A;
                setStatusFlags(Y);
                break;
            case 0xBA:
                X = S & 0xFF;
                setStatusFlags(X);
                break;
            case 0x8A:
                A = X;
                setStatusFlags(A);
                break;
            case 0x9A:
                S = X & 0XFF;
                break;
            case 0x98:
                A = Y;
                setStatusFlags(A);
                break;
            case 0x09:
                A |= byImmediate();
                setStatusFlags(A);
                break;
            case 0x05:
                address = byZeroPage();
                A |= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x15:
                address = byZeroPageX();
                A |= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x0D:
                address = byAbsolute();
                A |= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x1D:
                address = byAbsoluteX();
                A |= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x19:
                address = byAbsoluteY();
                A |= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x01:
                address = byIndirectX();
                A |= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x11:
                address = byIndirectY();
                A |= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x29:
                A &= byImmediate();
                setStatusFlags(A);
                break;
            case 0x25:
                address = byZeroPage();
                A &= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x35:
                address = byZeroPageX();
                A &= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x2D:
                address = byAbsolute();
                A &= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x3D:
                address = byAbsoluteX();
                A &= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x39:
                address = byAbsoluteY();
                A &= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x21:
                address = byIndirectX();
                A &= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x31:
                address = byIndirectY();
                A &= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x49:
                A ^= byImmediate();
                setStatusFlags(A);
                break;
            case 0x45:
                address = byZeroPage();
                A ^= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x55:
                address = byZeroPageX();
                A ^= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x4D:
                address = byAbsolute();
                A ^= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x5D:
                address = byAbsoluteX();
                A ^= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x59:
                address = byAbsoluteY();
                A ^= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x41:
                address = byIndirectX();
                A ^= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x51:
                address = byIndirectY();
                A ^= memory.read(address);
                setStatusFlags(A);
                break;
            case 0x24:
                operateBit(read(byZeroPage()));
                break;
            case 0x2C:
                operateBit(read(byAbsolute()));
                break;
            case 0x0A:
                A = ASL(A);
                break;
            case 0x06:
                address = byZeroPage();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = ASL(writeVal);
                write(address, writeVal);
                break;
            case 0x16:
                address = byZeroPageX();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = ASL(writeVal);
                write(address, writeVal);
                break;
            case 0x0E:
                address = byAbsolute();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = ASL(writeVal);
                write(address, writeVal);
                break;
            case 0x1E:
                address = byAbsoluteX();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = ASL(writeVal);
                write(address, writeVal);
                break;
            case 0x4A:
                A = LSR(A);
                break;
            case 0x46:
                address = byZeroPage();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = LSR(writeVal);
                write(address, writeVal);
                break;
            case 0x56:
                address = byZeroPageX();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = LSR(writeVal);
                write(address, writeVal);
                break;
            case 0x4E:
                address = byAbsolute();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = LSR(writeVal);
                write(address, writeVal);
                break;
            case 0x5E:
                address = byAbsoluteX();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = LSR(writeVal);
                write(address, writeVal);
                break;
            case 0x2A:
                A = ROL(A);
                break;
            case 0x26:
                address = byZeroPage();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = ROL(writeVal);
                write(address, writeVal);
                break;
            case 0x36:
                address = byZeroPageX();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = ROL(writeVal);
                write(address, writeVal);
                break;
            case 0x2E:
                address = byAbsolute();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = ROL(writeVal);
                write(address, writeVal);
                break;
            case 0x3E:
                address = byAbsoluteX();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = ROL(writeVal);
                write(address, writeVal);
                break;
            case 0x6A:
                A = ROR(A);
                break;
            case 0x66:
                address = byZeroPage();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = ROR(writeVal);
                write(address, writeVal);
                break;
            case 0x76:
                address = byZeroPageX();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = ROR(writeVal);
                write(address, writeVal);
                break;
            case 0x6E:
                address = byAbsolute();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = ROR(writeVal);
                write(address, writeVal);
                break;
            case 0x7E:
                address = byAbsoluteX();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = ROR(writeVal);
                write(address, writeVal);
                break;
            case 0x90:
                branch(0x01, false);
                break;
            case 0xB0:
                branch(0x01, true);
                break;
            case 0xD0:
                branch(0x02, false);
                break;
            case 0xF0:
                branch(0x02, true);
                break;
            case 0x10:
                branch(0x80, false);
                break;
            case 0x30:
                branch(0x80, true);
                break;
            case 0x50:
                branch(0x40, false);
                break;
            case 0x70:
                branch(0x40, true);
                break;
            case 0x4C:
                PC = byAbsolute();
                break;
            case 0x6C:
                address = byAbsolute();
                if ((address & 0x00FF) == 0xFF) {
                    PC = (read(address & 0xFF00) << 8) | read(address);
                } else {
                    PC = readWord(address);
                }
                break;
            case 0x20:
                address = PC + 1;
                pushWord(address);
                PC = byAbsolute();
                break;
            case 0x60:
                PC = popWord() + 1;
                break;
            case 0x40:
                P = pop();
                PC = popWord();
                break;
            case 0x48:
                push(A);
                break;
            case 0x08:
                push(P | 0x10);
                break;
            case 0x68:
                A = pop();
                setStatusFlags(A);
                break;
            case 0x28:
                P = pop();
                break;
            case 0x18:
                P &= 0xfe;
                break;
            case 0xD8:
                P &= 0xf7;
                break;
            case 0x58:
                P &= 0xfb;
                break;
            case 0xB8:
                P &= 0xbf;
                break;
            case 0x38:
                P |= 0x1;
                break;
            case 0xF8:
                P |= 0x8;
                break;
            case 0x78:
                P |= 0x4;
                break;
            case 0xE6:
                address = byZeroPage();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = increment(writeVal);
                write(address, writeVal);
                break;
            case 0xF6:
                address = byZeroPageX();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = increment(read(address));
                write(address, writeVal);
                break;
            case 0xEE:
                address = byAbsolute();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = increment(read(address));
                write(address, writeVal);
                break;
            case 0xFE:
                address = byAbsoluteX();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = increment(read(address));
                write(address, writeVal);
                break;
            case 0xE8:
                X++;
                X &= 0xff;
                setStatusFlags(X);
                break;
            case 0xC8:
                Y++;
                Y &= 0xff;
                setStatusFlags(Y);
                break;
            case 0xC6:
                address = byZeroPage();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = decrement(read(address));
                write(address, writeVal);
                break;
            case 0xD6:
                address = byZeroPageX();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = decrement(read(address));
                write(address, writeVal);
                break;
            case 0xCE:
                address = byAbsolute();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = decrement(read(address));
                write(address, writeVal);
                break;
            case 0xDE:
                address = byAbsoluteX();
                writeVal = memory.read(address);
                write(address, writeVal);
                writeVal = decrement(read(address));
                write(address, writeVal);
                break;
            case 0xCA:
                X--;
                X &= 0xff;
                setStatusFlags(X);
                break;
            case 0x88:
                Y--;
                Y &= 0xff;
                setStatusFlags(Y);
                break;
            case 0x69:
                operateAdd(byImmediate());
                break;
            case 0x65:
                operateAdd(read(byZeroPage()));
                break;
            case 0x75:
                operateAdd(read(byZeroPageX()));
                break;
            case 0x6D:
                operateAdd(read(byAbsolute()));
                break;
            case 0x7D:
                operateAdd(read(byAbsoluteX()));
                break;
            case 0x79:
                operateAdd(read(byAbsoluteY()));
                break;
            case 0x61:
                operateAdd(read(byIndirectX()));
                break;
            case 0x71:
                operateAdd(read(byIndirectY()));
                break;
            case 0xEB:
            case 0xE9:
                operateSub(byImmediate());
                break;
            case 0xE5:
                operateSub(read(byZeroPage()));
                break;
            case 0xF5:
                operateSub(read(byZeroPageX()));
                break;
            case 0xED:
                operateSub(read(byAbsolute()));
                break;
            case 0xFD:
                operateSub(read(byAbsoluteX()));
                break;
            case 0xF9:
                operateSub(read(byAbsoluteY()));
                break;
            case 0xE1:
                operateSub(read(byIndirectX()));
                break;
            case 0xF1:
                operateSub(read(byIndirectY()));
                break;
            case 0xC9:
                operateCmp(A, byImmediate());
                break;
            case 0xC5:
                operateCmp(A, read(byZeroPage()));
                break;
            case 0xD5:
                operateCmp(A, read(byZeroPageX()));
                break;
            case 0xCD:
                operateCmp(A, read(byAbsolute()));
                break;
            case 0xDD:
                operateCmp(A, read(byAbsoluteX()));
                break;
            case 0xD9:
                operateCmp(A, read(byAbsoluteY()));
                break;
            case 0xC1:
                operateCmp(A, read(byIndirectX()));
                break;
            case 0xD1:
                operateCmp(A, read(byIndirectY()));
                break;
            case 0xE0:
                operateCmp(X, byImmediate());
                break;
            case 0xE4:
                operateCmp(X, read(byZeroPage()));
                break;
            case 0xEC:
                operateCmp(X, read(byAbsolute()));
                break;
            case 0xC0:
                operateCmp(Y, byImmediate());
                break;
            case 0xC4:
                operateCmp(Y, read(byZeroPage()));
                break;
            case 0xCC:
                operateCmp(Y, read(byAbsolute()));
                break;
            case 0x1A:
            case 0x3A:
            case 0x5A:
            case 0x7A:
            case 0xDA:
            case 0xEA:
            case 0xFA:
                break;
            default:
                halted = true;
                PC--;
                break;
        }
        cyclesPending -= cycles[instCode];
    }
