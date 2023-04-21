    public boolean contains(Object element) {
        if (groupOfContact(element.toString()) != null) {
            return true;
        }
        if (getChannel(element.toString()) != null) {
            return true;
        }
        return false;
    }
