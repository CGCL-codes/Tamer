    public static void INT10_ScrollWindow(short rul, short cul, short rlr, short clr, byte nlines, short attr, short page) {
        if (Int10_modes.CurMode.type != VGA.M_TEXT) page = 0xff;
        int ncols = Memory.real_readw(Int10.BIOSMEM_SEG, Int10.BIOSMEM_NB_COLS);
        int nrows = Memory.real_readb(Int10.BIOSMEM_SEG, Int10.BIOSMEM_NB_ROWS) + 1;
        if (rul > rlr) return;
        if (cul > clr) return;
        if (rlr >= nrows) rlr = (short) (nrows - 1);
        if (clr >= ncols) clr = (short) (ncols - 1);
        clr++;
        if (page == 0xFF) page = Memory.real_readb(Int10.BIOSMEM_SEG, Int10.BIOSMEM_CURRENT_PAGE);
        int base = Int10_modes.CurMode.pstart + page * Memory.real_readw(Int10.BIOSMEM_SEG, Int10.BIOSMEM_PAGE_SIZE);
        int start = 0, end = 0;
        int next = 0;
        boolean gotofilling = false;
        if (nlines > 0) {
            start = rlr - nlines + 1;
            end = rul;
            next = -1;
        } else if (nlines < 0) {
            start = rul - nlines - 1;
            end = rlr;
            next = 1;
        } else {
            nlines = (byte) (rlr - rul + 1);
            gotofilling = true;
        }
        if (!gotofilling) {
            while (start != end) {
                start += next;
                switch(Int10_modes.CurMode.type) {
                    case VGA.M_TEXT:
                        TEXT_CopyRow(cul, clr, (short) start, (short) (start + nlines), base);
                        break;
                    case VGA.M_CGA2:
                        CGA2_CopyRow(cul, clr, (short) start, (short) (start + nlines), base);
                        break;
                    case VGA.M_CGA4:
                        CGA4_CopyRow(cul, clr, (short) start, (short) (start + nlines), base);
                        break;
                    case VGA.M_TANDY16:
                        TANDY16_CopyRow(cul, clr, (short) start, (short) (start + nlines), base);
                        break;
                    case VGA.M_EGA:
                        EGA16_CopyRow(cul, clr, (short) start, (short) (start + nlines), base);
                        break;
                    case VGA.M_VGA:
                        VGA_CopyRow(cul, clr, (short) start, (short) (start + nlines), base);
                        break;
                    case VGA.M_LIN4:
                        if ((Dosbox.machine == MachineType.MCH_VGA) && (Dosbox.svgaCard == SVGACards.SVGA_TsengET4K) && (Int10_modes.CurMode.swidth <= 800)) {
                            EGA16_CopyRow(cul, clr, (short) start, (short) (start + nlines), base);
                            break;
                        }
                    default:
                        if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_INT10, LogSeverities.LOG_ERROR, "Unhandled mode " + Int10_modes.CurMode.type + " for scroll");
                }
            }
        }
        if (nlines > 0) {
            start = rul;
        } else {
            nlines = (byte) -nlines;
            start = rlr - nlines + 1;
        }
        for (; nlines > 0; nlines--) {
            switch(Int10_modes.CurMode.type) {
                case VGA.M_TEXT:
                    TEXT_FillRow(cul, clr, (short) start, base, attr);
                    break;
                case VGA.M_CGA2:
                    CGA2_FillRow(cul, clr, (short) start, base, attr);
                    break;
                case VGA.M_CGA4:
                    CGA4_FillRow(cul, clr, (short) start, base, attr);
                    break;
                case VGA.M_TANDY16:
                    TANDY16_FillRow(cul, clr, (short) start, base, attr);
                    break;
                case VGA.M_EGA:
                    EGA16_FillRow(cul, clr, (short) start, base, attr);
                    break;
                case VGA.M_VGA:
                    VGA_FillRow(cul, clr, (short) start, base, attr);
                    break;
                case VGA.M_LIN4:
                    if ((Dosbox.machine == MachineType.MCH_VGA) && (Dosbox.svgaCard == SVGACards.SVGA_TsengET4K) && (Int10_modes.CurMode.swidth <= 800)) {
                        EGA16_FillRow(cul, clr, (short) start, base, attr);
                        break;
                    }
                default:
                    if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_INT10, LogSeverities.LOG_ERROR, "Unhandled mode " + Int10_modes.CurMode.type + " for scroll");
            }
            start++;
        }
    }
