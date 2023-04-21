    void delete(int i) {
        s.assertt(optioni > 0);
        optioni--;
        for (int j = i; j < optioni; j++) option[j] = option[j + 1];
    }
