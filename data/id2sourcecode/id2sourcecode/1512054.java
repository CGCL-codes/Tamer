    public static void INT10_GetFuncStateInformation(int save) {
        Memory.mem_writed(save, Int10.int10.rom.static_state);
        int i;
        for (i = 0; i < 0x1e; i++) {
            Memory.mem_writeb(save + 0x4 + i, Memory.real_readb(Int10.BIOSMEM_SEG, Int10.BIOSMEM_CURRENT_MODE + i));
        }
        Memory.mem_writeb(save + 0x22, Memory.real_readb(Int10.BIOSMEM_SEG, Int10.BIOSMEM_NB_ROWS) + 1);
        for (i = 1; i < 3; i++) {
            Memory.mem_writeb(save + 0x22 + i, Memory.real_readb(Int10.BIOSMEM_SEG, Int10.BIOSMEM_NB_ROWS + i));
        }
        for (i = 0x25; i < 0x40; i++) Memory.mem_writeb(save + i, 0);
        short dccode = 0x00;
        int vsavept = Memory.real_readd(Int10.BIOSMEM_SEG, Int10.BIOSMEM_VS_POINTER);
        int svstable = Memory.real_readd(Memory.RealSeg(vsavept), Memory.RealOff(vsavept) + 0x10);
        if (svstable != 0) {
            int dcctable = Memory.real_readd(Memory.RealSeg(svstable), Memory.RealOff(svstable) + 0x02);
            short entries = Memory.real_readb(Memory.RealSeg(dcctable), Memory.RealOff(dcctable) + 0x00);
            short idx = Memory.real_readb(Int10.BIOSMEM_SEG, Int10.BIOSMEM_DCC_INDEX);
            if (idx < entries) {
                int dccentry = Memory.real_readw(Memory.RealSeg(dcctable), Memory.RealOff(dcctable) + 0x04 + idx * 2);
                if ((dccentry & 0xff) == 0) dccode = (short) ((dccentry >> 8) & 0xff); else dccode = (short) (dccentry & 0xff);
            }
        }
        Memory.mem_writeb(save + 0x25, dccode);
        int col_count = 0;
        switch(Int10_modes.CurMode.type) {
            case VGA.M_TEXT:
                if (Int10_modes.CurMode.mode == 0x7) col_count = 1; else col_count = 16;
                break;
            case VGA.M_CGA2:
                col_count = 2;
                break;
            case VGA.M_CGA4:
                col_count = 4;
                break;
            case VGA.M_EGA:
                if (Int10_modes.CurMode.mode == 0x11 || Int10_modes.CurMode.mode == 0x0f) col_count = 2; else col_count = 16;
                break;
            case VGA.M_VGA:
                col_count = 256;
                break;
            default:
                if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_INT10, LogSeverities.LOG_ERROR, "Get Func State illegal mode type " + Int10_modes.CurMode.type);
        }
        Memory.mem_writew(save + 0x27, col_count);
        Memory.mem_writeb(save + 0x29, Int10_modes.CurMode.ptotal);
        switch(Int10_modes.CurMode.sheight) {
            case 200:
                Memory.mem_writeb(save + 0x2a, 0);
                break;
            case 350:
                Memory.mem_writeb(save + 0x2a, 1);
                break;
            case 400:
                Memory.mem_writeb(save + 0x2a, 2);
                break;
            case 480:
                Memory.mem_writeb(save + 0x2a, 3);
                break;
        }
        if (Int10_modes.CurMode.type == VGA.M_TEXT) Memory.mem_writeb(save + 0x2d, 0x21); else Memory.mem_writeb(save + 0x2d, 0x01);
        Memory.mem_writeb(save + 0x31, 3);
    }
