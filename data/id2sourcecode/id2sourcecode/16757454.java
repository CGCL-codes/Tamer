    public boolean isPoisonous(PsiField field, boolean isWrite) {
        final Set<PsiField> poisonousCache = isWrite ? writePoisonous : readPoisonous;
        final Set<PsiField> safeCache = isWrite ? writeSafe : readSafe;
        if (poisonousCache.contains(field)) {
            return true;
        }
        if (safeCache.contains(field)) {
            return false;
        }
        boolean isPoisonous = resolve(field, isWrite);
        if (isPoisonous) {
            poisonousCache.add(field);
        } else {
            safeCache.add(field);
        }
        return isPoisonous;
    }
