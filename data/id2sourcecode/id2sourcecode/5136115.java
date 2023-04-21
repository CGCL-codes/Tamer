    private int getCommentIndex(int start, int position, int exact) {
        if (position == 0) {
            if (this.comments.length > 0 && this.comments[0].getStartPosition() == 0) {
                return 0;
            }
            return -1;
        }
        int bottom = start, top = this.comments.length - 1;
        int i = 0, index = -1;
        Comment comment = null;
        while (bottom <= top) {
            i = bottom + (top - bottom) / 2;
            comment = this.comments[i];
            int commentStart = comment.getStartPosition();
            if (position < commentStart) {
                top = i - 1;
            } else if (position >= (commentStart + comment.getLength())) {
                bottom = i + 1;
            } else {
                index = i;
                break;
            }
        }
        if (index < 0 && exact != 0) {
            comment = this.comments[i];
            if (position < comment.getStartPosition()) {
                return exact < 0 ? i - 1 : i;
            } else {
                return exact < 0 ? i : i + 1;
            }
        }
        return index;
    }
