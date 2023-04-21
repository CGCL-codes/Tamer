    private DataOutputStream createNewPart(ZipOutputStream out, int mapId, int partNo) throws IOException {
        try {
            out.closeEntry();
        } catch (Exception e) {
        }
        out.putNextEntry(new ZipEntry(Process.MAP_DIR_PREFIX + Integer.toString(mapId, 16) + Process.MAP_DIR_SEP + Integer.toString(this.id, 16) + IDXDATA_SEP + Integer.toString(partNo, 16) + IDXDATA_SUFF));
        return new DataOutputStream(out);
    }
