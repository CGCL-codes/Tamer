    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(timeStamp);
        out.writeLong(length);
        out.writeLong(lastModified);
        out.writeLong(lastAccessed);
        out.writeBoolean(execute);
        out.writeBoolean(read);
        out.writeBoolean(write);
        out.writeBoolean(hidden);
        out.writeBoolean(ownerWriteOnly);
        out.writeBoolean(ownerExecOnly);
        out.writeBoolean(ownerReadOnly);
        if (this.dfGuid != null) {
            byte[] dfb = this.dfGuid.getBytes();
            out.writeInt(dfb.length);
            out.write(dfb);
        } else {
            out.writeInt(-1);
        }
        byte[] dfb = this.guid.getBytes();
        out.writeInt(dfb.length);
        out.write(dfb);
        if (this.monitor != null) {
            byte[] mfb = this.monitor.toByteArray();
            out.writeInt(mfb.length);
            out.write(mfb);
        } else {
            out.writeInt(-1);
        }
        out.writeBoolean(vmdk);
        out.writeInt(owner_id);
        out.writeInt(group_id);
        byte[] hmb = ByteUtils.serializeHashMap(extendedAttrs);
        out.writeInt(hmb.length);
        out.write(hmb);
        out.writeBoolean(dedup);
        byte[] vb = this.version.getBytes();
        out.writeInt(vb.length);
        out.write(vb);
    }
