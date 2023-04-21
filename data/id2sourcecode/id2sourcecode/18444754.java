    public int writeAllData(int mapId, ZipOutputStream out) throws IOException {
        int bytesWritten = 0;
        DataOutputStream ds;
        String name;
        out.putNextEntry(new ZipEntry(name = Process.MAP_DIR_PREFIX + Integer.toString(mapId, 16) + Process.MAP_DIR_SEP + this.depthLevel + OBJDATA_SEP + Integer.toString(this.coordX, 16) + OBJDATA_SEP + Integer.toString(this.coordY, 16) + OBJDATA_SUFF));
        ds = new DataOutputStream(out);
        for (int i = 0; i < this.polygons.size(); ++i) {
            bytesWritten += this.polygons.elementAt(i).write(ds);
        }
        for (int i = 0; i < this.polylines.size(); ++i) {
            bytesWritten += this.polylines.elementAt(i).write(ds);
        }
        for (int i = 0; i < this.pois.size(); ++i) {
            bytesWritten += this.pois.elementAt(i).write(ds);
        }
        ds.flush();
        out.closeEntry();
        return bytesWritten;
    }
