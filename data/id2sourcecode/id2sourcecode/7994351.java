    public BCD add(BCD val) throws ArithmeticException {
        if (val.m_signum == 0) {
            return this;
        } else if (m_signum == 0) {
            return val;
        } else if (((m_digits > val.m_digits) ? m_digits : val.m_digits) < DIGITS_LONG_MIN - 1) {
            long x = longValue();
            long y = val.longValue();
            return new BCD(x + y);
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
            int zDigits = ((xDigits > yDigits) ? xDigits : yDigits) + 1;
            byte[] zVal = new byte[((zDigits + 1) / 2) + 1];
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
                int zLo = xLo + yLo + carry;
                if (zLo > 9) {
                    carry = zLo / 10;
                    zLo %= 10;
                } else {
                    carry = 0;
                }
                int zHi = xHi + yHi + carry;
                if (zHi > 9) {
                    carry = zHi / 10;
                    zHi %= 10;
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
                    int zLo = yLo + carry;
                    if (zLo > 9) {
                        carry = zLo / 10;
                        zLo %= 10;
                    } else {
                        carry = 0;
                    }
                    int zHi = yHi + carry;
                    if (zHi > 9) {
                        carry = zHi / 10;
                        zHi %= 10;
                    } else {
                        carry = 0;
                    }
                    zVal[index] = (byte) (zLo | (zHi << 4));
                    index++;
                }
            }
            if (carry != 0) {
                zVal[index] = 0x01;
                zDigits = (index * 2) + 1;
            } else {
                if ((zVal[index - 1] & 0xF0) == 0) {
                    zDigits = (index * 2) - 1;
                } else {
                    zDigits = index * 2;
                }
            }
            return new BCD(zVal, zDigits, m_signum, BYTE_ARRAY_IS_IMMUTABLE);
        } else if (m_signum < 0) {
            return val.subtract(this.negate());
        } else {
            return subtract(val.negate());
        }
    }
