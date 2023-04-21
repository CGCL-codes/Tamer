    private static int MSCDEX_IOCTL_Input(int buffer, short drive_unit) {
        int ioctl_fct = Memory.mem_readb(buffer);
        if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_MISC, LogSeverities.LOG_ERROR, "MSCDEX: IOCTL INPUT Subfunction " + Integer.toString(ioctl_fct, 16));
        switch(ioctl_fct) {
            case 0x00:
                Memory.mem_writed(buffer + 1, Memory.RealMake(mscdex.rootDriverHeaderSeg, 0));
                break;
            case 0x01:
                {
                    Dos_cdrom.TMSF pos = new Dos_cdrom.TMSF();
                    mscdex.GetCurrentPos(drive_unit, pos);
                    short addr_mode = Memory.mem_readb(buffer + 1);
                    if (addr_mode == 0) {
                        long frames = pos.min * 60 * Dos_cdrom.CD_FPS + pos.sec * Dos_cdrom.CD_FPS + pos.fr;
                        if (frames < 150) if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_MISC, LogSeverities.LOG_ERROR, "MSCDEX: Get position: invalid position " + pos.min + ":" + pos.sec + ":" + pos.fr); else frames -= 150;
                        Memory.mem_writed(buffer + 2, (int) frames);
                    } else if (addr_mode == 1) {
                        Memory.mem_writeb(buffer + 2, pos.fr);
                        Memory.mem_writeb(buffer + 3, pos.sec);
                        Memory.mem_writeb(buffer + 4, pos.min);
                        Memory.mem_writeb(buffer + 5, 0x00);
                    } else {
                        if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_MISC, LogSeverities.LOG_ERROR, "MSCDEX: Get position: invalid address mode " + Integer.toString(addr_mode, 16));
                        return 0x03;
                    }
                }
                break;
            case 0x04:
                Dos_cdrom.TCtrl ctrl = new Dos_cdrom.TCtrl();
                if (!mscdex.GetChannelControl(drive_unit, ctrl)) return 0x01;
                for (int chan = 0; chan < 4; chan++) {
                    Memory.mem_writeb(buffer + chan * 2 + 1, ctrl.out[chan]);
                    Memory.mem_writeb(buffer + chan * 2 + 2, ctrl.vol[chan]);
                }
                break;
            case 0x06:
                Memory.mem_writed(buffer + 1, (int) mscdex.GetDeviceStatus(drive_unit));
                break;
            case 0x07:
                if (Memory.mem_readb(buffer + 1) == 0) Memory.mem_writed(buffer + 2, 2048); else if (Memory.mem_readb(buffer + 1) == 1) Memory.mem_writed(buffer + 2, 2352); else return 0x03;
                break;
            case 0x08:
                Memory.mem_writed(buffer + 1, (int) mscdex.GetVolumeSize(drive_unit));
                break;
            case 0x09:
                ShortRef status = new ShortRef();
                if (!mscdex.GetMediaStatus(drive_unit, status)) {
                    status.value = 0;
                }
                Memory.mem_writeb(buffer + 1, status.value);
                break;
            case 0x0A:
                ShortRef tr1 = new ShortRef(), tr2 = new ShortRef();
                Dos_cdrom.TMSF leadOut = new Dos_cdrom.TMSF();
                if (!mscdex.GetCDInfo(drive_unit, tr1, tr2, leadOut)) return 0x05;
                Memory.mem_writeb(buffer + 1, tr1.value);
                Memory.mem_writeb(buffer + 2, tr2.value);
                Memory.mem_writeb(buffer + 3, leadOut.fr);
                Memory.mem_writeb(buffer + 4, leadOut.sec);
                Memory.mem_writeb(buffer + 5, leadOut.min);
                Memory.mem_writeb(buffer + 6, 0x00);
                break;
            case 0x0B:
                {
                    ShortRef attr = new ShortRef();
                    Dos_cdrom.TMSF start = new Dos_cdrom.TMSF();
                    short track = Memory.mem_readb(buffer + 1);
                    mscdex.GetTrackInfo(drive_unit, track, attr, start);
                    Memory.mem_writeb(buffer + 2, start.fr);
                    Memory.mem_writeb(buffer + 3, start.sec);
                    Memory.mem_writeb(buffer + 4, start.min);
                    Memory.mem_writeb(buffer + 5, 0x00);
                    Memory.mem_writeb(buffer + 6, attr.value);
                    break;
                }
            case 0x0C:
                {
                    ShortRef attr = new ShortRef(), track = new ShortRef(), index = new ShortRef();
                    Dos_cdrom.TMSF abs = new Dos_cdrom.TMSF(), rel = new Dos_cdrom.TMSF();
                    mscdex.GetSubChannelData(drive_unit, attr, track, index, rel, abs);
                    Memory.mem_writeb(buffer + 1, attr.value);
                    Memory.mem_writeb(buffer + 2, track.value);
                    Memory.mem_writeb(buffer + 3, index.value);
                    Memory.mem_writeb(buffer + 4, rel.min);
                    Memory.mem_writeb(buffer + 5, rel.sec);
                    Memory.mem_writeb(buffer + 6, rel.fr);
                    Memory.mem_writeb(buffer + 7, 0x00);
                    Memory.mem_writeb(buffer + 8, abs.min);
                    Memory.mem_writeb(buffer + 9, abs.sec);
                    Memory.mem_writeb(buffer + 10, abs.fr);
                    break;
                }
            case 0x0E:
                {
                    ShortRef attr = new ShortRef();
                    StringRef upc = new StringRef();
                    mscdex.GetUPC(drive_unit, attr, upc);
                    Memory.mem_writeb(buffer + 1, attr.value);
                    for (int i = 0; i < 7; i++) Memory.mem_writeb(buffer + 2 + i, upc.value.charAt(i));
                    Memory.mem_writeb(buffer + 9, 0x00);
                    break;
                }
            case 0x0F:
                {
                    BooleanRef playing = new BooleanRef(), pause = new BooleanRef();
                    Dos_cdrom.TMSF resStart = new Dos_cdrom.TMSF(), resEnd = new Dos_cdrom.TMSF();
                    mscdex.GetAudioStatus(drive_unit, playing, pause, resStart, resEnd);
                    Memory.mem_writeb(buffer + 1, pause.value ? 1 : 0);
                    Memory.mem_writeb(buffer + 3, resStart.min);
                    Memory.mem_writeb(buffer + 4, resStart.sec);
                    Memory.mem_writeb(buffer + 5, resStart.fr);
                    Memory.mem_writeb(buffer + 6, 0x00);
                    Memory.mem_writeb(buffer + 7, resEnd.min);
                    Memory.mem_writeb(buffer + 8, resEnd.sec);
                    Memory.mem_writeb(buffer + 9, resEnd.fr);
                    Memory.mem_writeb(buffer + 10, 0x00);
                    break;
                }
            default:
                if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_MISC, LogSeverities.LOG_ERROR, "MSCDEX: Unsupported IOCTL INPUT Subfunction " + Integer.toString(ioctl_fct, 16));
                return 0x03;
        }
        return 0x00;
    }
