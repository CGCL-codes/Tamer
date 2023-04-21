    void setAnimationReplayMode(int animationReplayMode, float firstFrameDelay, float lastFrameDelay) {
        this.firstFrameDelay = firstFrameDelay > 0 ? firstFrameDelay : 0;
        firstFrameDelayMs = (int) (this.firstFrameDelay * 1000);
        this.lastFrameDelay = lastFrameDelay > 0 ? lastFrameDelay : 0;
        lastFrameDelayMs = (int) (this.lastFrameDelay * 1000);
        if (animationReplayMode >= ANIMATION_ONCE && animationReplayMode <= ANIMATION_PALINDROME) this.animationReplayMode = animationReplayMode; else Logger.error("invalid animationReplayMode:" + animationReplayMode);
    }
