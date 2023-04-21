    public Keyword addAndGet(String keyword, char type) {
        if (list.size() == 0) {
            Keyword k = new Keyword(keyword);
            list.add(k);
            return k;
        }
        int left = 0;
        int right = list.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (getKeywordString(mid).compareTo(keyword) < 0) left = mid + 1; else if (getKeywordString(mid).compareTo(keyword) > 0) right = mid - 1; else {
                if (!get(mid).hasGenre(type)) get(mid).addGenre(type);
                return get(mid);
            }
        }
        Keyword k = new Keyword(keyword, type);
        if (left >= list.size()) list.add(k); else list.add(left, k);
        return k;
    }
