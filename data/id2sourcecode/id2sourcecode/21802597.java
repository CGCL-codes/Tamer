    private void rwValuesInteger(final int[] fromPos, final int[] toPosExcl, final IAccessor reader, final IAccessor writer, final ITransformationIndex beforeWrite) {
        IndexIterator ii = new IndexIterator(fromPos, toPosExcl);
        for (; ii.notDone(); ii.incr()) {
            int[] index = ii.value();
            int value = reader.getInt(index);
            int[] transfIndex = index;
            if (beforeWrite != null) {
                transfIndex = beforeWrite.transform(index);
            }
            writer.setInt(transfIndex, value);
        }
    }
