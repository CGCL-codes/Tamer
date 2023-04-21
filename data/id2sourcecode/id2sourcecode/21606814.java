    public static double[] design(FilterShape type, float dbGain, float freq, float srate, float resonance) {
        double A, omega, sn, cs, alpha, beta;
        double a0, a1, a2, b0, b1, b2;
        if (decramped) {
            A = Math.pow(10, dbGain / 20);
            float bwHz = FilterTools.getHzBandwidth(freq, resonance);
            switch(type) {
                case PEQ:
                    if (Math.abs(dbGain) > 0.1) {
                        return OrfanidisBiQuadDesigner.designPeak(srate, freq, bwHz, (float) A);
                    }
                    break;
                case NOTCH:
                    return OrfanidisBiQuadDesigner.designNotch(srate, freq, bwHz);
                case RESONATOR:
                    return OrfanidisBiQuadDesigner.designResonator(srate, freq, bwHz);
            }
        }
        A = Math.pow(10, dbGain / 40);
        float bandwidth = FilterTools.getOctaveBandwidth(resonance);
        omega = 2 * Math.PI * freq / srate;
        sn = Math.sin(omega);
        cs = Math.cos(omega);
        alpha = sn * Math.sinh(M_LN2 / 2 * bandwidth * omega / sn);
        beta = Math.sqrt(A + A);
        switch(type) {
            case LPF:
                b0 = (1 - cs) / 2;
                b1 = 1 - cs;
                b2 = (1 - cs) / 2;
                a0 = 1 + alpha;
                a1 = -2 * cs;
                a2 = 1 - alpha;
                break;
            case HPF:
                b0 = (1 + cs) / 2;
                b1 = -(1 + cs);
                b2 = (1 + cs) / 2;
                a0 = 1 + alpha;
                a1 = -2 * cs;
                a2 = 1 - alpha;
                break;
            case BPF:
                b0 = alpha;
                b1 = 0;
                b2 = -alpha;
                a0 = 1 + alpha;
                a1 = -2 * cs;
                a2 = 1 - alpha;
                break;
            case NOTCH:
                b0 = 1;
                b1 = -2 * cs;
                b2 = 1;
                a0 = 1 + alpha;
                a1 = -2 * cs;
                a2 = 1 - alpha;
                break;
            case PEQ:
                b0 = 1 + (alpha * A);
                b1 = -2 * cs;
                b2 = 1 - (alpha * A);
                a0 = 1 + (alpha / A);
                a1 = -2 * cs;
                a2 = 1 - (alpha / A);
                break;
            case LSH:
                b0 = A * ((A + 1) - (A - 1) * cs + beta * sn);
                b1 = 2 * A * ((A - 1) - (A + 1) * cs);
                b2 = A * ((A + 1) - (A - 1) * cs - beta * sn);
                a0 = (A + 1) + (A - 1) * cs + beta * sn;
                a1 = -2 * ((A - 1) + (A + 1) * cs);
                a2 = (A + 1) + (A - 1) * cs - beta * sn;
                break;
            case HSH:
                b0 = A * ((A + 1) + (A - 1) * cs + beta * sn);
                b1 = -2 * A * ((A - 1) + (A + 1) * cs);
                b2 = A * ((A + 1) + (A - 1) * cs - beta * sn);
                a0 = (A + 1) - (A - 1) * cs + beta * sn;
                a1 = 2 * ((A - 1) - (A + 1) * cs);
                a2 = (A + 1) - (A - 1) * cs - beta * sn;
                break;
            default:
                return null;
        }
        double[] a = new double[5];
        a[0] = b0 / a0;
        a[1] = b1 / a0;
        a[2] = b2 / a0;
        a[3] = a1 / a0;
        a[4] = a2 / a0;
        return a;
    }
