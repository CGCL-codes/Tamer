    public void putNextEntry(ZipEntry ze) throws IOException {
        if (firstEntry) {
            byte[] edata = ze.getExtra();
            if (edata != null && !hasMagic(edata)) {
                byte[] tmp = new byte[edata.length + 4];
                System.arraycopy(tmp, 4, edata, 0, edata.length);
                edata = tmp;
            } else {
                edata = new byte[4];
            }
            set16(edata, 0, JAR_MAGIC);
            set16(edata, 2, 0);
            ze.setExtra(edata);
            firstEntry = false;
        }
        super.putNextEntry(ze);
    }
