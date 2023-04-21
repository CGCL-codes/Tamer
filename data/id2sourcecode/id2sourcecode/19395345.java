    public void emulateOp() {
        if (checkInterrupt) {
            if ((NMILow && !NMILastLow) && (cycles >= nmiCycleStart)) {
                log("NMI interrupt at " + cycles);
                lastInterrupt = NMI_INT;
                doInterrupt(0xfffa, getStatusByte() & 0xef);
                disableInterupt = true;
                NMILastLow = NMILow;
                return;
            } else if ((IRQLow && cycles >= irqCycleStart) || brk) {
                if (!disableInterupt) {
                    log("IRQ interrupt > " + IRQLow + " BRK: " + brk);
                    lastInterrupt = IRQ_INT;
                    int status = getStatusByte();
                    if (brk) {
                        status |= 0x10;
                        pc++;
                    } else status &= 0xef;
                    doInterrupt(0xfffe, status);
                    disableInterupt = true;
                    brk = false;
                    return;
                } else {
                    brk = false;
                    checkInterrupt = (NMILow && !NMILastLow);
                }
            } else if (resetFlag) {
                doReset();
            } else if (jumpTo != -1) {
                pc = jumpTo;
                jumpTo = -1;
            }
        }
        int data = INSTRUCTION_SET[fetchByte(pc++)];
        int op = data & OP_MASK;
        int addrMode = data & ADDRESSING_MASK;
        boolean read = (data & READ) != 0;
        boolean write = (data & WRITE) != 0;
        int adr = 0;
        int tmp = 0;
        boolean nxtcarry = false;
        lastReadOP = rindex;
        int p1 = fetchByte(pc);
        switch(addrMode) {
            case IMMEDIATE:
                pc++;
                data = p1;
                break;
            case ABSOLUTE:
                pc++;
                adr = (fetchByte(pc++) << 8) + p1;
                if (read) {
                    data = fetchByte(adr);
                }
                break;
            case ZERO:
                pc++;
                adr = p1;
                if (read) {
                    data = fetchByte(adr);
                }
                break;
            case ZERO_X:
            case ZERO_Y:
                pc++;
                fetchByte(p1);
                if (addrMode == ZERO_X) adr = (p1 + x) & 0xff; else adr = (p1 + y) & 0xff;
                if (read) {
                    data = fetchByte(adr);
                }
                break;
            case ABSOLUTE_X:
            case ABSOLUTE_Y:
                pc++;
                adr = fetchByte(pc++) << 8;
                if (addrMode == ABSOLUTE_X) p1 += x; else p1 += y;
                data = fetchByte(adr + (p1 & 0xff));
                adr += p1;
                if (read && (p1 > 0xff || write)) {
                    data = fetchByte(adr);
                }
                break;
            case RELATIVE:
                pc++;
                adr = pc + (byte) p1;
                if (((adr ^ pc) & 0xff00) > 0) {
                    tmp = 2;
                } else {
                    tmp = 1;
                }
                break;
            case ACCUMULATOR:
                data = acc;
                write = false;
                break;
            case INDIRECT_X:
                pc++;
                fetchByte(p1);
                tmp = (p1 + x) & 0xff;
                adr = (fetchByte(tmp + 1) << 8);
                adr |= fetchByte(tmp);
                if (read) {
                    data = fetchByte(adr);
                }
                break;
            case INDIRECT_Y:
                pc++;
                adr = (fetchByte(p1 + 1) << 8);
                p1 = fetchByte(p1);
                p1 += y;
                data = fetchByte(adr + (p1 & 0xff));
                adr += p1;
                if (read && (p1 > 0xff || write)) {
                    data = fetchByte(adr);
                }
                break;
            case INDIRECT:
                pc++;
                adr = (fetchByte(pc) << 8) + p1;
                tmp = (adr & 0xfff00) | ((adr + 1) & 0xff);
                adr = fetchByte(adr);
                adr += (fetchByte(tmp) << 8);
                break;
        }
        if (read && write) {
            writeByte(adr, data);
        }
        switch(op) {
            case BRK:
                brk = true;
                checkInterrupt = true;
                break;
            case AND:
                acc = acc & data;
                setZS(acc);
                break;
            case ADC:
                opADCimp(data);
                break;
            case SBC:
                opSBCimp(data);
                break;
            case ORA:
                acc = acc | data;
                setZS(acc);
                break;
            case EOR:
                acc = acc ^ data;
                setZS(acc);
                break;
            case BIT:
                sign = data > 0x7f;
                overflow = (data & 0x40) > 0;
                zero = (acc & data) == 0;
                break;
            case LSR:
                carry = (data & 0x01) != 0;
                data = data >> 1;
                zero = (data == 0);
                sign = false;
                break;
            case ROL:
                data = (data << 1) + (carry ? 1 : 0);
                carry = (data & 0x100) != 0;
                data = data & 0xff;
                setZS(data);
                break;
            case ROR:
                nxtcarry = (data & 0x01) != 0;
                data = (data >> 1) + (carry ? 0x80 : 0);
                carry = nxtcarry;
                setZS(data);
                break;
            case TXA:
                acc = x;
                setZS(acc);
                break;
            case TAX:
                x = acc;
                setZS(x);
                break;
            case TYA:
                acc = y;
                setZS(acc);
                break;
            case TAY:
                y = acc;
                setZS(y);
                break;
            case TSX:
                x = s;
                setZS(x);
                break;
            case TXS:
                s = x & 0xff;
                break;
            case DEC:
                data = (data - 1) & 0xff;
                setZS(data);
                break;
            case INC:
                data = (data + 1) & 0xff;
                setZS(data);
                break;
            case INX:
                x = (x + 1) & 0xff;
                setZS(x);
                break;
            case DEX:
                x = (x - 1) & 0xff;
                setZS(x);
                break;
            case INY:
                y = (y + 1) & 0xff;
                setZS(y);
                break;
            case DEY:
                y = (y - 1) & 0xff;
                setZS(y);
                break;
            case JSR:
                pc++;
                adr = (fetchByte(pc) << 8) + p1;
                fetchByte(s | 0x100);
                push((pc & 0xff00) >> 8);
                push(pc & 0x00ff);
                pc = adr;
                break;
            case JMP:
                pc = adr;
                break;
            case RTS:
                fetchByte(s | 0x100);
                pc = pop() + (pop() << 8);
                pc++;
                fetchByte(pc);
                break;
            case RTI:
                fetchByte(s | 0x100);
                tmp = pop();
                setStatusByte(tmp);
                pc = pop() + (pop() << 8);
                brk = false;
                interruptInExec--;
                checkInterrupt = true;
                break;
            case TRP:
                monitor.info("TRAP Instruction executed");
                break;
            case NOP:
                break;
            case ASL:
                setCarry(data);
                data = (data << 1) & 0xff;
                setZS(data);
                break;
            case PHA:
                push(acc);
                break;
            case PLA:
                fetchByte(s | 0x100);
                acc = pop();
                setZS(acc);
                break;
            case PHP:
                brk = true;
                push(getStatusByte());
                brk = false;
                break;
            case PLP:
                tmp = pop();
                setStatusByte(tmp);
                brk = false;
                checkInterrupt = true;
                break;
            case ANC:
                acc = acc & data;
                setZS(acc);
                carry = (acc & 0x80) != 0;
                break;
            case CMP:
                data = acc - data;
                carry = data >= 0;
                setZS((data & 0xff));
                break;
            case CPX:
                data = x - data;
                carry = data >= 0;
                setZS((data & 0xff));
                break;
            case CPY:
                data = y - data;
                carry = data >= 0;
                setZS((data & 0xff));
                break;
            case BCC:
                branch(!carry, adr, tmp);
                break;
            case BCS:
                branch(carry, adr, tmp);
                break;
            case BEQ:
                branch(zero, adr, tmp);
                break;
            case BNE:
                branch(!zero, adr, tmp);
                break;
            case BVC:
                branch(!overflow, adr, tmp);
                break;
            case BVS:
                branch(overflow, adr, tmp);
                break;
            case BPL:
                branch(!sign, adr, tmp);
                break;
            case BMI:
                branch(sign, adr, tmp);
                break;
            case CLC:
                carry = false;
                break;
            case SEC:
                carry = true;
                break;
            case CLD:
                decimal = false;
                break;
            case SED:
                decimal = true;
                break;
            case CLV:
                overflow = false;
                break;
            case SEI:
                disableInterupt = true;
                break;
            case CLI:
                disableInterupt = false;
                checkInterrupt = true;
                log(getName() + " Enabled interrupts: IRQ: " + chips.getIRQFlags() + " IRQLow: " + IRQLow);
                break;
            case LDA:
                acc = data;
                setZS(data);
                break;
            case LDX:
                x = data;
                setZS(data);
                break;
            case LDY:
                y = data;
                setZS(data);
                break;
            case STA:
                data = acc;
                break;
            case STX:
                data = x;
                break;
            case STY:
                data = y;
                break;
            case ANE:
                acc = p1 & x & (acc | 0xee);
                setZS(acc);
                break;
            case ARR:
                tmp = p1 & acc;
                acc = (carry ? (tmp >> 1) | 0x80 : tmp >> 1);
                if (!decimal) {
                    setZS(acc);
                    carry = (acc & 0x40) != 0;
                    overflow = ((acc & 0x40) ^ ((acc & 0x20) << 1)) != 0;
                } else {
                    sign = carry;
                    zero = acc == 0;
                    overflow = ((tmp ^ acc) & 0x40) != 0;
                    if ((tmp & 0x0f) + (tmp & 0x01) > 5) acc = acc & 0xf0 | (acc + 6) & 0x0f;
                    if (carry = ((tmp + (tmp & 0x10)) & 0x1f0) > 0x50) acc += 0x60;
                }
                break;
            case ASR:
                acc = acc & data;
                nxtcarry = (acc & 0x01) != 0;
                acc = (acc >> 1);
                carry = nxtcarry;
                setZS(acc);
                break;
            case DCP:
                data = (data - 1) & 0xff;
                setZS(data);
                tmp = acc - data;
                carry = tmp >= 0;
                setZS((tmp & 0xff));
                break;
            case ISB:
                data = (data + 1) & 0xff;
                opSBCimp(data);
                break;
            case LAX:
                acc = x = data;
                setZS(acc);
                break;
            case LAS:
                acc = x = s = (data & s);
                setZS(acc);
                break;
            case LXA:
                x = acc = (acc | 0xee) & p1;
                setZS(acc);
                break;
            case RLA:
                data = (data << 1) + (carry ? 1 : 0);
                carry = (data & 0x100) != 0;
                data = data & 0xff;
                acc = acc & data;
                zero = (acc == 0);
                sign = (acc > 0x7f);
                break;
            case RRA:
                nxtcarry = (data & 0x01) != 0;
                data = (data >> 1) + (carry ? 0x80 : 0);
                carry = nxtcarry;
                opADCimp(data);
                break;
            case SBX:
                x = ((acc & x) - p1);
                carry = x >= 0;
                x = x & 0xff;
                setZS(x);
                break;
            case SHA:
                data = acc & x & ((adr >> 8) + 1);
                break;
            case SHS:
                data = acc & x & ((adr >> 8) + 1);
                s = acc & x;
                break;
            case SHX:
                data = x & ((adr >> 8) + 1);
                break;
            case SHY:
                data = y & ((adr >> 8) + 1);
                break;
            case SAX:
                data = acc & x;
                break;
            case SRE:
                carry = (data & 0x01) != 0;
                data = data >> 1;
                acc = acc ^ data;
                setZS(acc);
                break;
            case SLO:
                setCarry(data);
                data = (data << 1) & 0xff;
                acc = acc | data;
                setZS(acc);
                break;
            default:
                unknownInstruction(pc, op);
        }
        if (write) {
            writeByte(adr, data);
        } else if (addrMode == ACCUMULATOR) {
            acc = data;
        }
    }
