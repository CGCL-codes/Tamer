    private int resolveChannel(boolean bendMode) {
        return (bendMode ? this.route.getChannel2() : this.route.getChannel1());
    }
