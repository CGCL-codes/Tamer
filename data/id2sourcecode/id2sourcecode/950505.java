    void deletePFrame() {
        if (npframes > 1) {
            for (int i = upba.currentFrameIndex; i < npframes - 1; i++) {
                pframes[i] = pframes[i + 1];
            }
            pframes[npframes - 1] = null;
            if (upba.currentFrameIndex > 0) {
                upba.currentFrameIndex--;
            }
            npframes--;
            this.resethist();
        }
    }
