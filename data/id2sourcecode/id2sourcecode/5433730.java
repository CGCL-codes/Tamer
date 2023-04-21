    private void checkInt(IntArrayBitWriter writer, IntArrayBitReader reader, int i) {
        for (C coding : getCodings()) {
            if (isEncodableValueLimited(coding) && Math.abs(i) > getMaxEncodableValue(coding)) return;
            writer.setPosition(0);
            coding.encodeInt(writer, i);
            writer.flush();
            reader.setPosition(0);
            int j = coding.decodeInt(reader);
            assertEquals(i, j);
            reader.setPosition(0);
        }
    }
