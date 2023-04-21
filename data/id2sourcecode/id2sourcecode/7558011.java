    public static int search(String x) {
        RuleBasedCollator coll;
        Vector<String> uniqueWords;
        int[] appearIdx;
        if (translationDirection == TranslationDirection.FIRST_SECOND_LANGUAGE) {
            coll = (RuleBasedCollator) firstLangColl;
            uniqueWords = uniqueFirstLangWords;
            appearIdx = firstLangAppearIdx;
        } else {
            coll = (RuleBasedCollator) secondLangColl;
            uniqueWords = uniqueSecondLangWords;
            appearIdx = secondLangAppearIdx;
        }
        int low = 0;
        int high = uniqueWords.size() - 1;
        int mid;
        int tries = 0;
        int beginsWithIdx = -1;
        while (low <= high) {
            tries++;
            mid = (low + high) / 2;
            String midTranslationText = uniqueWords.get(mid);
            int compResult = coll.compare(midTranslationText, x);
            if (compResult < 0) {
                low = mid + 1;
            } else if (compResult > 0) {
                CollationElementIterator transTextIt = coll.getCollationElementIterator(midTranslationText);
                CollationElementIterator searchTextIt = coll.getCollationElementIterator(x);
                int transTextElem = transTextIt.next();
                int searchTextElem = searchTextIt.next();
                boolean beginsWith = false;
                while (transTextElem != CollationElementIterator.NULLORDER && searchTextElem != CollationElementIterator.NULLORDER) {
                    if (transTextElem != searchTextElem) {
                        beginsWith = false;
                        break;
                    } else {
                        beginsWith = true;
                    }
                    transTextElem = transTextIt.next();
                    searchTextElem = searchTextIt.next();
                }
                if (beginsWith) beginsWithIdx = mid;
                high = mid - 1;
            } else {
                System.out.println("lookup took " + tries + " tries.");
                return (mid > 0 && mid < appearIdx.length) ? appearIdx[mid] : -1;
            }
        }
        return (beginsWithIdx > 0 && beginsWithIdx < appearIdx.length) ? appearIdx[beginsWithIdx] : -1;
    }
