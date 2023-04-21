    public int takeStep(int i1, int i2) {
        int y1 = 0, y2 = 0, s = 0;
        float alph1 = 0, alph2 = 0;
        float a1 = 0, a2 = 0;
        float E1 = 0, E2 = 0, L = 0, H = 0, k11 = 0, k22 = 0, k12 = 0, eta = 0, Lobj = 0, Hobj = 0;
        if (i1 == i2) return 0;
        {
            alph1 = object2float(alph.elementAt(i1));
            y1 = object2int(target.elementAt(i1));
            if (alph1 > 0 && alph1 < C) E1 = object2float(error_cache.elementAt(i1)); else E1 = learned_func(i1, learned_func_flag) - y1;
        }
        {
            alph2 = object2float(alph.elementAt(i2));
            y2 = object2int(target.elementAt(i2));
            if (alph2 > 0 && alph2 < C) E2 = object2float(error_cache.elementAt(i2)); else E2 = learned_func(i2, learned_func_flag) - y2;
        }
        s = y1 * y2;
        if (y1 == y2) {
            float gamma = alph1 + alph2;
            if (gamma > C) {
                L = gamma - C;
                H = C;
            } else {
                L = 0;
                H = gamma;
            }
        } else {
            float gamma = alph1 - alph2;
            if (gamma > 0) {
                L = 0;
                H = C - gamma;
            } else {
                L = -gamma;
                H = C;
            }
        }
        if (L == H) {
            return 0;
        }
        k11 = kernel_func(i1, i1, kernel_flag);
        k12 = kernel_func(i1, i2, kernel_flag);
        k22 = kernel_func(i2, i2, kernel_flag);
        eta = 2 * k12 - k11 - k22;
        if (eta < 0) {
            a2 = alph2 + y2 * (E2 - E1) / eta;
            if (a2 < L) a2 = L; else if (a2 > H) a2 = H;
        } else {
            {
                float c1 = eta / 2;
                float c2 = y2 * (E1 - E2) - eta * alph2;
                Lobj = c1 * L * L + c2 * L;
                Hobj = c1 * H * H + c2 * H;
            }
            if (Lobj > Hobj + eps) a2 = L; else if (Lobj < Hobj - eps) a2 = H; else a2 = alph2;
        }
        if (Math.abs(a2 - alph2) < eps * (a2 + alph2 + eps)) return 0;
        a1 = alph1 - s * (a2 - alph2);
        if (a1 < 0) {
            a2 += s * a1;
            a1 = 0;
        } else if (a1 > C) {
            float t = a1 - C;
            a2 += s * t;
            a1 = C;
        }
        {
            float b1 = 0, b2 = 0, bnew = 0;
            if (a1 > 0 && a1 < C) bnew = b + E1 + y1 * (a1 - alph1) * k11 + y2 * (a2 - alph2) * k12; else {
                if (a2 > 0 && a2 < C) bnew = b + E2 + y1 * (a1 - alph1) * k12 + y2 * (a2 - alph2) * k22; else {
                    b1 = b + E1 + y1 * (a1 - alph1) * k11 + y2 * (a2 - alph2) * k12;
                    b2 = b + E2 + y1 * (a1 - alph1) * k12 + y2 * (a2 - alph2) * k22;
                    bnew = (b1 + b2) / 2;
                }
            }
            delta_b = bnew - b;
            b = bnew;
        }
        if (is_linear_kernel) {
            float t1 = y1 * (a1 - alph1);
            float t2 = y2 * (a2 - alph2);
            if (is_sparse_data && is_binary) {
                int p1 = 0, num1 = 0, p2 = 0, num2 = 0;
                num1 = ((sparse_binary_vector) sparse_binary_points.elementAt(i1)).id.size();
                for (p1 = 0; p1 < num1; p1++) {
                    int temp0 = object2int(((sparse_binary_vector) sparse_binary_points.elementAt(i1)).id.elementAt(p1));
                    float temp = object2float(w.elementAt(temp0));
                    w.set(temp0, new Float(temp + t1));
                }
                num2 = ((sparse_binary_vector) sparse_binary_points.elementAt(i2)).id.size();
                for (p2 = 0; p2 < num2; p2++) {
                    int temp0 = object2int(((sparse_binary_vector) sparse_binary_points.elementAt(i2)).id.elementAt(p2));
                    float temp = object2float(w.elementAt(temp0));
                    w.set(temp0, new Float(temp + t2));
                }
            } else if (is_sparse_data && !is_binary) {
                int p1 = 0, num1 = 0, p2 = 0, num2 = 0;
                num1 = ((SparseVector) sparse_points.elementAt(i1)).id.size();
                for (p1 = 0; p1 < num1; p1++) {
                    int temp1 = object2int(((SparseVector) sparse_points.elementAt(i1)).id.elementAt(p1));
                    float temp = object2float(w.elementAt(temp1));
                    float temp2 = object2float(((SparseVector) sparse_points.elementAt(i1)).val.elementAt(p1));
                    w.set(temp1, new Float(temp + t1 * temp2));
                }
                num2 = ((SparseVector) sparse_points.elementAt(i2)).id.size();
                for (p2 = 0; p2 < num2; p2++) {
                    int temp1 = object2int(((SparseVector) sparse_points.elementAt(i2)).id.elementAt(p2));
                    float temp = object2float(w.elementAt(temp1));
                    float temp2 = object2float(((SparseVector) sparse_points.elementAt(i2)).val.elementAt(p2));
                    temp = temp + t2 * temp2;
                    Float value = new Float(temp);
                    w.set(temp1, value);
                }
            } else for (int i = 0; i < d; i++) {
                float temp = dense_points[i1][i] * t1 + dense_points[i2][i] * t2;
                float temp1 = object2float(w.elementAt(i));
                Float value = new Float(temp + temp1);
                w.set(i, value);
            }
        }
        {
            float t1 = y1 * (a1 - alph1);
            float t2 = y2 * (a2 - alph2);
            for (int i = 0; i < end_support_i; i++) if (0 < object2float(alph.elementAt(i)) && object2float(alph.elementAt(i)) < C) {
                float tmp = object2float(error_cache.elementAt(i));
                tmp += t1 * kernel_func(i1, i, kernel_flag) + t2 * kernel_func(i2, i, kernel_flag) - delta_b;
                error_cache.set(i, new Float(tmp));
            }
            error_cache.set(i1, new Float(0));
            error_cache.set(i2, new Float(0));
        }
        alph.set(i1, new Float(a1));
        alph.set(i2, new Float(a2));
        return 1;
    }
