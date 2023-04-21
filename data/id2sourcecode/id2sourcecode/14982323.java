    public Decimal divide(Decimal divisor) {
        int[] dividend;
        int[] quotient;
        int[] remainder;
        int qd;
        int nsqd;
        int trial = 0;
        int min;
        int max;
        int minadj;
        boolean trialgood;
        int r;
        int rh;
        int rl;
        int md = 0;
        int excp;
        Decimal result = newInstance(ZERO);
        if (nans != FINITE || divisor.nans != FINITE) {
            if (nans == QNAN || nans == SNAN) {
                return this;
            }
            if (divisor.nans == QNAN || divisor.nans == SNAN) {
                return divisor;
            }
            if (nans == INFINITE && divisor.nans == FINITE) {
                result = newInstance(this);
                result.sign = (byte) (sign * divisor.sign);
                return result;
            }
            if (divisor.nans == INFINITE && nans == FINITE) {
                result = newInstance(ZERO);
                result.sign = (byte) (sign * divisor.sign);
                return result;
            }
            if (divisor.nans == INFINITE && nans == INFINITE) {
                ieeeFlags |= FLAG_INVALID;
                result = newInstance(ZERO);
                result.nans = QNAN;
                result = dotrap(FLAG_INVALID, "divide", divisor, result);
                return result;
            }
        }
        if (divisor.mant[DIGITS - 1] == 0) {
            ieeeFlags |= FLAG_DIV_ZERO;
            result = newInstance(ZERO);
            result.sign = (byte) (sign * divisor.sign);
            result.nans = INFINITE;
            result = dotrap(FLAG_DIV_ZERO, "divide", divisor, result);
            return result;
        }
        dividend = new int[DIGITS + 1];
        quotient = new int[DIGITS + 2];
        remainder = new int[DIGITS + 1];
        dividend[DIGITS] = 0;
        quotient[DIGITS] = 0;
        quotient[DIGITS + 1] = 0;
        remainder[DIGITS] = 0;
        for (int i = 0; i < DIGITS; i++) {
            dividend[i] = mant[i];
            quotient[i] = 0;
            remainder[i] = 0;
        }
        nsqd = 0;
        for (qd = DIGITS + 1; qd >= 0; qd--) {
            r = dividend[DIGITS] * radix + dividend[DIGITS - 1];
            min = r / (divisor.mant[DIGITS - 1] + 1);
            max = (r + 1) / divisor.mant[DIGITS - 1];
            trialgood = false;
            while (!trialgood) {
                trial = (min + max) / 2;
                rh = 0;
                for (int i = 0; i < (DIGITS + 1); i++) {
                    int dm = (i < DIGITS) ? divisor.mant[i] : 0;
                    r = (dm * trial) + rh;
                    rh = r / radix;
                    rl = r % radix;
                    remainder[i] = rl;
                }
                rh = 1;
                for (int i = 0; i < (DIGITS + 1); i++) {
                    r = ((radix - 1) - remainder[i]) + dividend[i] + rh;
                    rh = r / radix;
                    rl = r % radix;
                    remainder[i] = rl;
                }
                if (rh == 0) {
                    max = trial - 1;
                    continue;
                }
                minadj = (remainder[DIGITS] * radix) + remainder[DIGITS - 1];
                minadj = minadj / (divisor.mant[DIGITS - 1] + 1);
                if (minadj >= 2) {
                    min = trial + minadj;
                    continue;
                }
                trialgood = false;
                for (int i = (DIGITS - 1); i >= 0; i--) {
                    if (divisor.mant[i] > remainder[i]) {
                        trialgood = true;
                    }
                    if (divisor.mant[i] < remainder[i]) {
                        break;
                    }
                }
                if (remainder[DIGITS] != 0) {
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
            if (rMode == ROUND_DOWN && nsqd == DIGITS) {
                break;
            }
            if (nsqd > DIGITS) {
                break;
            }
            dividend[0] = 0;
            for (int i = 0; i < DIGITS; i++) {
                dividend[i + 1] = remainder[i];
            }
        }
        md = DIGITS;
        for (int i = DIGITS + 1; i >= 0; i--) {
            if (quotient[i] != 0) {
                md = i;
                break;
            }
        }
        for (int i = 0; i < DIGITS; i++) {
            result.mant[DIGITS - i - 1] = quotient[md - i];
        }
        result.exp = (exp - divisor.exp + md - DIGITS + 1 - 1);
        result.sign = (byte) ((sign == divisor.sign) ? 1 : -1);
        if (result.mant[DIGITS - 1] == 0) {
            result.exp = 0;
        }
        if (md > (DIGITS - 1)) {
            excp = result.round(quotient[md - DIGITS]);
        } else {
            excp = result.round(0);
        }
        if (excp != 0) {
            result = dotrap(excp, "divide", divisor, result);
        }
        return result;
    }
