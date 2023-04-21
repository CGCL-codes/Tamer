    public void a52_imdct_256(double[] data, int dataPointer, int delayPointer, double bias) {
        for (int k = 0; k < 64; k++) {
            int p = 2 * (128 - 2 * k - 1);
            int q = 2 * (2 * k);
            buf_1_re[k] = data[p] * xcos2[k] - data[q] * xsin2[k];
            buf_1_im[k] = -1.0f * (data[q] * xcos2[k] + data[p] * xsin2[k]);
            buf_2_re[k] = data[p + 1] * xcos2[k] - data[q + 1] * xsin2[k];
            buf_2_im[k] = -1.0f * (data[q + 1] * xcos2[k] + data[p + 1] * xsin2[k]);
        }
        for (int i = 0; i < 64; i++) {
            int k = bit_reverse_256[i];
            if (k < i) {
                swap_cmplx(buf_1_re, buf_1_im, i, k);
                swap_cmplx(buf_2_re, buf_2_im, i, k);
            }
        }
        int two_m;
        int two_m_plus_one;
        double tmp_a_r, tmp_a_i;
        double tmp_b_r, tmp_b_i;
        for (int m = 0; m < 6; m++) {
            two_m = (1 << m);
            two_m_plus_one = (1 << (m + 1));
            if (m != 0) two_m = (1 << m); else two_m = 1;
            for (int k = 0; k < two_m; k++) {
                for (int i = 0; i < 64; i += two_m_plus_one) {
                    int p = k + i;
                    int q = p + two_m;
                    tmp_a_r = buf_1_re[p];
                    tmp_a_i = buf_1_im[p];
                    tmp_b_r = buf_1_re[q] * w_re[m][k] - buf_1_im[q] * w_im[m][k];
                    tmp_b_i = buf_1_im[q] * w_re[m][k] + buf_1_re[q] * w_im[m][k];
                    buf_1_re[p] = tmp_a_r + tmp_b_r;
                    buf_1_im[p] = tmp_a_i + tmp_b_i;
                    buf_1_re[q] = tmp_a_r - tmp_b_r;
                    buf_1_im[q] = tmp_a_i - tmp_b_i;
                    tmp_a_r = buf_2_re[p];
                    tmp_a_i = buf_2_im[p];
                    tmp_b_r = buf_2_re[q] * w_re[m][k] - buf_2_im[q] * w_im[m][k];
                    tmp_b_i = buf_2_im[q] * w_re[m][k] + buf_2_re[q] * w_im[m][k];
                    buf_2_re[p] = tmp_a_r + tmp_b_r;
                    buf_2_im[p] = tmp_a_i + tmp_b_i;
                    buf_2_re[q] = tmp_a_r - tmp_b_r;
                    buf_2_im[q] = tmp_a_i - tmp_b_i;
                }
            }
        }
        for (int i = 0; i < 64; i++) {
            tmp_a_r = buf_1_re[i];
            tmp_a_i = -buf_1_im[i];
            buf_1_re[i] = (tmp_a_r * xcos2[i]) - (tmp_a_i * xsin2[i]);
            buf_1_im[i] = (tmp_a_r * xsin2[i]) + (tmp_a_i * xcos2[i]);
            tmp_a_r = buf_2_re[i];
            tmp_a_i = -buf_2_im[i];
            buf_2_re[i] = (tmp_a_r * xcos2[i]) - (tmp_a_i * xsin2[i]);
            buf_2_im[i] = (tmp_a_r * xsin2[i]) + (tmp_a_i * xcos2[i]);
        }
        int dataPtr = dataPointer;
        int delayPtr = delayPointer;
        int windowPointer = 0;
        if (debug) System.out.println("BIAS " + bias);
        for (int i = 0; i < 64; i++) {
            data[dataPtr++] = -buf_1_im[i] * window[windowPointer++] + data[delayPtr++] + bias;
            data[dataPtr++] = buf_1_re[64 - i - 1] * window[windowPointer++] + data[delayPtr++] + bias;
        }
        for (int i = 0; i < 64; i++) {
            data[dataPtr++] = -buf_1_re[i] * window[windowPointer++] + data[delayPtr++] + bias;
            data[dataPtr++] = buf_1_im[64 - i - 1] * window[windowPointer++] + data[delayPtr++] + bias;
        }
        delayPtr = delayPointer;
        for (int i = 0; i < 64; i++) {
            data[delayPtr++] = -buf_2_re[i] * window[--windowPointer];
            data[delayPtr++] = buf_2_im[64 - i - 1] * window[--windowPointer];
        }
        for (int i = 0; i < 64; i++) {
            data[delayPtr++] = buf_2_im[i] * window[--windowPointer];
            data[delayPtr++] = -buf_2_re[64 - i - 1] * window[--windowPointer];
        }
    }
