    public void RemoveElement(ElementBase aE) {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == aE) {
                if (i < elements.length - 1) {
                    elements[i] = elements[i + 1];
                    elements[i + 1] = null;
                } else {
                    elements[i] = null;
                }
            }
        }
        changed = true;
    }
