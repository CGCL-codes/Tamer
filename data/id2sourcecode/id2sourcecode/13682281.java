    public void removeAtomContainer(int pos) {
        for (int i = pos; i < atomContainerCount - 1; i++) {
            atomContainers[i] = atomContainers[i + 1];
            multipliers[i] = multipliers[i + 1];
        }
        atomContainers[atomContainerCount - 1] = null;
        atomContainerCount--;
    }
