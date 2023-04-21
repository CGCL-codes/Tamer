    public synchronized void remove(Axis Axis) {
        for (int i = 0; i < size - 1; i++) {
            if (axes[i] == Axis) {
                for (; i < size - 1; i++) {
                    axes[i] = axes[i + 1];
                }
            }
        }
        size--;
        axes[size] = null;
    }
