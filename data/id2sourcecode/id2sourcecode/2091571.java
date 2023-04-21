    protected int codeNum(int low, int high, NumContext ctx, int v) throws IOException {
        boolean negative = false;
        int cutoff = 0;
        int ictx = ctx.intValue();
        if (ictx >= bitcells.size()) {
            throw new ArrayIndexOutOfBoundsException("JB2Image bad numcontext");
        }
        for (int phase = 1, range = -1; range != 1; ictx = ctx.intValue()) {
            boolean decision;
            if (ictx == 0) {
                ictx = bitcells.size();
                ctx.set(ictx);
                final BitContext pbitcells = new BitContext();
                final NumContext pleftcell = new NumContext();
                final NumContext prightcell = new NumContext();
                bitcells.addElement(pbitcells);
                leftcell.addElement(pleftcell);
                rightcell.addElement(prightcell);
                decision = encoding ? (((low < cutoff) && (high >= cutoff)) ? codeBit((v >= cutoff), pbitcells) : (v >= cutoff)) : ((low >= cutoff) || ((high >= cutoff) && codeBit(false, pbitcells)));
                ctx = (decision ? prightcell : pleftcell);
            } else {
                decision = encoding ? (((low < cutoff) && (high >= cutoff)) ? codeBit((v >= cutoff), (BitContext) bitcells.elementAt(ictx)) : (v >= cutoff)) : ((low >= cutoff) || ((high >= cutoff) && codeBit(false, (BitContext) bitcells.elementAt(ictx))));
                ctx = (NumContext) (decision ? rightcell.elementAt(ictx) : leftcell.elementAt(ictx));
            }
            switch(phase) {
                case 1:
                    {
                        negative = !decision;
                        if (negative) {
                            if (encoding) {
                                v = -v - 1;
                            }
                            final int temp = -low - 1;
                            low = -high - 1;
                            high = temp;
                        }
                        phase = 2;
                        cutoff = 1;
                        break;
                    }
                case 2:
                    {
                        if (!decision) {
                            phase = 3;
                            range = (cutoff + 1) / 2;
                            if (range == 1) {
                                cutoff = 0;
                            } else {
                                cutoff -= (range / 2);
                            }
                        } else {
                            cutoff = (2 * cutoff) + 1;
                        }
                        break;
                    }
                case 3:
                    {
                        range /= 2;
                        if (range != 1) {
                            if (!decision) {
                                cutoff -= (range / 2);
                            } else {
                                cutoff += (range / 2);
                            }
                        } else if (!decision) {
                            cutoff--;
                        }
                        break;
                    }
            }
        }
        return negative ? (-cutoff - 1) : cutoff;
    }
