    private boolean isMimeTypeSubclass(String mimeType, String subClass) {
        String umimeType = unaliasMimeType(mimeType);
        String usubClass = unaliasMimeType(subClass);
        MimeType _mimeType = new MimeType(umimeType);
        MimeType _subClass = new MimeType(usubClass);
        if (umimeType.compareTo(usubClass) == 0) {
            return true;
        }
        if (isSuperType(usubClass) && (_mimeType.getMediaType().equals(_subClass.getMediaType()))) {
            return true;
        }
        if (usubClass.equals("text/plain") && _mimeType.getMediaType().equals("text")) {
            return true;
        }
        if (usubClass.equals("application/octet-stream")) {
            return true;
        }
        int parentListOffset = getParentListOffset();
        int numParents = content.getInt(parentListOffset);
        int min = 0;
        int max = numParents - 1;
        while (max >= min) {
            int med = (min + max) / 2;
            int offset = content.getInt((parentListOffset + 4) + (8 * med));
            String parentMime = getMimeType(offset);
            int cmp = parentMime.compareTo(umimeType);
            if (cmp < 0) {
                min = med + 1;
            } else if (cmp > 0) {
                max = med - 1;
            } else {
                offset = content.getInt((parentListOffset + 4) + (8 * med) + 4);
                int _numParents = content.getInt(offset);
                for (int i = 0; i < _numParents; i++) {
                    int parentOffset = content.getInt((offset + 4) + (4 * i));
                    if (isMimeTypeSubclass(getMimeType(parentOffset), usubClass)) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }
