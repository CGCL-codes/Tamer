    public void removeReaction(int pos) {
        reactions[pos].removeListener(this);
        for (int i = pos; i < reactionCount - 1; i++) {
            reactions[i] = reactions[i + 1];
        }
        reactions[reactionCount - 1] = null;
        reactionCount--;
        notifyChanged();
    }
