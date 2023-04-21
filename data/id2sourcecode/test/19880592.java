    private SnapshotAttribute buildDiffAttribute() {
        SnapshotAttribute ret = new SnapshotAttribute();
        SnapshotAttributeReadValue read1 = this.firstSnapshotAttribute.getReadValue();
        SnapshotAttributeReadValue read2 = this.secondSnapshotAttribute.getReadValue();
        SnapshotAttributeWriteValue write1 = this.firstSnapshotAttribute.getWriteValue();
        SnapshotAttributeWriteValue write2 = this.secondSnapshotAttribute.getWriteValue();
        SnapshotAttributeReadValue readDiff = buildDiffAttributeReadValue(read1, read2);
        SnapshotAttributeWriteValue writeDiff = buildDiffAttributeWriteValue(write1, write2);
        SnapshotAttributeDeltaValue deltaDiff = new SnapshotAttributeDeltaValue(writeDiff, readDiff);
        ret.setReadValue(readDiff);
        ret.setWriteValue(writeDiff);
        ret.setDeltaValue(deltaDiff);
        ret.setDisplayFormat(firstSnapshotAttribute.getDisplayFormat());
        ret.setAttribute_complete_name(this.firstSnapshotAttribute.getAttribute_complete_name() + "-" + this.secondSnapshotAttribute.getAttribute_complete_name());
        if (this.firstSnapshotAttribute.getData_format() != SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT) {
            ret.setData_format(this.firstSnapshotAttribute.getData_format());
        } else if (this.secondSnapshotAttribute.getData_format() != SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT) {
            ret.setData_format(this.secondSnapshotAttribute.getData_format());
        } else {
            ret.setData_format(SnapshotAttributeValue.NOT_APPLICABLE_DATA_FORMAT);
        }
        if (this.firstSnapshotAttribute.getData_type() > 0) {
            ret.setData_type(this.firstSnapshotAttribute.getData_type());
        } else if (this.secondSnapshotAttribute.getData_type() > 0) {
            ret.setData_type(this.secondSnapshotAttribute.getData_type());
        } else {
            ret.setData_type(TangoConst.Tango_DEV_VOID);
        }
        return ret;
    }
