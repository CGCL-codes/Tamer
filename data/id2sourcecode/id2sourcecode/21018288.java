    public void execute(MixVM mix) throws Exception {
        M = A.intValue() + mix.getIndexRegister(I).intValue();
        switch(C) {
            case 0:
                mix.tick(1);
                break;
            case 1:
                if (F == 6) {
                    mix.tick(4);
                    mix.setAccumulator(mix.getAccumulator().doubleValue() + mix.read(M).doubleValue());
                } else {
                    mix.tick(2);
                    mix.setAccumulator(mix.getAccumulator().intValue() + V(mix));
                }
                break;
            case 2:
                if (F == 6) {
                    mix.tick(4);
                    mix.setAccumulator(mix.getAccumulator().doubleValue() - mix.read(M).doubleValue());
                } else {
                    mix.tick(2);
                    mix.setAccumulator(mix.getAccumulator().intValue() - V(mix));
                }
                break;
            case 3:
                if (F == 6) {
                    mix.tick(9);
                    mix.setAccumulator(mix.getAccumulator().doubleValue() * mix.read(M).doubleValue());
                } else {
                    mix.tick(10);
                    mix.setExtendedAccumulator(mix.getAccumulator().intValue() * (long) V(mix));
                }
                break;
            case 4:
                if (F == 6) {
                    mix.tick(11);
                    mix.setAccumulator(mix.getAccumulator().doubleValue() / mix.read(M).doubleValue());
                } else {
                    mix.tick(12);
                    int V = V(mix);
                    long dividend = mix.getExtendedAccumulator().longValue();
                    mix.setExtension((int) dividend % V);
                    long quotient = dividend / V;
                    if (quotient < -MixWord.MAX_VALUE || quotient > MixWord.MAX_VALUE) mix.setAccumulator(0x40000000); else mix.setAccumulator((int) quotient);
                }
                break;
            case 5:
                switch(F) {
                    case 0:
                        mix.tick(10);
                        MixWord w = mix.getExtendedAccumulator();
                        int n = 0;
                        int unit = 1;
                        for (int i = w.size(); i > 0; i--) {
                            int b = w.getByte(i).intValue() % 10;
                            n += b * unit;
                            unit *= 10;
                        }
                        mix.setAccumulator(n * w.sign());
                        break;
                    case 1:
                        mix.tick(10);
                        n = mix.getAccumulator().intValue();
                        String s = String.valueOf(n);
                        if (s.charAt(0) == '-') s = s.substring(1);
                        w = mix.getExtendedAccumulator();
                        for (int i = w.size(); i > 0; i--) {
                            int b = 0;
                            int j = s.length() - w.size() + i - 1;
                            if (j >= 0) b = s.charAt(j) - 18;
                            w.setByte(i, new MixByte(b));
                        }
                        mix.setExtendedAccumulator(w);
                        break;
                    case 2:
                        mix.tick(10);
                        mix.halt();
                        break;
                    case 6:
                        mix.tick(3);
                        mix.setAccumulator((double) mix.getAccumulator().intValue());
                        break;
                    case 7:
                        mix.tick(3);
                        double d = mix.getAccumulator().doubleValue();
                        if (Math.abs(d) < Math.pow(64.0, 5.0)) n = (int) Math.round(d); else n = MixWord.MAX_VALUE * (int) Math.signum(d);
                        mix.setAccumulator(n);
                        break;
                    default:
                        throw new InstructionNotImplementedException(this, "Unknown special instruction!");
                }
                break;
            case 6:
                mix.tick(2);
                if (M <= 0) break;
                switch(F) {
                    case 0:
                        MixWord w = mix.getAccumulator();
                        w.shiftLeft(M);
                        break;
                    case 1:
                        w = mix.getAccumulator();
                        w.shiftRight(M);
                        break;
                    case 2:
                        w = mix.getExtendedAccumulator();
                        w.shiftLeft(M);
                        mix.setExtendedAccumulator(w);
                        break;
                    case 3:
                        w = mix.getExtendedAccumulator();
                        w.shiftRight(M);
                        mix.setExtendedAccumulator(w);
                        break;
                    case 4:
                        w = mix.getExtendedAccumulator();
                        w.rotateLeft(M);
                        mix.setExtendedAccumulator(w);
                        break;
                    case 5:
                        w = mix.getExtendedAccumulator();
                        w.rotateRight(M);
                        mix.setExtendedAccumulator(w);
                        break;
                    case 6:
                        w = mix.getExtendedAccumulator();
                        w.shiftLeftBinary(M);
                        mix.setExtendedAccumulator(w);
                        break;
                    case 7:
                        w = mix.getExtendedAccumulator();
                        w.shiftRightBinary(M);
                        mix.setExtendedAccumulator(w);
                        break;
                    default:
                        throw new InstructionNotImplementedException(this, "Unknown shift instruction!");
                }
                break;
            case 7:
                mix.tick(1 + 2 * F);
                int dest = mix.getIndexRegister(1).intValue();
                for (int i = 0; i < F; i++) mix.write(dest + i, mix.read(M + i));
                break;
            case 8:
                mix.tick(2);
                mix.setAccumulator(V(mix));
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                mix.tick(2);
                mix.setIndexRegister(C - 8, V(mix));
                break;
            case 15:
                mix.tick(2);
                mix.setExtension(V(mix));
                break;
            case 16:
                mix.tick(2);
                mix.setAccumulator(-V(mix));
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
                mix.tick(2);
                mix.setIndexRegister(C - 16, -V(mix));
                break;
            case 23:
                mix.tick(2);
                mix.setExtension(-V(mix));
                break;
            case 24:
                mix.tick(2);
                mix.write(M, new FieldSpecification(F), mix.getAccumulator());
                break;
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
                mix.tick(2);
                mix.write(M, new FieldSpecification(F), mix.getIndexRegister(C - 24));
                break;
            case 31:
                mix.tick(2);
                mix.write(M, new FieldSpecification(F), mix.getExtension());
                break;
            case 32:
                mix.tick(2);
                mix.write(M, new FieldSpecification(F), mix.getJumpRegister());
                break;
            case 33:
                mix.tick(2);
                mix.write(M, new FieldSpecification(F), new MixWord(mix.REGISTER_WIDTH));
                break;
            case 34:
                mix.tick(1);
                if (F < mix.N_IO_UNITS) {
                    if (mix.unit(F).isBusy()) {
                        mix.setJumpRegister();
                        mix.setLocationPtr(M);
                    }
                } else throw new InstructionNotImplementedException(this, "Invalid I/O unit!");
                break;
            case 35:
                mix.tick(1);
                IOUnit u = mix.unit(F);
                switch(F) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        u.seek(M);
                        break;
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                        if (M != 0) throw new InstructionNotImplementedException(this, "Improper I/O control!");
                        break;
                    case 18:
                        if (M != 0) throw new InstructionNotImplementedException(this, "Improper I/O control!");
                        u.pagebreak();
                        break;
                    case 20:
                        if (M != 0) throw new InstructionNotImplementedException(this, "Improper I/O control!");
                        u.seek(0);
                        break;
                    default:
                        throw new InstructionNotImplementedException(this, "Improper I/O control!");
                }
                break;
            case 36:
                mix.tick(1);
                u = mix.unit(F);
                if (u == null) throw new InstructionNotImplementedException(this, "Invalid I/O unit!");
                int blocksize = u.getBlockSize();
                for (int i = 0; i < blocksize; i++) mix.write(M++, u.read());
                break;
            case 37:
                mix.tick(1);
                u = mix.unit(F);
                if (u == null) throw new InstructionNotImplementedException(this, "Invalid I/O unit!");
                blocksize = u.getBlockSize();
                for (int i = 0; i < blocksize; i++) u.write(mix.read(M++));
                if (F >= 16) u.newline();
                break;
            case 38:
                mix.tick(1);
                if (F < mix.N_IO_UNITS) {
                    if (mix.unit(F).isReady()) {
                        mix.setJumpRegister();
                        mix.setLocationPtr(M);
                    }
                } else throw new InstructionNotImplementedException(this, "Invalid I/O unit!");
                break;
            case 39:
                mix.tick(1);
                switch(F) {
                    case 0:
                        mix.setJumpRegister();
                    case 1:
                        mix.setLocationPtr(M);
                        break;
                    case 2:
                        if (mix.overflow()) {
                            mix.setJumpRegister();
                            mix.setLocationPtr(M);
                        }
                        break;
                    case 3:
                        if (!mix.overflow()) {
                            mix.setJumpRegister();
                            mix.setLocationPtr(M);
                        }
                        break;
                    case 4:
                        if (mix.cmpL()) {
                            mix.setJumpRegister();
                            mix.setLocationPtr(M);
                        }
                        break;
                    case 5:
                        if (mix.cmpE()) {
                            mix.setJumpRegister();
                            mix.setLocationPtr(M);
                        }
                        break;
                    case 6:
                        if (mix.cmpG()) {
                            mix.setJumpRegister();
                            mix.setLocationPtr(M);
                        }
                        break;
                    case 7:
                        if (mix.cmpGE()) {
                            mix.setJumpRegister();
                            mix.setLocationPtr(M);
                        }
                        break;
                    case 8:
                        if (mix.cmpNE()) {
                            mix.setJumpRegister();
                            mix.setLocationPtr(M);
                        }
                        break;
                    case 9:
                        if (mix.cmpLE()) {
                            mix.setJumpRegister();
                            mix.setLocationPtr(M);
                        }
                        break;
                    default:
                        throw new InstructionNotImplementedException(this, "Unknown jump instruction!");
                }
                break;
            case 40:
                mix.tick(1);
                conditionalJump(mix, M, mix.getAccumulator().intValue());
                break;
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
                mix.tick(1);
                conditionalJump(mix, M, mix.getIndexRegister(C - 40).intValue());
                break;
            case 47:
                mix.tick(1);
                conditionalJump(mix, M, mix.getExtension().intValue());
                break;
            case 48:
                mix.tick(1);
                switch(F) {
                    case 0:
                        mix.setAccumulator(mix.getAccumulator().intValue() + M);
                        break;
                    case 1:
                        mix.setAccumulator(mix.getAccumulator().intValue() - M);
                        break;
                    case 2:
                        mix.setAccumulator(M);
                        if (M == 0) mix.getAccumulator().setSign(A.sign());
                        break;
                    case 3:
                        mix.setAccumulator(-M);
                        if (M == 0) mix.getAccumulator().setSign(-A.sign());
                        break;
                    default:
                        throw new InstructionNotImplementedException(this, "Unknown address transfer instruction!");
                }
                break;
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
                mix.tick(1);
                switch(F) {
                    case 0:
                        mix.setIndexRegister(C - 48, mix.getIndexRegister(C - 48).intValue() + M);
                        break;
                    case 1:
                        mix.setIndexRegister(C - 48, mix.getIndexRegister(C - 48).intValue() - M);
                        break;
                    case 2:
                        mix.setIndexRegister(C - 48, M);
                        if (M == 0) mix.getIndexRegister(C - 48).setSign(A.sign());
                        break;
                    case 3:
                        mix.setIndexRegister(C - 48, -M);
                        if (M == 0) mix.getIndexRegister(C - 48).setSign(-A.sign());
                        break;
                    default:
                        throw new InstructionNotImplementedException(this, "Unknown address transfer instruction!");
                }
                break;
            case 55:
                mix.tick(1);
                switch(F) {
                    case 0:
                        mix.setExtension(mix.getExtension().intValue() + M);
                        break;
                    case 1:
                        mix.setExtension(mix.getExtension().intValue() - M);
                        break;
                    case 2:
                        mix.setExtension(M);
                        if (M == 0) mix.getExtension().setSign(A.sign());
                        break;
                    case 3:
                        mix.setExtension(-M);
                        if (M == 0) mix.getExtension().setSign(-A.sign());
                        break;
                    default:
                        throw new InstructionNotImplementedException(this, "Unknown address transfer instruction!");
                }
                break;
            case 56:
                if (F == 6) {
                    mix.tick(4);
                    double epsilon = mix.read(0).doubleValue();
                    double diff = mix.getAccumulator().doubleValue() - mix.read(M).doubleValue();
                    int comparison = 0;
                    if (diff > epsilon) comparison = 1;
                    if (diff < -epsilon) comparison = -1;
                    mix.compare(comparison);
                } else {
                    mix.tick(2);
                    mix.compare(mix.getAccumulator().intValue() - V(mix));
                }
                break;
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
                mix.tick(2);
                mix.compare(mix.getIndexRegister(C - 56).intValue() - V(mix));
                break;
            case 63:
                mix.tick(2);
                mix.compare(mix.getExtension().intValue() - V(mix));
                break;
            default:
                throw new InstructionNotImplementedException(this, "Unknown instruction!");
        }
    }
