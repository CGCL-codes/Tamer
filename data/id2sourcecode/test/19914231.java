    public void removeChild(String name, String value) {
        boolean found = false;
        for (int i = 0; i < values.length; i++) {
            if (!found) {
                if (values[i].getId().equals(name)) {
                    if (values[i].get().equals(value)) {
                        values[i] = null;
                        found = true;
                        numValidChildren--;
                    }
                }
            }
            if (i < (values.length - 1)) values[i] = values[i + 1];
        }
        if (found) values[values.length - 1] = null;
    }
