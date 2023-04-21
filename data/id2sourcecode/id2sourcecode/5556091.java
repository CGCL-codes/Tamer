    @Action
    public void search() throws IOException {
        int lo = 0, mid, hi = this.index.size();
        String term = this.tfSearch.getText();
        while (lo < hi) {
            mid = (lo + hi) / 2;
            if (term.compareTo(this.index.get(mid).first) < 0) hi = mid - 1; else if (term.compareTo(this.index.get(mid).first) > 0) lo = mid + 1; else lo = hi = mid;
        }
        while (lo > 0 && this.index.get(lo).first.equals(this.index.get(lo - 1).first)) lo--;
        if (lo > 0 && term.compareTo(this.index.get(lo).first) < 0) lo--;
        displayWord(lo);
    }
