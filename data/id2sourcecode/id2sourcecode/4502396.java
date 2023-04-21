    @Override
    public String toString() {
        String str = string;
        if (str == null) {
            if (this == ZERO) {
                str = "PT0S";
            } else {
                StringBuilder buf = new StringBuilder();
                buf.append('P');
                if (years != 0) {
                    buf.append(years).append('Y');
                }
                if (months != 0) {
                    buf.append(months).append('M');
                }
                if (days != 0) {
                    buf.append(days).append('D');
                }
                if ((hours | minutes | seconds) != 0 || nanos != 0) {
                    buf.append('T');
                    if (hours != 0) {
                        buf.append(hours).append('H');
                    }
                    if (minutes != 0) {
                        buf.append(minutes).append('M');
                    }
                    if (seconds != 0 || nanos != 0) {
                        if (nanos == 0) {
                            buf.append(seconds).append('S');
                        } else {
                            long s = seconds + (nanos / 1000000000);
                            long n = nanos % 1000000000;
                            if (s < 0 && n > 0) {
                                n -= 1000000000;
                                s++;
                            } else if (s > 0 && n < 0) {
                                n += 1000000000;
                                s--;
                            }
                            if (n < 0) {
                                n = -n;
                                if (s == 0) {
                                    buf.append('-');
                                }
                            }
                            buf.append(s);
                            int dotPos = buf.length();
                            n += 1000000000;
                            while (n % 10 == 0) {
                                n /= 10;
                            }
                            buf.append(n);
                            buf.setCharAt(dotPos, '.');
                            buf.append('S');
                        }
                    }
                }
                str = buf.toString();
            }
            string = str;
        }
        return str;
    }
