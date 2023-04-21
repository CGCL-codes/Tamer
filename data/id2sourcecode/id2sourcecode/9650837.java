    public void remove(Ko ko) {
        for (int i = 0; i < db; i++) if (kovek[i] == ko) {
            db--;
            for (int j = i; j < db; j++) kovek[j] = kovek[j + 1];
            break;
        }
    }
