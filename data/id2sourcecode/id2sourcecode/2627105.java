    public Object dequeue() {
        if (qsize == 0) return null;
        for (; ; ) {
            int i = lastbucket;
            do {
                _Element e_ = buckets[i];
                if ((e_ != null) && (e_.key < buckettop)) {
                    buckets[i] = e_.next;
                    lastbucket = i;
                    lastkey = e_.key;
                    if (--qsize < bot_threshold && resizeenabled) resize(nbuckets >> 1);
                    return e_.obj;
                } else {
                    if (++i == nbuckets) {
                        i = 0;
                        buckettop = prevtop + nbuckets * width;
                        prevtop = buckettop;
                    } else {
                        buckettop += width;
                    }
                }
            } while (i != lastbucket);
            numDirectSearch++;
            int pos_ = 0;
            _Element min_;
            do {
                min_ = buckets[pos_++];
            } while (min_ == null);
            pos_--;
            int k;
            for (k = pos_ + 1; k < nbuckets; k++) {
                _Element e_ = buckets[k];
                if ((e_ != null) && (e_.key < min_.key)) {
                    min_ = e_;
                    pos_ = k;
                }
            }
            lastbucket = pos_;
            lastkey = min_.key;
            long n = (long) (min_.key * oneonwidth);
            buckettop = width * (n + 1.5);
            prevtop = buckettop - lastbucket * width;
        }
    }
