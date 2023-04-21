    private LanguageImpl find(String name, boolean bestMatch, boolean add) {
        int prefixLength = 0;
        if (this.prefix != null) {
            if (!name.startsWith(this.prefix)) {
                return null;
            }
            prefixLength = this.prefix.length();
        }
        String partialName;
        int pos = name.indexOf('-', prefixLength);
        if (pos >= 0) {
            partialName = name.substring(0, pos);
        } else {
            partialName = name;
            name = null;
        }
        int start = 0;
        int end = this.storedObject.getChildCount();
        while (start < end) {
            int mid = (start + end) / 2;
            StoredObject language = this.storedObject.getChild(mid, Id.LANGUAGE, SubId.NONE);
            int result = LanguageImpl.compareLanguageName(language, partialName);
            if (result > 0) {
                end = mid;
            } else if (result < 0) {
                start = mid + 1;
            } else {
                if (name == null) {
                    if (add) {
                        return null;
                    } else {
                        return new LanguageImpl(language);
                    }
                } else {
                    LanguageImpl partial = new LanguageImpl(language);
                    LanguageImpl full = partial.getSubLanguages().find(name, bestMatch, add);
                    if (full == null && bestMatch) {
                        return partial;
                    } else {
                        return full;
                    }
                }
            }
        }
        if (add) {
            StoredObject language = this.storedObject.insertChild(start, Id.LANGUAGE, SubId.NONE);
            language.setString(0, partialName);
            LanguageImpl partial = new LanguageImpl(language);
            if (name == null) {
                return partial;
            } else {
                return partial.getSubLanguages().find(name, bestMatch, add);
            }
        } else {
            return null;
        }
    }
