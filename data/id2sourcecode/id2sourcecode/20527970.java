    private static final void convolveLine(final float[] input, final float[] pixels, final float[][] kernel, final int readFrom, final int readTo, final int writeFrom, final int writeTo, final int point0, final int pointInc) {
        final int length = input.length;
        final float first = input[0];
        final float last = input[length - 1];
        final float[] kern = kernel[0];
        final float kern0 = kern[0];
        final float[] kernSum = kernel[1];
        final int kRadius = kern.length;
        final int firstPart = kRadius < length ? kRadius : length;
        int p = point0 + writeFrom * pointInc;
        int i = writeFrom;
        for (; i < firstPart; i++, p += pointInc) {
            float result = input[i] * kern0;
            result += kernSum[i] * first;
            if (i + kRadius > length) result += kernSum[length - i - 1] * last;
            for (int k = 1; k < kRadius; k++) {
                float v = 0;
                if (i - k >= 0) v += input[i - k];
                if (i + k < length) v += input[i + k];
                result += kern[k] * v;
            }
            pixels[p] = result;
        }
        final int iEndInside = length - kRadius < writeTo ? length - kRadius : writeTo;
        for (; i < iEndInside; i++, p += pointInc) {
            float result = input[i] * kern0;
            for (int k = 1; k < kRadius; k++) result += kern[k] * (input[i - k] + input[i + k]);
            pixels[p] = result;
        }
        for (; i < writeTo; i++, p += pointInc) {
            float result = input[i] * kern0;
            if (i < kRadius) result += kernSum[i] * first;
            if (i + kRadius >= length) result += kernSum[length - i - 1] * last;
            for (int k = 1; k < kRadius; k++) {
                float v = 0;
                if (i - k >= 0) v += input[i - k];
                if (i + k < length) v += input[i + k];
                result += kern[k] * v;
            }
            pixels[p] = result;
        }
    }
