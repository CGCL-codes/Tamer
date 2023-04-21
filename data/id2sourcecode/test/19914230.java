    public void removeChild(String name) {
        boolean found = false;
        for (int i = 0; i < values.length; i++) {
            if (!found) {
                if (values[i].getId().compareTo(name) == 0) {
                    values[i] = null;
                    found = true;
                    numValidChildren--;
                }
            }
            if (found && i < (values.length - 1)) values[i] = values[i + 1];
        }
        if (found) values[values.length - 1] = null;
    }
