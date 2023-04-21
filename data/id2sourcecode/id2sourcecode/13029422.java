    public static BigInteger[] mulCiphertexts(BigInteger[] a, BigInteger[] b, FHEParams fheparams, BigInteger det, BigInteger root, BigInteger[] pkBlocksX, BigInteger[] ctxts, GHKeyPair key, IProgressMonitor monitor, int work) {
        int max = Math.max(a.length, b.length);
        BigInteger[] aTemp = new BigInteger[a.length];
        BigInteger[] bTemp = new BigInteger[b.length];
        for (int i = 0; i < a.length; i++) aTemp[i] = a[i];
        for (int i = 0; i < b.length; i++) bTemp[i] = b[i];
        BigInteger[] temp = new BigInteger[max];
        BigInteger[] temp2 = new BigInteger[max];
        for (int i = 0; i < max; i++) {
            temp[i] = GHEncrypt.encrypt(fheparams, key, 0);
        }
        for (int i = 0; i < aTemp.length; i++) {
            for (int j = 0; j < bTemp.length; j++) {
                temp2[j] = GHReCrypt.recrypt(fheparams, bTemp[j].multiply(aTemp[0]).mod(det), det, root, pkBlocksX, ctxts);
            }
            SubProgressMonitor sm = new SubProgressMonitor(monitor, work / aTemp.length);
            sm.beginTask("", work / aTemp.length);
            temp = addCiphertexts(temp, temp2, fheparams, det, root, pkBlocksX, ctxts, sm, work / aTemp.length);
            if (sm.isCanceled()) return null;
            sm.done();
            for (int k = 0; k < aTemp.length - 1; k++) {
                aTemp[k] = aTemp[k + 1];
            }
            aTemp[aTemp.length - 1] = temp[0];
            for (int k = 0; k < temp.length - 1; k++) {
                temp[k] = temp[k + 1];
            }
            if (monitor.isCanceled()) {
                return null;
            }
        }
        return aTemp;
    }
