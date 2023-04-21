    private static int maxSumRecursive(int[] a, int left, int right) {
        int somaEsquerdaMax = 0, somaDireitaMax = 0;
        int somaEsquerda = 0, somaDireita = 0;
        int center = (left + right) / 2;
        if (left == right) return a[left] > 0 ? a[left] : 0;
        int maxLeftSum = maxSumRecursive(a, left, center);
        int maxRightSum = maxSumRecursive(a, center + 1, right);
        for (int i = center; i >= left; i--) {
            somaEsquerda += a[i];
            if (somaEsquerda > somaEsquerdaMax) somaEsquerdaMax = somaEsquerda;
        }
        for (int i = center + 1; i <= right; i++) {
            somaDireita += a[i];
            if (somaDireita > somaDireitaMax) somaDireitaMax = somaDireita;
        }
        return max3(maxLeftSum, maxRightSum, somaEsquerdaMax + somaDireitaMax);
    }
