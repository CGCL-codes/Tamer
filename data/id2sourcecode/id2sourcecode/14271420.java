    private static void mergeSortNiveau(Sort[] a, Sort[] tmpArray, int left, int right, String nomClasse) {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSortNiveau(a, tmpArray, left, center, nomClasse);
            mergeSortNiveau(a, tmpArray, center + 1, right, nomClasse);
            mergeSortNiveauClasse(a, tmpArray, left, center + 1, right, nomClasse);
        }
    }
