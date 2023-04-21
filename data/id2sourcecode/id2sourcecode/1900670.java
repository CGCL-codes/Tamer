    public int relevantRetrieved(int documentsRetrieved) {
        int low = 0;
        int high = _relevantRetrieved.size() - 1;
        if (_relevantRetrieved.size() == 0) {
            return 0;
        }
        Document lastRelevant = _relevantRetrieved.get(high);
        if (lastRelevant.rank <= documentsRetrieved) {
            return _relevantRetrieved.size();
        }
        Document firstRelevant = _relevantRetrieved.get(low);
        if (firstRelevant.rank > documentsRetrieved) {
            return 0;
        }
        while ((high - low) >= 2) {
            int middle = low + (high - low) / 2;
            Document middleDocument = _relevantRetrieved.get(middle);
            if (middleDocument.rank == documentsRetrieved) {
                return middle + 1;
            } else if (middleDocument.rank > documentsRetrieved) {
                high = middle;
            } else {
                low = middle;
            }
        }
        assert _relevantRetrieved.get(low).rank <= documentsRetrieved && _relevantRetrieved.get(high).rank > documentsRetrieved;
        return low + 1;
    }
