    public boolean remPart(int i) {
        try {
            for (int j = i; j < parts; j++) {
                part[j] = part[j + 1];
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
