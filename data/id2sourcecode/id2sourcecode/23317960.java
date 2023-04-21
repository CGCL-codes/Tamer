    public void writeAllData(int mapId, ZipOutputStream out, int dataSize) throws IOException {
        DataOutputStream ds;
        out.putNextEntry(new ZipEntry(Process.MAP_DIR_PREFIX + mapId + Process.MAP_DIR_SEP + MAP_META));
        ds = new DataOutputStream(out);
        ds.writeInt(mapId);
        ds.writeUTF(this.name);
        ds.writeInt(this.boundWest);
        ds.writeInt(this.boundSouth);
        ds.writeInt(this.boundEast);
        ds.writeInt(this.boundNorth);
        ds.writeByte(this.zoomLevels);
        for (int i = 0; i < this.zoomLevels; ++i) {
            ds.writeInt(this.rectangleSizesX[i]);
            ds.writeInt(this.rectangleSizesY[i]);
        }
        if (dataSize == -1) {
            ds.writeInt(-1);
        } else {
            dataSize += ds.size() + 8;
            ds.writeInt(dataSize);
        }
        ds.writeInt(this.indexesPresent);
        ds.flush();
        out.closeEntry();
    }
