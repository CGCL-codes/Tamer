    public BCD subtract(BCD val) {
        if (val.m_signum == 0) {
            return this;
        } else if (m_signum == 0) {
            return val.negate();
        } else if (((m_digits > val.m_digits) ? m_digits : val.m_digits) < DIGITS_LONG_MIN - 1) {
            long x = longValue();
            long y = val.longValue();
            return new BCD(x - y);
        } else if (val.m_signum == m_signum) {
            byte[] xVal = m_magnitude;
            int xDigits = m_digits;
            byte[] yVal = val.m_magnitude;
            int yDigits = val.m_digits;
            int cmp = compareTo(val);
            if (((cmp > 0) && (m_signum > 0)) || ((cmp < 0) && (m_signum < 0))) {
                xVal = val.m_magnitude;
                xDigits = val.m_digits;
                yVal = m_magnitude;
                yDigits = m_digits;
            }
            int zDigits = yDigits;
            byte[] zVal = new byte[(zDigits / 2) + 1];
            int carry = 0;
            int index = 0;
            int endIndex = (xDigits + 1) / 2;
            while (index < endIndex) {
                byte hiLo = xVal[index];
                int xHi = (hiLo >>> 4) & 0x0F;
                int xLo = hiLo & 0x0F;
                hiLo = yVal[index];
                int yHi = (hiLo >>> 4) & 0x0F;
                int yLo = hiLo & 0x0F;
                int zLo = yLo - xLo - carry;
                if (zLo < 0) {
                    carry = 1;
                    zLo += 10;
                } else {
                    carry = 0;
                }
                int zHi = yHi - xHi - carry;
                if (zHi < 0) {
                    carry = 1;
                    zHi += 10;
                } else {
                    carry = 0;
                }
                zVal[index] = (byte) (zLo | (zHi << 4));
                index++;
            }
            if (xVal.length < yVal.length) {
                while (index < yVal.length) {
                    byte hiLo = yVal[index];
                    int yHi = (hiLo >>> 4) & 0x0F;
                    int yLo = hiLo & 0x0F;
                    int zLo = yLo - carry;
                    if (zLo < 0) {
                        carry = 1;
                        zLo += 10;
                    } else {
                        carry = 0;
                    }
                    int zHi = yHi - carry;
                    if (zHi < 0) {
                        carry = 1;
                        zHi += 10;
                    } else {
                        carry = 0;
                    }
                    zVal[index] = (byte) (zLo | (zHi << 4));
                    index++;
                }
            }
            zDigits = (index * 2);
            if ((zVal[index - 1] & 0xF0) == 0) {
                zDigits--;
            }
            return new BCD(zVal, zDigits, cmp, BYTE_ARRAY_IS_IMMUTABLE);
        } else if (m_signum < 0) {
            return val.add(this.negate()).negate();
        } else {
            return add(val.negate());
        }
    }
