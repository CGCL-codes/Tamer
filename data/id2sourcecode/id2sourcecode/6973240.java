    static FindEnsemblePartIndexResult FindEnsemblePartIndex(Ensemble ensData, String partName) {
        int pos = 0;
        int first, last;
        int cmp;
        int posRes;
        first = 0;
        last = ensData.numParts - 1;
        while (last >= first) {
            pos = (first + last) / 2;
            if (partName.charAt(0) == ensData.parts[pos].name.charAt(0)) {
                cmp = partName.compareTo(ensData.parts[pos].name);
                if (cmp == 0) {
                    break;
                }
            } else if (partName.charAt(0) < ensData.parts[pos].name.charAt(0)) {
                cmp = -1;
            } else {
                cmp = 1;
            }
            if (cmp > 0) {
                first = pos + 1;
            } else {
                last = pos - 1;
            }
        }
        FindEnsemblePartIndexResult res = new FindEnsemblePartIndexResult();
        if (last >= first) {
            res.status = true;
            res.pos = pos;
            return res;
        }
        res.status = false;
        res.pos = first;
        return res;
    }
