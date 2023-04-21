    public BCD shiftLeft(int digits) {
        if (digits < 0) throw new IllegalArgumentException("BCD Invalid digits parameter"); else if (digits == 0) return this;
        if (m_signum == 0) return ZERO;
        int newDigits = m_digits + digits;
        if ((digits % 2) == 0) {
            int nBytesToAdd = digits / 2;
            int len = m_magnitude.length + nBytesToAdd;
            byte[] b = new byte[len];
            System.arraycopy(m_magnitude, 0, b, nBytesToAdd, m_magnitude.length);
            return new BCD(b, newDigits, m_signum, BYTE_ARRAY_IS_IMMUTABLE);
        } else {
            int newLen = (newDigits + 1) / 2;
            byte[] newMagnitude = new byte[newLen];
            int nBytesToShift = digits / 2;
            System.arraycopy(m_magnitude, 0, newMagnitude, nBytesToShift, m_magnitude.length);
            byte prevHi = 0;
            int index = 0;
            while (index < newLen - 1) {
                byte thisHi = newMagnitude[index];
                newMagnitude[index] = (byte) ((newMagnitude[index] << 4) | ((prevHi >>> 4) & 0x0F));
                prevHi = thisHi;
                index++;
            }
            newMagnitude[index] <<= 4;
            newMagnitude[index] |= (byte) ((prevHi >>> 4) & 0x0F);
            return new BCD(newMagnitude, newDigits, m_signum, BYTE_ARRAY_IS_IMMUTABLE);
        }
    }
