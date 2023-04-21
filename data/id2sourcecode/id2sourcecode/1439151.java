    void insertOperator(PrismsConfig op) {
        int pri = op.getInt("order", 0);
        int min = 0, max = theOperators.size();
        while (max > min) {
            int mid = (min + max) / 2;
            if (mid == theOperators.size()) {
                min = mid;
                break;
            }
            int testPri = theOperators.get(mid).getInt("order", 0);
            if (pri > testPri) min = mid + 1; else max = mid;
        }
        theOperators.add(min, op);
    }
