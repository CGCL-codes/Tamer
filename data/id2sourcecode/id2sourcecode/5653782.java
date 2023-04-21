    public static final double[] Preprocess_Series(double[] v, boolean seriesFixedLength, boolean Nor, boolean Tip, boolean Dif) {
        int longitud = v.length;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double total = 0;
        for (int pos = 0; pos < longitud; pos++) {
            if (v[pos] < min) min = v[pos];
            if (v[pos] > max) max = v[pos];
            total += v[pos];
        }
        double amplitud = max - min;
        double media = total / longitud;
        double suma = 0;
        if (Nor) {
            for (int i = 0; i < longitud; i++) v[i] = (v[i] - min) / amplitud;
        } else if (Tip) {
            for (int i = 0; i < longitud; i++) suma += Math.pow(v[i] - media, 2);
            if (suma > 0) {
                double desv = Math.sqrt(suma / (longitud - 1));
                for (int i = 0; i < longitud; i++) v[i] = (v[i] - media) / desv;
            }
        }
        if (Dif) longitud--;
        double[] aux = new double[longitud];
        if (Dif) for (int i = 0; i < longitud; i++) aux[i] = v[i + 1] - v[i]; else for (int i = 0; i < longitud; i++) aux[i] = v[i];
        return aux;
    }
