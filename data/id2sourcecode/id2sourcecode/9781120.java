    public void removeReference(Reference ref) {
        if (!(reads.remove(ref) || writes.remove(ref))) {
            throw new IllegalArgumentException("unknown access");
        }
    }
