    private static double[] Line_Minimize(double shigh, double slow, double[] y, double[] direction, int probe_count, int clone_count, double[][] aa, int[] probeOrder, int clone_length, double const1, double[] R) {
        double scalar, old_func, new_func;
        double grad_left, grad_right, grad_middle;
        double CRITERIA_BISECTION = 0.0000000001;
        double[] y_temp = new double[probe_count + 1];
        for (int probeIndex = 0; probeIndex < probe_count + 1; probeIndex++) {
            y_temp[probeIndex] = y[probeIndex] + slow * direction[probeIndex];
        }
        grad_left = Gradient_S(y_temp, direction, probe_count, clone_count, aa, probeOrder, clone_length, R);
        old_func = Func(const1, R, aa, y_temp, clone_length, probe_count, clone_count, probeOrder);
        for (int probeIndex = 0; probeIndex < probe_count + 1; probeIndex++) {
            y_temp[probeIndex] = y[probeIndex] + shigh * direction[probeIndex];
        }
        grad_right = Gradient_S(y_temp, direction, probe_count, clone_count, aa, probeOrder, clone_length, R);
        while (1 < 2) {
            if (grad_left <= 0 && grad_right <= 0) {
                for (int probeIndex = 0; probeIndex < probe_count + 1; probeIndex++) {
                    y[probeIndex] = y[probeIndex] + shigh * direction[probeIndex];
                }
                break;
            }
            if (grad_left >= 0 && grad_right >= 0) {
                for (int probeIndex = 0; probeIndex < probe_count + 1; probeIndex++) {
                    y[probeIndex] = y[probeIndex] + slow * direction[probeIndex];
                }
                break;
            }
            if ((grad_left <= 0 && grad_right >= 0) || (grad_left >= 0 && grad_right <= 0)) {
                scalar = (slow + shigh) / 2;
                for (int probeIndex = 0; probeIndex < probe_count + 1; probeIndex++) {
                    y_temp[probeIndex] = y[probeIndex] + scalar * direction[probeIndex];
                }
                grad_middle = Gradient_S(y_temp, direction, probe_count, clone_count, aa, probeOrder, clone_length, R);
                new_func = Func(const1, R, aa, y_temp, clone_length, probe_count, clone_count, probeOrder);
                if ((new_func <= old_func) && (old_func - new_func) <= CRITERIA_BISECTION) {
                    for (int probeIndex = 0; probeIndex < probe_count + 1; probeIndex++) {
                        y[probeIndex] = y[probeIndex] + scalar * direction[probeIndex];
                    }
                    break;
                }
                if (grad_middle > 0) {
                    old_func = new_func;
                    shigh = scalar;
                    grad_right = grad_middle;
                } else {
                    old_func = new_func;
                    slow = scalar;
                    grad_left = grad_middle;
                }
                new_func = old_func;
            }
        }
        return y;
    }
