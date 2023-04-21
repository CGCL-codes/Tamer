    public boolean isPrefixOf(ContentObject other, int count) {
        boolean match = isPrefixOf(other.name(), count);
        if (match || count() != count) return match;
        if (count() == other.name().count() + 1) {
            if (DataUtils.compare(component(count() - 1), other.digest()) == 0) {
                return true;
            }
        }
        return false;
    }
