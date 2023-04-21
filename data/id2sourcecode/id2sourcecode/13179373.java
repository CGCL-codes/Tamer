    boolean setAnimationRelative(int direction) {
        int frameStep = this.frameStep * direction * currentDirection;
        int modelIndexNext = currentModelIndex + frameStep;
        boolean isDone = (modelIndexNext > firstModelIndex && modelIndexNext > lastModelIndex || modelIndexNext < firstModelIndex && modelIndexNext < lastModelIndex);
        if (isDone) {
            switch(animationReplayMode) {
                case ANIMATION_ONCE:
                    return false;
                case ANIMATION_LOOP:
                    modelIndexNext = (animationDirection == currentDirection ? firstModelIndex : lastModelIndex);
                    break;
                case ANIMATION_PALINDROME:
                    currentDirection = -currentDirection;
                    modelIndexNext -= 2 * frameStep;
            }
        }
        int nModels = viewer.getModelCount();
        if (modelIndexNext < 0 || modelIndexNext >= nModels) return false;
        setCurrentModelIndex(modelIndexNext);
        return true;
    }
