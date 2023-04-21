    private static byte[] numberToCalledPartyBCDHelper(String number, boolean includeLength) {
        int numberLenReal = number.length();
        int numberLenEffective = numberLenReal;
        boolean hasPlus = number.indexOf('+') != -1;
        if (hasPlus) numberLenEffective--;
        if (numberLenEffective == 0) return null;
        int resultLen = (numberLenEffective + 1) / 2;
        int extraBytes = 1;
        if (includeLength) extraBytes++;
        resultLen += extraBytes;
        byte[] result = new byte[resultLen];
        int digitCount = 0;
        for (int i = 0; i < numberLenReal; i++) {
            char c = number.charAt(i);
            if (c == '+') continue;
            int shift = ((digitCount & 0x01) == 1) ? 4 : 0;
            result[extraBytes + (digitCount >> 1)] |= (byte) ((charToBCD(c) & 0x0F) << shift);
            digitCount++;
        }
        if ((digitCount & 0x01) == 1) result[extraBytes + (digitCount >> 1)] |= 0xF0;
        int offset = 0;
        if (includeLength) result[offset++] = (byte) (resultLen - 1);
        result[offset] = (byte) (hasPlus ? TOA_International : TOA_Unknown);
        return result;
    }
