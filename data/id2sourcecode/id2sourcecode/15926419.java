    BtreeLeaf(BtreeIndex index, Session session, DataPage s) throws SQLException {
        super(index);
        writePos = s.readByte() == 'P';
        if (writePos) {
            int size = s.readInt();
            pageData = ObjectArray.newInstance(size);
            for (int i = 0; i < size; i++) {
                Row r = index.getRow(session, s.readInt());
                pageData.add(r);
            }
        } else {
            pageData = index.readRowArray(s);
        }
    }
