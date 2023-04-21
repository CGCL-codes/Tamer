    private void splitInS(Bin source, int start, int end) {
        if (source.isnonempty()) {
            if (start != end) {
                int i = start + (end - start) / 2;
                Bin left = new Bin();
                Bin right = new Bin();
                split(source, left, right, 0, spbrkpts.pts[i]);
                splitInS(left, start, i);
                splitInS(right, i + 1, end);
            } else {
                if (start == spbrkpts.start || start == spbrkpts.end) {
                    freejarcs(source);
                } else if (renderhints.display_method == NurbsConsts.N_OUTLINE_PARAM_S) {
                    outline(source);
                    freejarcs(source);
                } else {
                    setArcTypeBezier();
                    setNonDegenerate();
                    s_index = start;
                    splitInT(source, tpbrkpts.start, tpbrkpts.end);
                }
            }
        } else {
        }
    }
