    private void checkDecimal(IntArrayBitWriter writer, IntArrayBitReader reader, BigDecimal d) {
        for (C coding : getCodings()) {
            if (isEncodableValueLimited(coding)) return;
            writer.setPosition(0);
            coding.encodeDecimal(writer, d);
            writer.flush();
            reader.setPosition(0);
            BigDecimal e = coding.decodeDecimal(reader);
            assertEquals(d, e);
            reader.setPosition(0);
        }
    }
