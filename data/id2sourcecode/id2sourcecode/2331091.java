    protected static int findLanguage(StoredObject contentObject, int start, int end, StoredObject language, boolean single, boolean add) {
        if (language == null) {
            if (single && start < end && contentObject.getReference(start) == null) {
                return start;
            } else if (add) {
                for (; start < end; start++) {
                    if (contentObject.getReference(start) != null) {
                        break;
                    }
                }
            }
        } else {
            String name = LanguageImpl.getLanguageName(language);
            while (start < end) {
                int mid = (start + end) / 2;
                int result = LanguageImpl.compareLanguageName(contentObject.getReference(mid, Id.LANGUAGE, SubId.NONE), name);
                if (single && result == 0) {
                    return mid;
                } else if (result >= (add ? 1 : 0)) {
                    end = mid;
                } else {
                    start = mid + 1;
                }
            }
        }
        if (add) {
            contentObject.insertReference(start, language);
            return ~start;
        } else if (single) {
            return end;
        } else {
            return start;
        }
    }
