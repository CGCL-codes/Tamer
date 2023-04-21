    public void addGenre(String genre) {
        monGenre.add(genre);
        if (tousLesGenres.size() == 0) {
            tousLesGenres.add(genre);
        } else {
            int d = 0;
            int f = tousLesGenres.size();
            while (d <= f) {
                int m = (d + f) / 2;
                if (((String) tousLesGenres.get(m)).compareTo(genre) < 0) {
                    d = m + 1;
                }
                if (((String) tousLesGenres.get(m)).compareTo(genre) > 0) {
                    f = m - 1;
                } else {
                    return;
                }
            }
            if (d >= tousLesGenres.size()) {
                tousLesGenres.add(genre);
            } else {
                tousLesGenres.add(d, genre);
            }
        }
    }
