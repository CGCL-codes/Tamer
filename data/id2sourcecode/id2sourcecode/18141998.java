    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.timeStamp = in.readLong();
        this.length = in.readLong();
        this.lastModified = in.readLong();
        this.lastAccessed = in.readLong();
        this.execute = in.readBoolean();
        this.read = in.readBoolean();
        this.write = in.readBoolean();
        this.hidden = in.readBoolean();
        this.ownerWriteOnly = in.readBoolean();
        this.ownerExecOnly = in.readBoolean();
        this.ownerReadOnly = in.readBoolean();
        int dfgl = in.readInt();
        if (dfgl == -1) {
            this.dfGuid = null;
        } else {
            byte[] dfb = new byte[dfgl];
            in.read(dfb);
            this.dfGuid = new String(dfb);
        }
        int gl = in.readInt();
        byte[] gfb = new byte[gl];
        in.read(gfb);
        this.guid = new String(gfb);
        int ml = in.readInt();
        if (ml == -1) {
            this.monitor = null;
        } else {
            byte[] mlb = new byte[ml];
            in.read(mlb);
            this.monitor = new IOMonitor();
            monitor.fromByteArray(mlb);
        }
        this.vmdk = in.readBoolean();
        in.readInt();
        in.readInt();
        byte[] hmb = new byte[in.readInt()];
        in.read(hmb);
        this.extendedAttrs = ByteUtils.deSerializeHashMap(hmb);
        this.dedup = in.readBoolean();
        try {
            if (in.available() > 0) {
                int vlen = in.readInt();
                byte[] vb = new byte[vlen];
                in.read(vb);
                this.version = new String(vb);
            }
        } catch (Exception e) {
        }
    }
