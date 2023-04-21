    static pack_t LoadPackFile(String packfile) {
        dpackheader_t header;
        Hashtable newfiles;
        RandomAccessFile file;
        int numpackfiles = 0;
        pack_t pack = null;
        try {
            file = new RandomAccessFile(packfile, "r");
            FileChannel fc = file.getChannel();
            ByteBuffer packhandle = fc.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            packhandle.order(ByteOrder.LITTLE_ENDIAN);
            fc.close();
            if (packhandle == null || packhandle.limit() < 1) return null;
            header = new dpackheader_t();
            header.ident = packhandle.getInt();
            header.dirofs = packhandle.getInt();
            header.dirlen = packhandle.getInt();
            if (header.ident != IDPAKHEADER) Com.Error(Defines.ERR_FATAL, packfile + " is not a packfile");
            numpackfiles = header.dirlen / packfile_t.SIZE;
            if (numpackfiles > MAX_FILES_IN_PACK) Com.Error(Defines.ERR_FATAL, packfile + " has " + numpackfiles + " files");
            newfiles = new Hashtable(numpackfiles);
            packhandle.position(header.dirofs);
            packfile_t entry = null;
            for (int i = 0; i < numpackfiles; i++) {
                packhandle.get(tmpText);
                entry = new packfile_t();
                entry.name = new String(tmpText).trim();
                entry.filepos = packhandle.getInt();
                entry.filelen = packhandle.getInt();
                newfiles.put(entry.name.toLowerCase(), entry);
            }
        } catch (IOException e) {
            Com.DPrintf(e.getMessage() + '\n');
            return null;
        }
        pack = new pack_t();
        pack.filename = new String(packfile);
        pack.handle = file;
        pack.numfiles = numpackfiles;
        pack.files = newfiles;
        Com.Printf("Added packfile " + packfile + " (" + numpackfiles + " files)\n");
        return pack;
    }
