    private void sinft(float z[], int n, int isign) {
        double theta, wi = 0.0, wpi, wpr, wr = 1.0, wtemp;
        float a[], sum, y1, y2;
        int j;
        int n2 = n + 2;
        a = new float[n + 1];
        for (j = 1; j <= n; j++) {
            a[j] = z[j - 1];
        }
        theta = PI / n;
        wtemp = Math.sin(0.5 * theta);
        wpr = -2.0 * wtemp * wtemp;
        wpi = Math.sin(theta);
        a[1] = 0.0f;
        for (j = 2; j <= (n >> 1) + 1; j++) {
            wr = (wtemp = wr) * wpr - wi * wpi + wr;
            wi = wi * wpr + wtemp * wpi + wi;
            y1 = (float) (wi * (a[j] + a[n2 - j]));
            y2 = (float) (0.5 * (a[j] - a[n2 - j]));
            a[j] = y1 + y2;
            a[n2 - j] = y1 - y2;
        }
        realft(a, n, 1);
        a[1] *= 0.5;
        sum = a[2] = 0.0f;
        for (j = 1; j <= n - 1; j += 2) {
            sum += a[j];
            a[j] = a[j + 1];
            a[j + 1] = sum;
        }
        if (isign == 1) {
            for (j = 1; j <= n; j++) {
                z[j - 1] = a[j];
            }
        } else if (isign == -1) {
            for (j = 1; j <= n; j++) {
                z[j - 1] = (float) 2.0 * a[j] / n;
            }
        }
        z[n] = 0.0f;
        a = null;
    }
