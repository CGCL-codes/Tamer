    private static int findIsodataUmbral(int umbral, long[] valores, int inicio, int fin) {
        if (inicio == fin) return inicio;
        int promedio1 = getPromedios(inicio, umbral, valores);
        int promedio2 = getPromedios(umbral, fin, valores);
        if (promedio1 < 0 || promedio2 < 0) {
            if (promedio1 < 0) inicio = umbral;
            if (promedio2 < 0) fin = umbral;
            umbral = (inicio + fin) / 2;
            return findIsodataUmbral(umbral, valores, inicio, fin);
        } else {
            int newumbral = (promedio1 + promedio2) / 2;
            if (newumbral == umbral) {
                return newumbral;
            } else {
                return findIsodataUmbral(newumbral, valores, inicio, fin);
            }
        }
    }
