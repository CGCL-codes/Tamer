    static double torben(double m[]) {
        int i;
        int less;
        int greater;
        int equal;
        double min = m[0];
        double max = m[0];
        double guess;
        double maxltguess;
        double mingtguess;
        for (i = 1; i < m.length; i++) {
            if (m[i] < min) {
                min = m[i];
            }
            if (m[i] > max) {
                max = m[i];
            }
        }
        while (true) {
            guess = (min + max) / 2;
            less = 0;
            greater = 0;
            equal = 0;
            maxltguess = min;
            mingtguess = max;
            for (i = 0; i < m.length; i++) {
                if (m[i] < guess) {
                    less++;
                    if (m[i] > maxltguess) {
                        maxltguess = m[i];
                    }
                } else if (m[i] > guess) {
                    greater++;
                    if (m[i] < mingtguess) {
                        mingtguess = m[i];
                    }
                } else {
                    equal++;
                }
            }
            if (less <= (m.length + 1) / 2 && greater <= (m.length + 1) / 2) {
                break;
            } else if (less > greater) {
                max = maxltguess;
            } else {
                min = mingtguess;
            }
        }
        if (less >= (m.length + 1) / 2) {
            return maxltguess;
        } else if (less + equal >= (m.length + 1) / 2) {
            return guess;
        } else {
            return mingtguess;
        }
    }
