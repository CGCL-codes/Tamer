    public boolean emulateOP(long maxCycles) throws EmulationException {
        int pc = readRegister(PC);
        long startCycles = cycles;
        if (interruptsEnabled && servicedInterrupt == -1 && interruptMax >= 0) {
            pc = serviceInterrupt(pc);
        }
        if (cpuOff || flash.blocksCPU()) {
            while (cycles >= nextEventCycles) {
                executeEvents();
            }
            if (interruptsEnabled && interruptMax > 0) {
                return false;
            }
            if (maxCycles >= 0 && maxCycles < nextEventCycles) {
                cycles = cycles < maxCycles ? maxCycles : cycles;
            } else {
                cycles = nextEventCycles;
            }
            return false;
        }
        if (breakPoints[pc] != null) {
            if (breakpointActive) {
                breakPoints[pc].cpuAction(CPUMonitor.EXECUTE, pc, 0);
                breakpointActive = false;
                return false;
            }
            breakpointActive = true;
        }
        if (globalMonitor != null) {
            globalMonitor.cpuAction(CPUMonitor.EXECUTE, pc, 0);
        }
        instruction = read(pc, true);
        op = instruction >> 12;
        int sp = 0;
        int sr = 0;
        boolean word = (instruction & 0x40) == 0;
        int dstRegister = 0;
        int dstAddress = -1;
        boolean dstRegMode = false;
        int dst = 0;
        boolean write = false;
        boolean updateStatus = true;
        pc += 2;
        writeRegister(PC, pc);
        switch(op) {
            case 0:
                op = instruction & 0xf0f0;
                System.out.println("Executing MSP430X instruction op:" + Utils.hex16(op) + " ins:" + Utils.hex16(instruction) + " PC = " + Utils.hex16(pc - 2));
                int src = 0;
                int srcData = (instruction & 0x0f00) >> 8;
                int dstData = (instruction & 0x000f);
                switch(op) {
                    case MOVA_IMM2REG:
                        src = read(pc, true);
                        writeRegister(PC, pc += 2);
                        dst = src + (srcData << 16);
                        System.out.println("*** Writing $" + Utils.hex16(dst) + " to reg: " + dstData);
                        writeRegister(dstData, dst);
                        break;
                    default:
                        System.out.println("MSP430X instructions not yet supported...");
                }
                break;
            case 1:
                {
                    dstRegister = instruction & 0xf;
                    int ad = (instruction >> 4) & 3;
                    int nxtCarry = 0;
                    op = instruction & 0xff80;
                    if (op == PUSH || op == CALL) {
                        sp = readRegister(SP) - 2;
                        writeRegister(SP, sp);
                    }
                    if ((dstRegister == CG1 && ad > AM_INDEX) || dstRegister == CG2) {
                        dstRegMode = true;
                        cycles++;
                    } else {
                        switch(ad) {
                            case AM_REG:
                                dstRegMode = true;
                                cycles++;
                                break;
                            case AM_INDEX:
                                dstAddress = readRegisterCG(dstRegister, ad) + read(pc, true);
                                pc += 2;
                                writeRegister(PC, pc);
                                cycles += 4;
                                break;
                            case AM_IND_REG:
                                dstAddress = readRegister(dstRegister);
                                cycles += 3;
                                break;
                            case AM_IND_AUTOINC:
                                if (dstRegister == PC) {
                                    dstAddress = readRegister(PC);
                                    pc += 2;
                                    writeRegister(PC, pc);
                                } else {
                                    dstAddress = readRegister(dstRegister);
                                    writeRegister(dstRegister, dstAddress + (word ? 2 : 1));
                                }
                                cycles += 3;
                                break;
                        }
                    }
                    if (dstRegMode) {
                        dst = readRegisterCG(dstRegister, ad);
                        if (!word) {
                            dst &= 0xff;
                        }
                    } else {
                        dst = read(dstAddress, word);
                    }
                    switch(op) {
                        case RRC:
                            nxtCarry = (dst & 1) > 0 ? CARRY : 0;
                            dst = dst >> 1;
                            if (word) {
                                dst |= (readRegister(SR) & CARRY) > 0 ? 0x8000 : 0;
                            } else {
                                dst |= (readRegister(SR) & CARRY) > 0 ? 0x80 : 0;
                            }
                            write = true;
                            writeRegister(SR, (readRegister(SR) & ~(CARRY | OVERFLOW)) | nxtCarry);
                            break;
                        case SWPB:
                            int tmp = dst;
                            dst = ((tmp >> 8) & 0xff) + ((tmp << 8) & 0xff00);
                            write = true;
                            break;
                        case RRA:
                            nxtCarry = (dst & 1) > 0 ? CARRY : 0;
                            if (word) {
                                dst = (dst & 0x8000) | (dst >> 1);
                            } else {
                                dst = (dst & 0x80) | (dst >> 1);
                            }
                            write = true;
                            writeRegister(SR, (readRegister(SR) & ~(CARRY | OVERFLOW)) | nxtCarry);
                            break;
                        case SXT:
                            sr = readRegister(SR);
                            dst = (dst & 0x80) > 0 ? dst | 0xff00 : dst & 0x7f;
                            write = true;
                            sr = sr & ~(CARRY | OVERFLOW);
                            if (dst != 0) {
                                sr |= CARRY;
                            }
                            writeRegister(SR, sr);
                            break;
                        case PUSH:
                            if (word) {
                                write(sp, dst, true);
                            } else {
                                write(sp, dst & 0xff, true);
                            }
                            cycles += (ad == AM_REG || ad == AM_IND_AUTOINC) ? 2 : 1;
                            write = false;
                            updateStatus = false;
                            break;
                        case CALL:
                            pc = readRegister(PC);
                            write(sp, pc, true);
                            writeRegister(PC, dst);
                            cycles += (ad == AM_REG) ? 3 : (ad == AM_IND_AUTOINC) ? 2 : 1;
                            if (profiler != null) {
                                MapEntry function = map.getEntry(dst);
                                if (function == null) {
                                    function = getFunction(map, dst);
                                }
                                profiler.profileCall(function, cpuCycles, pc);
                            }
                            write = false;
                            updateStatus = false;
                            break;
                        case RETI:
                            servicedInterrupt = -1;
                            sp = readRegister(SP);
                            writeRegister(SR, read(sp, true));
                            sp = sp + 2;
                            writeRegister(PC, read(sp, true));
                            sp = sp + 2;
                            writeRegister(SP, sp);
                            write = false;
                            updateStatus = false;
                            cycles += 4;
                            if (debugInterrupts) {
                                System.out.println("### RETI at " + pc + " => " + reg[PC] + " SP after: " + reg[SP]);
                            }
                            if (profiler != null) {
                                profiler.profileRETI(cycles);
                            }
                            handlePendingInterrupts();
                            break;
                        default:
                            System.out.println("Error: Not implemented instruction:" + instruction);
                    }
                }
                break;
            case 2:
            case 3:
                int jmpOffset = instruction & 0x3ff;
                jmpOffset = (jmpOffset & 0x200) == 0 ? 2 * jmpOffset : -(2 * (0x200 - (jmpOffset & 0x1ff)));
                boolean jump = false;
                cycles += 2;
                sr = readRegister(SR);
                switch(instruction & 0xfc00) {
                    case JNE:
                        jump = (sr & ZERO) == 0;
                        break;
                    case JEQ:
                        jump = (sr & ZERO) > 0;
                        break;
                    case JNC:
                        jump = (sr & CARRY) == 0;
                        break;
                    case JC:
                        jump = (sr & CARRY) > 0;
                        break;
                    case JN:
                        jump = (sr & NEGATIVE) > 0;
                        break;
                    case JGE:
                        jump = (sr & NEGATIVE) > 0 == (sr & OVERFLOW) > 0;
                        break;
                    case JL:
                        jump = (sr & NEGATIVE) > 0 != (sr & OVERFLOW) > 0;
                        break;
                    case JMP:
                        jump = true;
                        break;
                    default:
                        System.out.println("Not implemented instruction: " + Utils.binary16(instruction));
                }
                if (jump) {
                    writeRegister(PC, pc + jmpOffset);
                }
                updateStatus = false;
                break;
            default:
                dstRegister = instruction & 0xf;
                int srcRegister = (instruction >> 8) & 0xf;
                int as = (instruction >> 4) & 3;
                dstRegMode = ((instruction >> 7) & 1) == 0;
                dstAddress = -1;
                int srcAddress = -1;
                src = 0;
                if ((srcRegister == CG1 && as > AM_INDEX) || srcRegister == CG2) {
                    src = CREG_VALUES[srcRegister - 2][as];
                    if (!word) {
                        src &= 0xff;
                    }
                    cycles += dstRegMode ? 1 : 4;
                } else {
                    switch(as) {
                        case AM_REG:
                            src = readRegister(srcRegister);
                            if (!word) {
                                src &= 0xff;
                            }
                            cycles += dstRegMode ? 1 : 4;
                            if (dstRegister == PC) cycles++;
                            break;
                        case AM_INDEX:
                            srcAddress = readRegisterCG(srcRegister, as) + read(pc, true);
                            incRegister(PC, 2);
                            cycles += dstRegMode ? 3 : 6;
                            break;
                        case AM_IND_REG:
                            srcAddress = readRegister(srcRegister);
                            cycles += dstRegMode ? 2 : 5;
                            break;
                        case AM_IND_AUTOINC:
                            if (srcRegister == PC) {
                                srcAddress = readRegister(PC);
                                pc += 2;
                                incRegister(PC, 2);
                                cycles += dstRegMode ? 2 : 5;
                            } else {
                                srcAddress = readRegister(srcRegister);
                                incRegister(srcRegister, word ? 2 : 1);
                                cycles += dstRegMode ? 2 : 5;
                            }
                            if (dstRegister == PC) {
                                cycles++;
                            }
                            break;
                    }
                }
                if (dstRegMode) {
                    if (op != MOV) {
                        dst = readRegister(dstRegister);
                        if (!word) {
                            dst &= 0xff;
                        }
                    }
                } else {
                    pc = readRegister(PC);
                    if (dstRegister == 2) {
                        dstAddress = read(pc, true);
                    } else {
                        dstAddress = readRegister(dstRegister) + read(pc, true);
                    }
                    if (op != MOV) dst = read(dstAddress, word);
                    pc += 2;
                    incRegister(PC, 2);
                }
                if (srcAddress != -1) {
                    srcAddress = srcAddress & 0xffff;
                    src = read(srcAddress, word);
                }
                int tmp = 0;
                int tmpAdd = 0;
                switch(op) {
                    case MOV:
                        dst = src;
                        write = true;
                        updateStatus = false;
                        if (instruction == RETURN && profiler != null) {
                            profiler.profileReturn(cpuCycles);
                        }
                        break;
                    case SUB:
                        tmpAdd = 1;
                    case SUBC:
                        src = (src ^ 0xffff) & 0xffff;
                    case ADDC:
                        if (op == ADDC || op == SUBC) tmpAdd = ((readRegister(SR) & CARRY) > 0) ? 1 : 0;
                    case ADD:
                        sr = readRegister(SR);
                        sr &= ~(OVERFLOW | CARRY);
                        tmp = (src ^ dst) & (word ? 0x8000 : 0x80);
                        dst = dst + src + tmpAdd;
                        if (dst > (word ? 0xffff : 0xff)) {
                            sr |= CARRY;
                        }
                        if (tmp == 0 && ((src ^ dst) & (word ? 0x8000 : 0x80)) != 0) {
                            sr |= OVERFLOW;
                        }
                        writeRegister(SR, sr);
                        write = true;
                        break;
                    case CMP:
                        int b = word ? 0x8000 : 0x80;
                        sr = readRegister(SR);
                        sr = (sr & ~(CARRY | OVERFLOW)) | (dst >= src ? CARRY : 0);
                        tmp = (dst - src);
                        if (((src ^ tmp) & b) == 0 && (((src ^ dst) & b) != 0)) {
                            sr |= OVERFLOW;
                        }
                        writeRegister(SR, sr);
                        dst = tmp;
                        break;
                    case DADD:
                        if (DEBUG) System.out.println("DADD: Decimal add executed - result error!!!");
                        dst = dst + src + ((readRegister(SR) & CARRY) > 0 ? 1 : 0);
                        write = true;
                        break;
                    case BIT:
                        dst = src & dst;
                        sr = readRegister(SR);
                        sr = sr & ~(CARRY | OVERFLOW);
                        if (dst != 0) {
                            sr |= CARRY;
                        }
                        writeRegister(SR, sr);
                        break;
                    case BIC:
                        dst = (~src) & dst;
                        write = true;
                        updateStatus = false;
                        break;
                    case BIS:
                        dst = src | dst;
                        write = true;
                        updateStatus = false;
                        break;
                    case XOR:
                        sr = readRegister(SR);
                        sr = sr & ~(CARRY | OVERFLOW);
                        if ((src & (word ? 0x8000 : 0x80)) != 0 && (dst & (word ? 0x8000 : 0x80)) != 0) {
                            sr |= OVERFLOW;
                        }
                        dst = src ^ dst;
                        if (dst != 0) {
                            sr |= CARRY;
                        }
                        write = true;
                        writeRegister(SR, sr);
                        break;
                    case AND:
                        sr = readRegister(SR);
                        sr = sr & ~(CARRY | OVERFLOW);
                        dst = src & dst;
                        if (dst != 0) {
                            sr |= CARRY;
                        }
                        write = true;
                        writeRegister(SR, sr);
                        break;
                    default:
                        System.out.println("DoubleOperand not implemented: op = " + op + " at " + pc);
                        if (EXCEPTION_ON_BAD_OPERATION) {
                            EmulationException ex = new EmulationException("Bad operation: " + op + " at " + pc);
                            ex.initCause(new Throwable("" + pc));
                            throw ex;
                        }
                }
        }
        if (word) {
            dst &= 0xffff;
        } else {
            dst &= 0xff;
        }
        if (write) {
            if (dstRegMode) {
                writeRegister(dstRegister, dst);
            } else {
                dstAddress &= 0xffff;
                write(dstAddress, dst, word);
            }
        }
        if (updateStatus) {
            sr = readRegister(SR);
            sr = (sr & ~(ZERO | NEGATIVE)) | ((dst == 0) ? ZERO : 0) | (word ? ((dst & 0x8000) > 0 ? NEGATIVE : 0) : ((dst & 0x80) > 0 ? NEGATIVE : 0));
            writeRegister(SR, sr);
        }
        while (cycles >= nextEventCycles) {
            executeEvents();
        }
        cpuCycles += cycles - startCycles;
        return true;
    }
