    public final void runcycle(final int scanline, final int pixel) {
        if (interrupt > 0 && !interruptsDisabled) {
            interrupt();
            cycles += 7;
            return;
        }
        pb = 0;
        final int instr = ram.read(PC++);
        if (logging) {
            try {
                w.write(utils.hex(PC - 1) + " " + utils.hex(instr) + String.format(" %-14s ", opcodes[instr].replaceFirst("%1", utils.hex(ram.read(PC))).replaceFirst("%2", utils.hex(ram.read(PC + 1))).replaceFirst("%3", utils.hex(PC + (byte) (ram.read(PC)) + 1))) + status() + " CYC:" + pixel + " SL:" + scanline + "\n");
                if (cycles == 0) {
                    w.flush();
                }
            } catch (IOException e) {
                System.err.println("Cannot write to debug log");
            }
        }
        switch(instr) {
            case 0x69:
                adc(imm());
                cycles += 2;
                break;
            case 0x65:
                adc(zpg());
                cycles += 3;
                break;
            case 0x75:
                adc(zpg(X));
                cycles += 4;
                break;
            case 0x6d:
                adc(abs());
                cycles += 4;
                break;
            case 0x7d:
                adc(abs(X));
                cycles += 4 + pb;
                break;
            case 0x79:
                adc(abs(Y));
                cycles += 4 + pb;
                break;
            case 0x61:
                adc(indX());
                cycles += 6;
                break;
            case 0x71:
                adc(indY());
                cycles += 5 + pb;
                break;
            case 0x29:
                and(imm());
                cycles += 2;
                break;
            case 0x25:
                and(zpg());
                cycles += 2;
                break;
            case 0x35:
                and(zpg(X));
                cycles += 3;
                break;
            case 0x2D:
                and(abs());
                cycles += 4 + pb;
                break;
            case 0x3D:
                and(abs(X));
                cycles += 4 + pb;
                break;
            case 0x39:
                and(abs(Y));
                cycles += 4 + pb;
                break;
            case 0x21:
                and(indX());
                cycles += 6;
                break;
            case 0x31:
                and(indY());
                cycles += 5 + pb;
                break;
            case 0x0A:
                aslA();
                cycles += 2;
                break;
            case 0x06:
                asl(zpg());
                cycles += 5;
                break;
            case 0x16:
                asl(zpg(X));
                cycles += 6;
                break;
            case 0x0e:
                asl(abs());
                cycles += 6;
                break;
            case 0x1e:
                asl(abs(X));
                cycles += 7;
                break;
            case 0x24:
                bit(zpg());
                cycles += 3;
                break;
            case 0x2c:
                bit(abs());
                cycles += 4;
                break;
            case 0x10:
                branch(!negativeFlag);
                cycles += 2 + pb;
                break;
            case 0x30:
                branch(negativeFlag);
                cycles += 2 + pb;
                break;
            case 0x50:
                branch(!overflowFlag);
                cycles += 2 + pb;
                break;
            case 0x70:
                branch(overflowFlag);
                cycles += 2 + pb;
                break;
            case 0x90:
                branch(!carryFlag);
                cycles += 2 + pb;
                break;
            case 0xB0:
                branch(carryFlag);
                cycles += 2 + pb;
                break;
            case 0xD0:
                branch(!zeroFlag);
                cycles += 2 + pb;
                break;
            case 0xF0:
                branch(zeroFlag);
                cycles += 2 + pb;
                break;
            case 0x00:
                breakinterrupt();
                cycles += 7;
                break;
            case 0xc9:
                cmp(A, imm());
                cycles += 2;
                break;
            case 0xc5:
                cmp(A, zpg());
                cycles += 3;
                break;
            case 0xd5:
                cmp(A, zpg(X));
                cycles += 4;
                break;
            case 0xcd:
                cmp(A, abs());
                cycles += 4;
                break;
            case 0xdd:
                cmp(A, abs(X));
                cycles += 4 + pb;
                break;
            case 0xd9:
                cmp(A, abs(Y));
                cycles += 4 + pb;
                break;
            case 0xc1:
                cmp(A, indX());
                cycles += 6;
                break;
            case 0xd1:
                cmp(A, indY());
                cycles += 5 + pb;
                break;
            case 0xe0:
                cmp(X, imm());
                cycles += 2;
                break;
            case 0xe4:
                cmp(X, zpg());
                cycles += 3;
                break;
            case 0xec:
                cmp(X, abs());
                cycles += 4;
                break;
            case 0xc0:
                cmp(Y, imm());
                cycles += 2;
                break;
            case 0xc4:
                cmp(Y, zpg());
                cycles += 3;
                break;
            case 0xcc:
                cmp(Y, abs());
                cycles += 4;
                break;
            case 0xc6:
                dec(zpg());
                cycles += 5;
                break;
            case 0xd6:
                dec(zpg(X));
                cycles += 6;
                break;
            case 0xce:
                dec(abs());
                cycles += 6;
                break;
            case 0xde:
                dec(abs(X));
                cycles += 7;
                break;
            case 0x49:
                eor(imm());
                cycles += 2;
                break;
            case 0x45:
                eor(zpg());
                cycles += 3;
                break;
            case 0x55:
                eor(zpg(X));
                cycles += 4;
                break;
            case 0x4d:
                eor(abs());
                cycles += 4;
                break;
            case 0x5d:
                eor(abs(X));
                cycles += 4 + pb;
                break;
            case 0x59:
                eor(abs(Y));
                cycles += 4 + pb;
                break;
            case 0x41:
                eor(indX());
                cycles += 6;
                break;
            case 0x51:
                eor(indY());
                cycles += 5;
                break;
            case 0x18:
                carryFlag = false;
                cycles += 2;
                break;
            case 0x38:
                carryFlag = true;
                cycles += 2;
                break;
            case 0x58:
                interruptsDisabled = false;
                cycles += 2;
                break;
            case 0x78:
                interruptsDisabled = true;
                cycles += 2;
                break;
            case 0xb8:
                overflowFlag = false;
                cycles += 2;
                break;
            case 0xd8:
                decimalModeFlag = false;
                cycles += 2;
                break;
            case 0xf8:
                decimalModeFlag = true;
                cycles += 2;
                break;
            case 0xe6:
                inc(zpg());
                cycles += 5;
                break;
            case 0xf6:
                inc(zpg(X));
                cycles += 6;
                break;
            case 0xee:
                inc(abs());
                cycles += 6;
                break;
            case 0xfe:
                inc(abs(X));
                cycles += 7;
                break;
            case 0x4c:
                PC = abs();
                cycles += 3;
                break;
            case 0x6c:
                PC = ind();
                cycles += 5;
                break;
            case 0x20:
                jsr(abs());
                cycles += 6;
                break;
            case 0xa3:
                lax(indX());
                break;
            case 0xb3:
                lax(indY());
                break;
            case 0x83:
                lax(abs(Y));
                break;
            case 0xa7:
                lax(zpg());
                break;
            case 0xb7:
                lax(zpg(Y));
                break;
            case 0xaf:
                lax(abs());
                break;
            case 0xbf:
                lax(abs(Y));
                break;
            case 0xa9:
                lda(imm());
                cycles += 2;
                break;
            case 0xa5:
                lda(zpg());
                cycles += 3;
                break;
            case 0xb5:
                lda(zpg(X));
                cycles += 4;
                break;
            case 0xad:
                lda(abs());
                cycles += 4;
                break;
            case 0xbd:
                lda(abs(X));
                cycles += 4 + pb;
                break;
            case 0xb9:
                lda(abs(Y));
                cycles += 4 + pb;
                break;
            case 0xa1:
                lda(indX());
                cycles += 6;
                break;
            case 0xb1:
                lda(indY());
                cycles += 5 + pb;
                break;
            case 0xa2:
                ldx(imm());
                cycles += 2;
                break;
            case 0xa6:
                ldx(zpg());
                cycles += 3;
                break;
            case 0xb6:
                ldx(zpg(Y));
                cycles += 4;
                break;
            case 0xae:
                ldx(abs());
                cycles += 4;
                break;
            case 0xbe:
                ldx(abs(Y));
                cycles += 4 + pb;
                break;
            case 0xa0:
                ldy(imm());
                cycles += 2;
                break;
            case 0xa4:
                ldy(zpg());
                cycles += 3;
                break;
            case 0xb4:
                ldy(zpg(X));
                cycles += 4;
                break;
            case 0xac:
                ldy(abs());
                cycles += 4;
                break;
            case 0xbc:
                ldy(abs(X));
                cycles += 4 + pb;
                break;
            case 0x4a:
                lsrA();
                cycles += 2;
                break;
            case 0x46:
                lsr(zpg());
                cycles += 5;
                break;
            case 0x56:
                lsr(zpg(X));
                cycles += 6;
                break;
            case 0x4e:
                lsr(abs());
                cycles += 6;
                break;
            case 0x5e:
                lsr(abs(X));
                cycles += 7;
                break;
            case 0x1a:
            case 0x3a:
            case 0x5a:
            case 0x7a:
            case 0xda:
            case 0xEA:
            case 0xfa:
                cycles += 2;
                break;
            case 0x80:
            case 0x04:
                PC++;
                cycles += 3;
                break;
            case 0x14:
            case 0x34:
            case 0x44:
            case 0x54:
            case 0x64:
            case 0x74:
            case 0xd4:
            case 0xf4:
                PC++;
                cycles += 4;
                break;
            case 0x0C:
            case 0x3c:
            case 0x5c:
            case 0x7c:
            case 0xdc:
            case 0xfc:
                PC += 2;
                cycles += 4;
                break;
            case 0x1c:
                PC += 2;
                cycles += 4 + pb;
                break;
            case 0x09:
                ora(imm());
                cycles += 2;
                break;
            case 0x05:
                ora(zpg());
                cycles += 3;
                break;
            case 0x15:
                ora(zpg(X));
                cycles += 4;
                break;
            case 0x0d:
                ora(abs());
                cycles += 4;
                break;
            case 0x1d:
                ora(abs(X));
                cycles += 4 + pb;
                break;
            case 0x19:
                ora(abs(Y));
                cycles += 4 + pb;
                break;
            case 0x01:
                ora(indX());
                cycles += 6;
                break;
            case 0x11:
                ora(indY());
                cycles += 5 + pb;
                break;
            case 0xAA:
                X = A;
                cycles += 2;
                setflags(A);
                break;
            case 0x8a:
                A = X;
                cycles += 2;
                setflags(A);
                break;
            case 0xca:
                X--;
                X &= 0xFF;
                setflags(X);
                cycles += 2;
                break;
            case 0xe8:
                X++;
                X &= 0xFF;
                setflags(X);
                cycles += 2;
                break;
            case 0xa8:
                Y = A;
                cycles += 2;
                setflags(A);
                break;
            case 0x98:
                A = Y;
                cycles += 2;
                setflags(A);
                break;
            case 0x88:
                Y--;
                Y &= 0xFF;
                setflags(Y);
                cycles += 2;
                break;
            case 0xc8:
                Y++;
                Y &= 0xFF;
                setflags(Y);
                cycles += 2;
                break;
            case 0x2a:
                rolA();
                cycles += 2;
                break;
            case 0x26:
                rol(zpg());
                cycles += 5;
                break;
            case 0x36:
                rol(zpg(X));
                cycles += 6;
                break;
            case 0x2e:
                rol(abs());
                cycles += 6;
                break;
            case 0x3e:
                rol(abs(X));
                cycles += 6;
                break;
            case 0x6a:
                rorA();
                cycles += 2;
                break;
            case 0x66:
                ror(zpg());
                cycles += 5;
                break;
            case 0x76:
                ror(zpg(X));
                cycles += 6;
                break;
            case 0x6e:
                ror(abs());
                cycles += 6;
                break;
            case 0x7e:
                ror(abs(X));
                cycles += 6;
                break;
            case 0x40:
                rti();
                cycles += 6;
                break;
            case 0x60:
                rts();
                cycles += 6;
                break;
            case 0xE9:
                sbc(imm());
                cycles += 2;
                break;
            case 0xE5:
                sbc(zpg());
                cycles += 3;
                break;
            case 0xF5:
                sbc(zpg(X));
                cycles += 4;
                break;
            case 0xEd:
                sbc(abs());
                cycles += 4;
                break;
            case 0xFd:
                sbc(abs(X));
                cycles += 4 + pb;
                break;
            case 0xF9:
                sbc(abs(Y));
                cycles += 4 + pb;
                break;
            case 0xE1:
                sbc(indX());
                cycles += 6;
                break;
            case 0xF1:
                sbc(indY());
                cycles += 5 + pb;
                break;
            case 0xCB:
                sbx(imm());
                cycles += 2;
                break;
            case 0x85:
                sta(zpg());
                cycles += 3;
                break;
            case 0x95:
                sta(zpg(X));
                cycles += 4;
                break;
            case 0x8d:
                sta(abs());
                cycles += 4;
                break;
            case 0x9d:
                sta(abs(X));
                cycles += 5;
                break;
            case 0x99:
                sta(abs(Y));
                cycles += 5;
                break;
            case 0x81:
                sta(indX());
                cycles += 6;
                break;
            case 0x91:
                sta(indY());
                cycles += 6;
                break;
            case 0x9A:
                S = X;
                cycles += 2;
                break;
            case 0xBA:
                X = S;
                cycles += 2;
                setflags(X);
                break;
            case 0x48:
                push(A);
                cycles += 3;
                break;
            case 0x68:
                A = pop();
                setflags(A);
                cycles += 4;
                break;
            case 0x08:
                push(flagstobyte() | 0x30);
                cycles += 3;
                break;
            case 0x28:
                bytetoflags(pop());
                cycles += 4;
                break;
            case 0x86:
                stx(zpg());
                cycles += 3;
                break;
            case 0x96:
                stx(zpg(Y));
                cycles += 4;
                break;
            case 0x8E:
                stx(abs());
                cycles += 4;
                break;
            case 0x84:
                sty(zpg());
                cycles += 3;
                break;
            case 0x94:
                sty(zpg(X));
                cycles += 4;
                break;
            case 0x8c:
                sty(abs());
                cycles += 4;
                break;
            default:
                cycles += 2;
                System.err.println("Illegal opcode:" + utils.hex(instr) + " @ " + utils.hex(PC - 1));
                break;
        }
        pb = 0;
        PC &= 0xffff;
    }
