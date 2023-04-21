    private void sort(int jc) {
        TaskResult taux;
        int length = taur.length;
        for (int i = jc; i < length; i++) {
            if (taur[i] == null) {
                int i2;
                for (i2 = i; i2 < length - 1; i2++) {
                    taur[i2] = taur[i2 + 1];
                    taur[i2 + 1] = null;
                }
                length--;
            } else {
                for (int j = i + 1; j < length; j++) {
                    if (taur[j] != null) {
                        if (taur[i].getT1() > taur[j].getT1()) {
                            taux = taur[i];
                            taur[i] = taur[j];
                            taur[j] = taux;
                        } else {
                            if (taur[i].getT1() == taur[j].getT1()) {
                                if (taur[i].p < taur[j].p) {
                                    taux = taur[i];
                                    taur[i] = taur[j];
                                    taur[j] = taux;
                                } else {
                                    if (taur[i].p == taur[j].p) {
                                        if (taur[i].getInst() > taur[j].getInst()) {
                                            taux = taur[i];
                                            taur[i] = taur[j];
                                            taur[j] = taux;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
