    public void add(Arc arc) {
        int complexity = arc.getComplexity();
        if (complexity < requiredMinComplexity) return;
        if (!list.contains(arc)) {
            if (list.size() == 0) {
                list.add(arc);
            } else {
                int lowIdx = 0;
                int highIdx = list.size() - 1;
                int idx = 0;
                while (lowIdx <= highIdx) {
                    idx = (lowIdx + highIdx) / 2;
                    Arc curArc = (Arc) list.get(idx);
                    int curComplexity = curArc.getComplexity();
                    if (complexity == curComplexity) {
                        list.add(idx + 1, arc);
                        return;
                    }
                    if (complexity < curComplexity) highIdx = idx - 1; else lowIdx = idx + 1;
                }
                list.add(lowIdx, arc);
            }
        }
    }
