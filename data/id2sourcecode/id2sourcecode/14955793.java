    protected TransitionOpns removeOpn(Tape t, HeadMove hm, Symbol read, Symbol write) {
        for (TransitionOpns it : opns) {
            if (it.getTape() == t && it.getHm().equals(hm) && (read != null && it.getReadSymb() == read || it.getReadSymb().isBlank() && read.isBlank() || it.getReadSymb().isEpsilon() && read.isEpsilon()) && (write != null && it.getWriteSymb() == write || it.getWriteSymb().isBlank() && write.isBlank() || it.getWriteSymb().isEpsilon() && write.isEpsilon())) {
                opns.remove(it);
                return it;
            }
        }
        return null;
    }
