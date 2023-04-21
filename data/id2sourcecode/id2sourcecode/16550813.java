    @Override
    protected void initObject() {
        primaryKey = new DbValue[] { new DbValue(hostid, Columns.HOSTID.name()) };
        otherFields = new DbValue[] { new DbValue(readgloballimit, Columns.READGLOBALLIMIT.name()), new DbValue(writegloballimit, Columns.WRITEGLOBALLIMIT.name()), new DbValue(readsessionlimit, Columns.READSESSIONLIMIT.name()), new DbValue(writesessionlimit, Columns.WRITESESSIONLIMIT.name()), new DbValue(delayllimit, Columns.DELAYLIMIT.name()), new DbValue(updatedInfo, Columns.UPDATEDINFO.name()) };
        allFields = new DbValue[] { otherFields[0], otherFields[1], otherFields[2], otherFields[3], otherFields[4], otherFields[5], primaryKey[0] };
    }
