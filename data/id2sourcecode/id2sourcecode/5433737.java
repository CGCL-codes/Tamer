    private void checkBigInt(IntArrayBitWriter writer, IntArrayBitReader reader, BigInteger i) {
        for (C coding : getCodings()) {
            if (isEncodableValueLimited(coding) && i.abs().compareTo(BigInteger.valueOf(getMaxEncodableValue(coding))) > 0) return;
            writer.setPosition(0);
            coding.encodeBigInt(writer, i);
            writer.flush();
            reader.setPosition(0);
            BigInteger j = coding.decodeBigInt(reader);
            assertEquals(i, j);
            reader.setPosition(0);
        }
    }
