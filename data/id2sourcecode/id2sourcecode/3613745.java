    public static ByteBuffer LoadMappedFile(String filename) {
        searchpath_t search;
        String netpath;
        pack_t pak;
        filelink_t link;
        File file = null;
        int fileLength = 0;
        FileChannel channel = null;
        FileInputStream input = null;
        ByteBuffer buffer = null;
        file_from_pak = 0;
        try {
            for (Iterator it = fs_links.iterator(); it.hasNext(); ) {
                link = (filelink_t) it.next();
                if (filename.regionMatches(0, link.from, 0, link.fromlength)) {
                    netpath = link.to + filename.substring(link.fromlength);
                    file = new File(netpath);
                    if (file.canRead()) {
                        input = new FileInputStream(file);
                        channel = input.getChannel();
                        fileLength = (int) channel.size();
                        buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileLength);
                        input.close();
                        return buffer;
                    }
                    return null;
                }
            }
            for (search = fs_searchpaths; search != null; search = search.next) {
                if (search.pack != null) {
                    pak = search.pack;
                    filename = filename.toLowerCase();
                    packfile_t entry = (packfile_t) pak.files.get(filename);
                    if (entry != null) {
                        file_from_pak = 1;
                        file = new File(pak.filename);
                        if (!file.canRead()) Com.Error(Defines.ERR_FATAL, "Couldn't reopen " + pak.filename);
                        if (pak.handle == null || !pak.handle.getFD().valid()) {
                            pak.handle = new RandomAccessFile(pak.filename, "r");
                        }
                        if (pak.backbuffer == null) {
                            channel = pak.handle.getChannel();
                            pak.backbuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, pak.handle.length());
                            channel.close();
                        }
                        pak.backbuffer.position(entry.filepos);
                        buffer = pak.backbuffer.slice();
                        buffer.limit(entry.filelen);
                        return buffer;
                    }
                } else {
                    netpath = search.filename + '/' + filename;
                    file = new File(netpath);
                    if (!file.canRead()) continue;
                    input = new FileInputStream(file);
                    channel = input.getChannel();
                    fileLength = (int) channel.size();
                    buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileLength);
                    input.close();
                    return buffer;
                }
            }
        } catch (Exception e) {
        }
        try {
            if (input != null) input.close(); else if (channel != null && channel.isOpen()) channel.close();
        } catch (IOException ioe) {
        }
        return null;
    }
