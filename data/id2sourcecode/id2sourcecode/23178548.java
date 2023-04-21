    @Override
    protected final void calc_lambda_eq() {
        double lambda_eq_sum = 0;
        int count = 0;
        int i;
        for (i = 0; i < n; i++) {
            if ((x[i] > l[i]) && (x[i] < u[i])) {
                if (A[i] > 0) {
                    lambda_eq_sum -= (sum[i] + c[i]);
                } else {
                    lambda_eq_sum += sum[i] + c[i];
                }
                ;
                count++;
            }
            ;
        }
        ;
        if (count > 0) {
            lambda_eq_sum /= count;
        } else {
            double lambda_min = Double.NEGATIVE_INFINITY;
            double lambda_max = Double.POSITIVE_INFINITY;
            double nabla;
            for (i = 0; i < n; i++) {
                nabla = sum[i] + c[i];
                if (x[i] <= l[i]) {
                    if (A[i] > 0) {
                        if (-nabla > lambda_min) {
                            lambda_min = -nabla;
                        }
                        ;
                    } else {
                        if (nabla < lambda_max) {
                            lambda_max = nabla;
                        }
                        ;
                    }
                    ;
                } else {
                    if (A[i] > 0) {
                        if (-nabla < lambda_max) {
                            lambda_max = -nabla;
                        }
                        ;
                    } else {
                        if (nabla > lambda_min) {
                            lambda_min = nabla;
                        }
                        ;
                    }
                    ;
                }
                ;
            }
            ;
            if (lambda_min > Double.NEGATIVE_INFINITY) {
                if (lambda_max < Double.POSITIVE_INFINITY) {
                    lambda_eq_sum = (lambda_max + lambda_min) / 2;
                } else {
                    lambda_eq_sum = lambda_min;
                }
                ;
            } else {
                lambda_eq_sum = lambda_max;
            }
            ;
        }
        ;
        lambda_eq = lambda_eq_sum;
    }
