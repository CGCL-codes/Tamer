    public ScoredClassification classify(Vector x) {
        int[] nzDims = x.nonZeroDimensions();
        int heapSize = 0;
        for (int k = 0; k < nzDims.length; ++k) if (nzDims[k] < mTermIndexes.length) ++heapSize;
        int[] current = new int[heapSize];
        float[] vals = new float[heapSize];
        int j = 0;
        for (int k = 0; k < heapSize; ++k) {
            if (nzDims[k] >= mTermIndexes.length) continue;
            current[j] = mTermIndexes[nzDims[k]];
            vals[j] = (float) x.value(nzDims[k]);
            ++j;
        }
        for (int k = (heapSize + 1) / 2; --k >= 0; ) heapify(k, heapSize, current, vals, mDocumentIds);
        BoundedPriorityQueue<ScoredDoc> queue = new BoundedPriorityQueue<ScoredDoc>(ScoredObject.comparator(), mMaxResults);
        int[] documentIds = mDocumentIds;
        while (heapSize > 0) {
            int doc = documentIds[current[0]];
            double score = 0.0;
            while (heapSize > 0 && documentIds[current[0]] == doc) {
                score += vals[0] * mScores[current[0]];
                ++current[0];
                if (documentIds[current[0]] == -1) {
                    --heapSize;
                    if (heapSize > 0) {
                        current[0] = current[heapSize];
                        vals[0] = vals[heapSize];
                    }
                }
                heapify(0, heapSize, current, vals, documentIds);
            }
            queue.offer(new ScoredDoc(doc, score));
        }
        String[] categories = new String[queue.size()];
        double[] scores = new double[queue.size()];
        int pos = 0;
        for (ScoredDoc sd : queue) {
            categories[pos] = Integer.toString(sd.docId());
            scores[pos] = sd.score();
            ++pos;
        }
        return new ScoredClassification(categories, scores);
    }
