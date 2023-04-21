    public final int map(RandomAccessFile file, long offset, int addr, int len, boolean read, boolean write, boolean exec) throws MemoryMapException {
        if ((addr % PAGE_SIZE) != 0) {
            MemoryMapException.unalignedAddress(addr);
        }
        if (DBT_Options.debugRuntime) {
            System.out.println("Mapping file " + file + " offset=" + offset + " addr=0x" + Integer.toHexString(addr) + " len=" + len + (read ? " r" : " -") + (write ? "w" : "-") + (exec ? "x" : "-"));
        }
        addr = map(addr, len, read, write, exec);
        try {
            file.seek(offset);
            for (int i = 0; i < len; i += 4) {
                int[] page = getPage(getPTE(addr + i));
                page[getOffset(addr + i)] = readInt(file);
            }
            return addr;
        } catch (java.io.IOException e) {
            throw new Error(e);
        }
    }
