    int bisect_forward_serialno(long begin, long searched, long end, int currentno, int m) {
        long endsearched = end;
        long next = end;
        Page page = new Page();
        int ret;
        while (searched < endsearched) {
            long bisect;
            if (endsearched - searched < CHUNKSIZE) {
                bisect = searched;
            } else {
                bisect = (searched + endsearched) / 2;
            }
            seek_helper(bisect);
            ret = get_next_page(page, -1);
            if (ret == OV_EREAD) return OV_EREAD;
            if (ret < 0 || page.serialno() != currentno) {
                endsearched = bisect;
                if (ret >= 0) next = ret;
            } else {
                searched = ret + page.header_len + page.body_len;
            }
        }
        seek_helper(next);
        ret = get_next_page(page, -1);
        if (ret == OV_EREAD) return OV_EREAD;
        if (searched >= end || ret == -1) {
            links = m + 1;
            offsets = new long[m + 2];
            offsets[m + 1] = searched;
        } else {
            ret = bisect_forward_serialno(next, offset, end, page.serialno(), m + 1);
            if (ret == OV_EREAD) return OV_EREAD;
        }
        offsets[m] = begin;
        return 0;
    }
