    public int findAnnotationIndex(int pos, int bias) {
        if (searchindex < 0 || searchindex >= annotations.size()) searchindex = annotations.size() / 2;
        Annotation anno;
        boolean found = false;
        int min = 0;
        int max = annotations.size() - 1;
        while (!found && searchindex < annotations.size()) {
            anno = (Annotation) annotations.get(searchindex);
            if (pos < anno.getStartOffset()) {
                if (searchindex == min || ((Annotation) annotations.get(searchindex - 1)).getEndOffset() - 1 < pos) {
                    switch(bias) {
                        case BIAS_NONE:
                            return -2;
                        case BIAS_LEFT:
                            return searchindex - 1;
                        case BIAS_RIGHT:
                            return searchindex;
                        default:
                            throw new IllegalArgumentException("bias invalid");
                    }
                } else {
                    max = searchindex - 1;
                    searchindex = min + (searchindex - min) / 2;
                }
            } else if (pos > anno.getEndOffset() - 1) {
                if (searchindex == max || ((Annotation) annotations.get(searchindex + 1)).getStartOffset() > pos) {
                    switch(bias) {
                        case BIAS_NONE:
                            return -2;
                        case BIAS_LEFT:
                            return searchindex;
                        case BIAS_RIGHT:
                            return searchindex + 1;
                        default:
                            throw new IllegalArgumentException("bias invalid");
                    }
                } else {
                    min = searchindex + 1;
                    searchindex = searchindex + (max - searchindex) / 2 + 1;
                }
            } else found = true;
        }
        return searchindex;
    }
