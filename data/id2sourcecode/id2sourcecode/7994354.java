    private int multiplyAndAccumulateOdd(byte[] accumulator, int accumulatorDigits, byte[] xValue, int xDigits, int yValue, int accumulatorIndex) {
        if (yValue == 0) {
            return accumulatorDigits;
        }
        int xWholeBytes = (xDigits + 1) / 2;
        int carry = 0;
        int xPreviousHi = 0;
        for (int xIndex = 0; xIndex < xWholeBytes; xIndex++) {
            byte hiLo = xValue[xIndex];
            int xHi = (hiLo >>> 4) & 0x0F;
            int xLo = hiLo & 0x0F;
            hiLo = accumulator[accumulatorIndex];
            int aHi = (hiLo >>> 4) & 0x0F;
            int aLo = hiLo & 0x0F;
            aLo += xPreviousHi * yValue + carry;
            if (aLo > 9) {
                carry = aLo / 10;
                aLo %= 10;
            } else {
                carry = 0;
            }
            aHi += xLo * yValue + carry;
            if (aHi > 9) {
                carry = aHi / 10;
                aHi %= 10;
            } else {
                carry = 0;
            }
            xPreviousHi = xHi;
            accumulator[accumulatorIndex] = (byte) (aLo | (aHi << 4));
            accumulatorIndex++;
        }
        carry += xPreviousHi * yValue;
        if (carry != 0) {
            while (carry != 0) {
                byte hiLo = accumulator[accumulatorIndex];
                int aHi = (hiLo >>> 4) & 0x0F;
                int aLo = hiLo & 0x0F;
                aLo += carry;
                if (aLo > 9) {
                    carry = aLo / 10;
                    aLo %= 10;
                } else {
                    carry = 0;
                }
                aHi += carry;
                if (aHi > 9) {
                    carry = aHi / 10;
                    aHi %= 10;
                } else {
                    carry = 0;
                }
                accumulator[accumulatorIndex] = (byte) (aLo | (aHi << 4));
                accumulatorIndex++;
            }
        }
        accumulatorDigits = (accumulatorIndex * 2);
        if ((accumulator[accumulatorIndex - 1] & 0xF0) == 0) {
            accumulatorDigits--;
        }
        return accumulatorDigits;
    }
