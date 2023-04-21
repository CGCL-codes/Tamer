    private void checkDouble(IntArrayBitWriter writer, IntArrayBitReader reader, double d) {
        for (C coding : getCodings()) {
            if (isEncodableValueLimited(coding)) return;
            writer.setPosition(0);
            coding.encodeDouble(writer, d);
            writer.flush();
            reader.setPosition(0);
            double e = coding.decodeDouble(reader);
            assertEquals(d, e);
        }
    }
