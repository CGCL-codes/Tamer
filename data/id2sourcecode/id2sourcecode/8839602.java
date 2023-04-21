    private void LoadPathNodeFile(byte rx, byte ry) {
        if (rx < Config.WORLD_X_MIN || rx > Config.WORLD_X_MAX || ry < Config.WORLD_Y_MIN || ry > Config.WORLD_Y_MAX) {
            _log.warning("Failed to Load PathNode File: invalid region " + rx + "," + ry + "\n");
            return;
        }
        String fname = "./data/pathnode/" + rx + "_" + ry + ".pn";
        short regionoffset = getRegionOffset(rx, ry);
        _log.info("PathFinding Engine: - Loading: " + fname + " -> region offset: " + regionoffset + "X: " + rx + " Y: " + ry);
        File Pn = new File(fname);
        int node = 0, size, index = 0;
        FileChannel roChannel = null;
        try {
            roChannel = new RandomAccessFile(Pn, "r").getChannel();
            size = (int) roChannel.size();
            MappedByteBuffer nodes;
            if (Config.FORCE_GEODATA) nodes = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, size).load(); else nodes = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, size);
            IntBuffer indexs = IntBuffer.allocate(65536);
            while (node < 65536) {
                byte layer = nodes.get(index);
                indexs.put(node++, index);
                index += layer * 10 + 1;
            }
            _pathNodesIndex.put(regionoffset, indexs);
            _pathNodes.put(regionoffset, nodes);
        } catch (Exception e) {
            _log.log(Level.WARNING, "Failed to Load PathNode File: " + fname + " : " + e.getMessage(), e);
        } finally {
            try {
                roChannel.close();
            } catch (Exception e) {
            }
        }
    }
