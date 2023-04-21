    private void splitInT(Bin source, int start, int end) {
        if (source.isnonempty()) {
            if (start != end) {
                int i = start + (end - start) / 2;
                Bin left = new Bin();
                Bin right = new Bin();
                split(source, left, right, 1, tpbrkpts.pts[i + 1]);
                splitInT(left, start, i);
                splitInT(right, i + 1, end);
            } else {
                if (start == tpbrkpts.start || start == tpbrkpts.end) {
                    freejarcs(source);
                } else if (renderhints.display_method == NurbsConsts.N_OUTLINE_PARAM_ST) {
                    outline(source);
                    freejarcs(source);
                } else {
                    t_index = start;
                    setArcTypeBezier();
                    setDegenerate();
                    float[] pta = new float[2];
                    float[] ptb = new float[2];
                    pta[0] = spbrkpts.pts[s_index - 1];
                    pta[1] = tpbrkpts.pts[t_index - 1];
                    ptb[0] = spbrkpts.pts[s_index];
                    ptb[1] = tpbrkpts.pts[t_index];
                    qlist.downloadAll(pta, ptb, backend);
                    Patchlist patchlist = new Patchlist(qlist, pta, ptb);
                    samplingSplit(source, patchlist, renderhints.maxsubdivisions, 0);
                    setNonDegenerate();
                    setArcTypeBezier();
                }
            }
        }
    }
