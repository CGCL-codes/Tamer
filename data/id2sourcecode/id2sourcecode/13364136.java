    public static int rechercheDichotomique(String[][] indexes, String[] mots, int milieu, int debut, int fin, int niveau) {
        if (fin - debut == 1) {
            if (indexes[milieu][2 + niveau].compareTo(mots[niveau]) < 0) return milieu; else return indiceMotPrecedent(indexes, mots[niveau], milieu, niveau);
        }
        if (indexes[milieu][2 + niveau].compareTo(mots[niveau]) > 0) {
            fin = milieu;
            milieu = (debut + fin) / 2;
            return rechercheDichotomique(indexes, mots, milieu, debut, fin, niveau);
        } else if (indexes[milieu][2 + niveau].compareTo(mots[niveau]) < 0) {
            debut = milieu;
            milieu = (debut + fin) / 2;
            return rechercheDichotomique(indexes, mots, milieu, debut, fin, niveau);
        } else if (niveau + 1 < mots.length) {
            int[] intervalle = getIntervalleMot(indexes, milieu, niveau);
            niveau++;
            return rechercheDichotomique(indexes, mots, (intervalle[0] + intervalle[1]) / 2, intervalle[0], intervalle[1], niveau);
        } else {
            return milieu;
        }
    }
