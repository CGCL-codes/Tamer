    public void removeRoot(ActuatorContainer root) {
        for (int i = 0; i < acCount; i++) {
            if (acs[i] == root) {
                acs[i] = acs[i + 1];
                root = acs[i];
            }
        }
        acCount--;
    }
