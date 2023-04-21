    void image(int subsample, GRect rect, int index, final byte[] img8, int rowsize, int pixsep, boolean fast) {
        int nlevel = 0;
        while ((nlevel < 5) && ((32 >> nlevel) > subsample)) {
            nlevel++;
        }
        final int boxsize = 1 << nlevel;
        if (subsample != (32 >> nlevel)) {
            throw new IllegalArgumentException("(IWMap::image) Unsupported subsampling factor");
        }
        if (rect.isEmpty()) {
            throw new IllegalArgumentException("(IWMap::image) GRect is empty");
        }
        GRect irect = new GRect(0, 0, ((iw + subsample) - 1) / subsample, ((ih + subsample) - 1) / subsample);
        if ((rect.xmin < 0) || (rect.ymin < 0) || (rect.xmax > irect.xmax) || (rect.ymax > irect.ymax)) {
            throw new IllegalArgumentException("(IWMap::image) GRect is out of bounds: " + rect.xmin + "," + rect.ymin + "," + rect.xmax + "," + rect.ymax + "," + irect.xmax + "," + irect.ymax);
        }
        GRect[] needed = new GRect[8];
        GRect[] recomp = new GRect[8];
        for (int i = 0; i < 8; ) {
            needed[i] = new GRect();
            recomp[i++] = new GRect();
        }
        int r = 1;
        needed[nlevel] = (GRect) rect.clone();
        recomp[nlevel] = (GRect) rect.clone();
        for (int i = nlevel - 1; i >= 0; i--) {
            needed[i] = recomp[i + 1];
            needed[i].inflate(3 * r, 3 * r);
            needed[i].intersect(needed[i], irect);
            r += r;
            recomp[i].xmin = ((needed[i].xmin + r) - 1) & ~(r - 1);
            recomp[i].xmax = needed[i].xmax & ~(r - 1);
            recomp[i].ymin = ((needed[i].ymin + r) - 1) & ~(r - 1);
            recomp[i].ymax = needed[i].ymax & ~(r - 1);
        }
        GRect work = new GRect();
        work.xmin = needed[0].xmin & ~(boxsize - 1);
        work.ymin = needed[0].ymin & ~(boxsize - 1);
        work.xmax = ((needed[0].xmax - 1) & ~(boxsize - 1)) + boxsize;
        work.ymax = ((needed[0].ymax - 1) & ~(boxsize - 1)) + boxsize;
        final int dataw = work.width();
        final short[] data = new short[dataw * work.height()];
        int blkw = bw >> 5;
        int lblock = ((work.ymin >> nlevel) * blkw) + (work.xmin >> nlevel);
        final short[] liftblock = new short[1024];
        for (int by = work.ymin, ldata = 0; by < work.ymax; by += boxsize, ldata += (dataw << nlevel), lblock += blkw) {
            for (int bx = work.xmin, bidx = lblock, rdata = ldata; bx < work.xmax; bx += boxsize, bidx++, rdata += boxsize) {
                IWBlock block = blocks[bidx];
                int mlevel = nlevel;
                if ((nlevel > 2) && (((bx + 31) < needed[2].xmin) || (bx > needed[2].xmax) || ((by + 31) < needed[2].ymin) || (by > needed[2].ymax))) {
                    mlevel = 2;
                }
                final int bmax = ((1 << (mlevel + mlevel)) + 15) >> 4;
                final int ppinc = 1 << (nlevel - mlevel);
                final int ppmod1 = dataw << (nlevel - mlevel);
                final int ttmod0 = 32 >> mlevel;
                final int ttmod1 = ttmod0 << 5;
                block.write_liftblock(liftblock, 0, bmax);
                for (int ii = 0, tt = 0, pp = rdata; ii < boxsize; ii += ppinc, pp += ppmod1, tt += (ttmod1 - 32)) {
                    for (int jj = 0; jj < boxsize; jj += ppinc, tt += ttmod0) {
                        data[pp + jj] = liftblock[tt];
                    }
                }
            }
        }
        r = boxsize;
        for (int i = 0; i < nlevel; i++) {
            GRect comp = needed[i];
            comp.xmin = comp.xmin & ~(r - 1);
            comp.ymin = comp.ymin & ~(r - 1);
            comp.translate(-work.xmin, -work.ymin);
            if (fast && (i >= 4)) {
                for (int ii = comp.ymin, pp = (comp.ymin * dataw); ii < comp.ymax; ii += 2, pp += (dataw + dataw)) {
                    for (int jj = comp.xmin; jj < comp.xmax; jj += 2) {
                        data[pp + jj + dataw] = data[pp + jj + dataw + 1] = data[pp + jj + 1] = data[pp + jj];
                    }
                }
                break;
            }
            backward(data, (comp.ymin * dataw) + comp.xmin, comp.width(), comp.height(), dataw, r, r >> 1);
            r >>= 1;
        }
        GRect nrect = (GRect) rect.clone();
        nrect.translate(-work.xmin, -work.ymin);
        for (int i = nrect.ymin, pidx = (nrect.ymin * dataw), ridx = index; i++ < nrect.ymax; ridx += rowsize, pidx += dataw) {
            for (int j = nrect.xmin, pixidx = ridx; j < nrect.xmax; j++, pixidx += pixsep) {
                int x = (data[pidx + j] + 32) >> 6;
                if (x < -128) {
                    x = -128;
                } else if (x > 127) {
                    x = 127;
                }
                img8[pixidx] = (byte) x;
            }
        }
    }
