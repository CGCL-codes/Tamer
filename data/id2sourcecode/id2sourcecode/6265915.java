    private void outOfBitsStrategy(final algo_t that, final int sfwork[], final int vbrsfmin[], final int target) {
        int wrk[] = new int[L3Side.SFBMAX];
        final int dm = sfDepth(sfwork);
        final int p = that.cod_info.global_gain;
        {
            int bi = dm / 2;
            int bi_ok = -1;
            int bu = 0;
            int bo = dm;
            for (; ; ) {
                final int sfmax = flattenDistribution(sfwork, wrk, dm, bi, p);
                int nbits = tryThatOne(that, wrk, vbrsfmin, sfmax);
                if (nbits <= target) {
                    bi_ok = bi;
                    bo = bi - 1;
                } else {
                    bu = bi + 1;
                }
                if (bu <= bo) {
                    bi = (bu + bo) / 2;
                } else {
                    break;
                }
            }
            if (bi_ok >= 0) {
                if (bi != bi_ok) {
                    final int sfmax = flattenDistribution(sfwork, wrk, dm, bi_ok, p);
                    tryThatOne(that, wrk, vbrsfmin, sfmax);
                }
                return;
            }
        }
        {
            int bi = (255 + p) / 2;
            int bi_ok = -1;
            int bu = p;
            int bo = 255;
            for (; ; ) {
                final int sfmax = flattenDistribution(sfwork, wrk, dm, dm, bi);
                int nbits = tryThatOne(that, wrk, vbrsfmin, sfmax);
                if (nbits <= target) {
                    bi_ok = bi;
                    bo = bi - 1;
                } else {
                    bu = bi + 1;
                }
                if (bu <= bo) {
                    bi = (bu + bo) / 2;
                } else {
                    break;
                }
            }
            if (bi_ok >= 0) {
                if (bi != bi_ok) {
                    final int sfmax = flattenDistribution(sfwork, wrk, dm, dm, bi_ok);
                    tryThatOne(that, wrk, vbrsfmin, sfmax);
                }
                return;
            }
        }
        searchGlobalStepsizeMax(that, wrk, vbrsfmin, target);
    }
