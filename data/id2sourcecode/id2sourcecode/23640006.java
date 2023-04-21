    private void sortDataRSSChannel(int start, int end) {
        RSSChannel pivot, temp;
        int lo = start;
        int hi = end;
        if (lo >= hi) return; else if (lo == hi - 1) {
            if (rssChannelArr[lo].getTitle().compareTo(rssChannelArr[hi].getTitle()) > 0) {
                pivot = rssChannelArr[lo];
                rssChannelArr[lo] = rssChannelArr[hi];
                rssChannelArr[hi] = pivot;
            }
            return;
        }
        tempInt = (lo + hi) / 2;
        pivot = rssChannelArr[tempInt];
        rssChannelArr[tempInt] = rssChannelArr[hi];
        rssChannelArr[hi] = pivot;
        while (lo < hi) {
            while ((rssChannelArr[lo].getTitle().compareTo(pivot.getTitle()) <= 0) && (lo < hi)) lo++;
            while ((pivot.getTitle().compareTo(rssChannelArr[hi].getTitle()) <= 0) && (lo < hi)) hi--;
            if (lo < hi) {
                temp = rssChannelArr[lo];
                rssChannelArr[lo] = rssChannelArr[hi];
                rssChannelArr[hi] = temp;
            }
        }
        rssChannelArr[end] = rssChannelArr[hi];
        rssChannelArr[hi] = pivot;
        sortDataRSSChannel(start, lo - 1);
        sortDataRSSChannel(hi + 1, end);
    }
