    private void rwValuesFloat(final int[] fromPos, final int[] toPosExcl, final IAccessor reader, final IAccessor writer, final ITransformationIndex beforeWrite) {
        IndexIterator ii = new IndexIterator(fromPos, toPosExcl);
        for (; ii.notDone(); ii.incr()) {
            int[] index = ii.value();
            float value = reader.getFloat(index);
            int[] transfIndex = index;
            if (beforeWrite != null) {
                transfIndex = beforeWrite.transform(index);
            }
            writer.setFloat(transfIndex, value);
        }
    }
