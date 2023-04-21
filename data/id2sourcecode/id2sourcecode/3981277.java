        boolean GetDirectoryEntry(int drive, boolean copyFlag, int pathname, int buffer, IntRef error) {
            String volumeID;
            String searchName;
            String entryName;
            boolean foundComplete = false;
            boolean foundName;
            boolean nextPart = true;
            String useName = "";
            int entryLength, nameLength;
            error.value = 0;
            searchName = Memory.MEM_StrCopy(pathname + 1, Memory.mem_readb(pathname)).toUpperCase();
            String searchPos = searchName;
            int searchlen = searchName.length();
            if (searchlen > 1 && searchName.indexOf("..") >= 0) if (searchName.charAt(searchlen - 1) == '.') searchName = searchName.substring(0, searchlen - 1);
            int defBuffer = GetDefaultBuffer();
            if (!ReadSectors(GetSubUnit(drive), false, 16, 1, defBuffer)) return false;
            volumeID = Memory.MEM_StrCopy(defBuffer + 1, 5);
            boolean iso = ("CD001".equals(volumeID));
            if (!iso) Log.exit("MSCDEX: GetDirEntry: Not an ISO 9960 CD.");
            int dirEntrySector = Memory.mem_readd(defBuffer + 156 + 2);
            int dirSize = Memory.mem_readd(defBuffer + 156 + 10);
            int index;
            while (dirSize > 0) {
                index = 0;
                if (!ReadSectors(GetSubUnit(drive), false, dirEntrySector, 1, defBuffer)) return false;
                foundName = false;
                if (nextPart) {
                    if (searchPos.length() > 0) {
                        useName = searchPos;
                        int pos = searchPos.indexOf("\\");
                        if (pos >= 0) searchPos = searchPos.substring(pos + 1); else searchPos = "";
                    }
                    if (searchPos.length() == 0) foundComplete = true;
                }
                do {
                    entryLength = Memory.mem_readb(defBuffer + index);
                    if (entryLength == 0) break;
                    nameLength = Memory.mem_readb(defBuffer + index + 32);
                    entryName = Memory.MEM_StrCopy(defBuffer + index + 33, nameLength);
                    if (entryName.equals(useName)) {
                        foundName = true;
                        break;
                    }
                    int longername = entryName.indexOf(';');
                    if (longername >= 0) {
                        if (entryName.substring(0, longername).equals(useName)) {
                            foundName = true;
                            break;
                        }
                    }
                    index += entryLength;
                } while (index + 33 <= 2048);
                if (foundName) {
                    if (foundComplete) {
                        if (copyFlag) {
                            Log.log(LogTypes.LOG_MISC, LogSeverities.LOG_WARN, "MSCDEX: GetDirEntry: Copyflag structure not entirely accurate maybe");
                            byte[] readBuf = new byte[256];
                            byte[] writeBuf = new byte[256];
                            if (entryLength > 256) return false;
                            Memory.MEM_BlockRead(defBuffer + index, readBuf, entryLength);
                            writeBuf[0] = readBuf[1];
                            System.arraycopy(readBuf, 0x2, writeBuf, 1, 4);
                            writeBuf[5] = 0;
                            writeBuf[6] = 8;
                            System.arraycopy(readBuf, 0xa, writeBuf, 7, 4);
                            System.arraycopy(readBuf, 0x12, writeBuf, 0xb, 7);
                            writeBuf[0x12] = readBuf[0x19];
                            writeBuf[0x13] = readBuf[0x1a];
                            writeBuf[0x14] = readBuf[0x1b];
                            System.arraycopy(readBuf, 0x1c, writeBuf, 0x15, 2);
                            writeBuf[0x17] = readBuf[0x20];
                            System.arraycopy(readBuf, 0x21, writeBuf, 0x18, readBuf[0x20] <= 38 ? readBuf[0x20] : 38);
                            Memory.MEM_BlockWrite(buffer, writeBuf, 0x18 + 40);
                        } else {
                            Memory.MEM_BlockCopy(buffer, defBuffer + index, entryLength);
                        }
                        error.value = iso ? 1 : 0;
                        return true;
                    }
                    dirEntrySector = Memory.mem_readd(defBuffer + index + 2);
                    dirSize = Memory.mem_readd(defBuffer + index + 10);
                    nextPart = true;
                } else {
                    dirSize -= 2048;
                    dirEntrySector++;
                    nextPart = false;
                }
            }
            error.value = 2;
            return false;
        }
