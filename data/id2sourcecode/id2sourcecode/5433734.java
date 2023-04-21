    private void checkPositiveBigInt(IntArrayBitWriter writer, IntArrayBitReader reader, BigInteger i) {
        for (C coding : getCodings()) {
            if (isEncodableValueLimited(coding) && i.compareTo(BigInteger.valueOf(getMaxEncodableValue(coding))) > 0) return;
            writer.setPosition(0);
            coding.encodePositiveBigInt(writer, i);
            writer.flush();
            reader.setPosition(0);
            BigInteger j = coding.decodePositiveBigInt(reader);
            assertEquals(i, j);
            reader.setPosition(0);
        }
    }
