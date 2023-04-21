    public int map(RandomAccessFile file, long offset, int addr, int len, boolean read, boolean write, boolean exec) throws MemoryMapException {
        if ((addr % PAGE_SIZE) != 0) {
            MemoryMapException.unalignedAddress(addr);
        }
        int num_pages = (len + PAGE_SIZE - 1) / PAGE_SIZE;
        if (addr == 0) {
            addr = findFreePages(num_pages);
        }
        if (DBT_Options.debugMemory) {
            System.out.println("Mapping file " + file + " offset=" + offset + " addr=0x" + Integer.toHexString(addr) + " len=" + len + (read ? " r" : " -") + (write ? "w" : "-") + (exec ? "x" : "-"));
        }
        try {
            int pte = getPTE(addr);
            if (!HAVE_java_nio_FileChannelImpl_nio_mmap_file) {
                file.seek(offset);
                for (int i = 0; i < num_pages; i++) {
                    if (getPage(pte + i) != null) {
                        throw new Error("Memory map of already mapped location addr=0x" + Integer.toHexString(addr) + " len=" + len);
                    }
                    byte page[] = new byte[PAGE_SIZE];
                    if (i == 0) {
                        file.read(page, getOffset(addr), PAGE_SIZE - getOffset(addr));
                    } else if (i == (num_pages - 1)) {
                        file.read(page, 0, ((len - getOffset(addr)) % PAGE_SIZE));
                    } else {
                        file.read(page);
                    }
                    readableMemory[pte + i] = read ? page : null;
                    writableMemory[pte + i] = write ? page : null;
                    executableMemory[pte + i] = exec ? page : null;
                }
            } else {
                for (int i = 0; i < num_pages; i++) {
                    if (getPage(pte + i) != null) {
                        throw new Error("Memory map of already mapped location addr=0x" + Integer.toHexString(addr) + " len=" + len);
                    }
                    if (read && write) {
                        readableMemory[pte + i] = file.getChannel().map(FileChannel.MapMode.READ_WRITE, offset + (i * PAGE_SIZE), PAGE_SIZE).array();
                        writableMemory[pte + i] = readableMemory[pte + i];
                        if (exec) {
                            executableMemory[pte + i] = readableMemory[pte + i];
                        }
                    } else if (read) {
                        readableMemory[pte + i] = file.getChannel().map(FileChannel.MapMode.READ_ONLY, offset + (i * PAGE_SIZE), PAGE_SIZE).array();
                        if (exec) {
                            executableMemory[pte + i] = readableMemory[pte + i];
                        }
                    } else if (exec) {
                        executableMemory[pte + i] = file.getChannel().map(FileChannel.MapMode.READ_ONLY, offset + (i * PAGE_SIZE), PAGE_SIZE).array();
                    } else {
                        throw new Error("Unable to map address 0x" + Integer.toHexString(addr) + " with permissions " + (read ? "r" : "-") + (write ? "w" : "-") + (exec ? "x" : "-"));
                    }
                }
            }
            return addr;
        } catch (java.io.IOException e) {
            throw new Error(e);
        }
    }
