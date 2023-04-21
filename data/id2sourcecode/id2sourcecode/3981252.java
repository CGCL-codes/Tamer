        int RemoveDrive(int _drive) {
            int idx = MSCDEX_MAX_DRIVES;
            for (int i = 0; i < GetNumDrives(); i++) {
                if (dinfo[i].drive == _drive) {
                    idx = i;
                    break;
                }
            }
            if (idx == MSCDEX_MAX_DRIVES || (idx != 0 && idx != GetNumDrives() - 1)) return 0;
            cdrom[idx].close();
            if (idx == 0) {
                for (int i = 0; i < GetNumDrives(); i++) {
                    if (i == MSCDEX_MAX_DRIVES - 1) {
                        cdrom[i] = null;
                        dinfo[i] = new TDriveInfo();
                    } else {
                        dinfo[i] = dinfo[i + 1];
                        cdrom[i] = cdrom[i + 1];
                    }
                }
            } else {
                cdrom[idx] = null;
                dinfo[idx] = new TDriveInfo();
            }
            numDrives--;
            if (GetNumDrives() == 0) {
                DOS_DeviceHeader devHeader = new DOS_DeviceHeader(Memory.PhysMake(rootDriverHeaderSeg, 0));
                int off = DOS_DeviceHeader.size;
                devHeader.SetStrategy(off + 4);
                devHeader.SetInterrupt(off + 4);
                devHeader.SetDriveLetter(0);
            } else if (idx == 0) {
                DOS_DeviceHeader devHeader = new DOS_DeviceHeader((Memory.PhysMake(rootDriverHeaderSeg, 0)));
                devHeader.SetDriveLetter(GetFirstDrive() + 1);
            }
            return 1;
        }
