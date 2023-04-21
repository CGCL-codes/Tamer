    private void rwValuesDouble(final int[] fromPos, final int[] toPosExcl, final IAccessor reader, final IAccessor writer, final ITransformationIndex beforeWrite) {
        IndexIterator ii = new IndexIterator(fromPos, toPosExcl);
        for (; ii.notDone(); ii.incr()) {
            int[] index = ii.value();
            double value = reader.getDouble(index);
            int[] transfIndex = index;
            if (beforeWrite != null) {
                transfIndex = beforeWrite.transform(index);
            }
            writer.setDouble(transfIndex, value);
        }
    }
