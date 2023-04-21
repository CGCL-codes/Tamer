    public Dfp divide(Dfp divisor) {
        int dividend[];
        int quotient[];
        int remainder[];
        int qd;
        int nsqd;
        int trial = 0;
        int minadj;
        boolean trialgood;
        int md = 0;
        int excp;
        if (field.getRadixDigits() != divisor.field.getRadixDigits()) {
            field.setIEEEFlagsBits(DfpField.FLAG_INVALID);
            final Dfp result = newInstance(getZero());
            result.nans = QNAN;
            return dotrap(DfpField.FLAG_INVALID, DIVIDE_TRAP, divisor, result);
        }
        Dfp result = newInstance(getZero());
        if (nans != FINITE || divisor.nans != FINITE) {
            if (isNaN()) {
                return this;
            }
            if (divisor.isNaN()) {
                return divisor;
            }
            if (nans == INFINITE && divisor.nans == FINITE) {
                result = newInstance(this);
                result.sign = (byte) (sign * divisor.sign);
                return result;
            }
            if (divisor.nans == INFINITE && nans == FINITE) {
                result = newInstance(getZero());
                result.sign = (byte) (sign * divisor.sign);
                return result;
            }
            if (divisor.nans == INFINITE && nans == INFINITE) {
                field.setIEEEFlagsBits(DfpField.FLAG_INVALID);
                result = newInstance(getZero());
                result.nans = QNAN;
                result = dotrap(DfpField.FLAG_INVALID, DIVIDE_TRAP, divisor, result);
                return result;
            }
        }
        if (divisor.mant[mant.length - 1] == 0) {
            field.setIEEEFlagsBits(DfpField.FLAG_DIV_ZERO);
            result = newInstance(getZero());
            result.sign = (byte) (sign * divisor.sign);
            result.nans = INFINITE;
            result = dotrap(DfpField.FLAG_DIV_ZERO, DIVIDE_TRAP, divisor, result);
            return result;
        }
        dividend = new int[mant.length + 1];
        quotient = new int[mant.length + 2];
        remainder = new int[mant.length + 1];
        dividend[mant.length] = 0;
        quotient[mant.length] = 0;
        quotient[mant.length + 1] = 0;
        remainder[mant.length] = 0;
        for (int i = 0; i < mant.length; i++) {
            dividend[i] = mant[i];
            quotient[i] = 0;
            remainder[i] = 0;
        }
        nsqd = 0;
        for (qd = mant.length + 1; qd >= 0; qd--) {
            final int divMsb = dividend[mant.length] * RADIX + dividend[mant.length - 1];
            int min = divMsb / (divisor.mant[mant.length - 1] + 1);
            int max = (divMsb + 1) / divisor.mant[mant.length - 1];
            trialgood = false;
            while (!trialgood) {
                trial = (min + max) / 2;
                int rh = 0;
                for (int i = 0; i < mant.length + 1; i++) {
                    int dm = (i < mant.length) ? divisor.mant[i] : 0;
                    final int r = (dm * trial) + rh;
                    rh = r / RADIX;
                    remainder[i] = r - rh * RADIX;
                }
                rh = 1;
                for (int i = 0; i < mant.length + 1; i++) {
                    final int r = ((RADIX - 1) - remainder[i]) + dividend[i] + rh;
                    rh = r / RADIX;
                    remainder[i] = r - rh * RADIX;
                }
                if (rh == 0) {
                    max = trial - 1;
                    continue;
                }
                minadj = (remainder[mant.length] * RADIX) + remainder[mant.length - 1];
                minadj = minadj / (divisor.mant[mant.length - 1] + 1);
                if (minadj >= 2) {
                    min = trial + minadj;
                    continue;
                }
                trialgood = false;
                for (int i = mant.length - 1; i >= 0; i--) {
                    if (divisor.mant[i] > remainder[i]) {
                        trialgood = true;
                    }
                    if (divisor.mant[i] < remainder[i]) {
                        break;
                    }
                }
                if (remainder[mant.length] != 0) {
                    trialgood = false;
                }
                if (trialgood == false) {
                    min = trial + 1;
                }
            }
            quotient[qd] = trial;
            if (trial != 0 || nsqd != 0) {
                nsqd++;
            }
            if (field.getRoundingMode() == DfpField.RoundingMode.ROUND_DOWN && nsqd == mant.length) {
                break;
            }
            if (nsqd > mant.length) {
                break;
            }
            dividend[0] = 0;
            for (int i = 0; i < mant.length; i++) {
                dividend[i + 1] = remainder[i];
            }
        }
        md = mant.length;
        for (int i = mant.length + 1; i >= 0; i--) {
            if (quotient[i] != 0) {
                md = i;
                break;
            }
        }
        for (int i = 0; i < mant.length; i++) {
            result.mant[mant.length - i - 1] = quotient[md - i];
        }
        result.exp = exp - divisor.exp + md - mant.length;
        result.sign = (byte) ((sign == divisor.sign) ? 1 : -1);
        if (result.mant[mant.length - 1] == 0) {
            result.exp = 0;
        }
        if (md > (mant.length - 1)) {
            excp = result.round(quotient[md - mant.length]);
        } else {
            excp = result.round(0);
        }
        if (excp != 0) {
            result = dotrap(excp, DIVIDE_TRAP, divisor, result);
        }
        return result;
    }
