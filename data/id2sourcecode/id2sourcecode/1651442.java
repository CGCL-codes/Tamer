    public void resortFavoredValues() {
        Coding tc = (Coding) tokenCoding;
        fValues = BandStructure.realloc(fValues, 1 + fVlen);
        int fillp = 1;
        for (int n = 1; n <= tc.B(); n++) {
            int nmax = tc.byteMax(n);
            if (nmax > fVlen) nmax = fVlen;
            if (nmax < tc.byteMin(n)) break;
            int low = fillp;
            int high = nmax + 1;
            if (high == low) continue;
            assert (high > low) : high + "!>" + low;
            assert (tc.getLength(low) == n) : n + " != len(" + (low) + ") == " + tc.getLength(low);
            assert (tc.getLength(high - 1) == n) : n + " != len(" + (high - 1) + ") == " + tc.getLength(high - 1);
            int midTarget = low + (high - low) / 2;
            int mid = low;
            int prevCount = -1;
            int prevLimit = low;
            for (int i = low; i < high; i++) {
                int val = fValues[i];
                int count = vHist.getFrequency(val);
                if (prevCount != count) {
                    if (n == 1) {
                        Arrays.sort(fValues, prevLimit, i);
                    } else if (Math.abs(mid - midTarget) > Math.abs(i - midTarget)) {
                        mid = i;
                    }
                    prevCount = count;
                    prevLimit = i;
                }
            }
            if (n == 1) {
                Arrays.sort(fValues, prevLimit, high);
            } else {
                Arrays.sort(fValues, low, mid);
                Arrays.sort(fValues, mid, high);
            }
            assert (tc.getLength(low) == tc.getLength(mid));
            assert (tc.getLength(low) == tc.getLength(high - 1));
            fillp = nmax + 1;
        }
        assert (fillp == fValues.length);
        symtab = null;
    }
