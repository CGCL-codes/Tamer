    public String toString() {
        return "attribute: " + name + (isGetter() ? " read" : "") + (isSetter() ? " write" : "") + " type: " + type.getSimpleName();
    }
