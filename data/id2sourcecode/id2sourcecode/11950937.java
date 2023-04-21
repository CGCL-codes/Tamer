    private int rechercheDichotomique(String[] mots, int milieu, int debut, int fin, int niveau) {
        if (fin - debut == 1) {
            if (indexes[milieu][tailleEnTete + niveau].compareTo(mots[niveau]) < 0) return milieu; else return indiceMotPrecedent(mots[niveau], milieu, niveau);
        }
        if (indexes[milieu][tailleEnTete + niveau].compareTo(mots[niveau]) > 0) {
            fin = milieu;
            milieu = (debut + fin) / 2;
            return rechercheDichotomique(mots, milieu, debut, fin, niveau);
        } else if (indexes[milieu][tailleEnTete + niveau].compareTo(mots[niveau]) < 0) {
            debut = milieu;
            milieu = (debut + fin) / 2;
            return rechercheDichotomique(mots, milieu, debut, fin, niveau);
        } else if (niveau + 1 < mots.length) {
            int[] intervalle = getIntervalleMot(milieu, niveau);
            niveau++;
            return rechercheDichotomique(mots, (intervalle[0] + intervalle[1]) / 2, intervalle[0], intervalle[1], niveau);
        } else {
            return milieu;
        }
    }
