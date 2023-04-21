    private int prettyPrintInternal(String[] lines) {
        int myLine = depth * 2;
        if (lines[myLine] == null) lines[myLine] = "";
        String lbl = getLabel();
        if (lbl == null) lbl = "X";
        if (isLeaf()) {
            if (lines[myLine].equals("")) lines[myLine] = lbl; else lines[myLine] += " " + lbl;
            return lines[myLine].length() - 1;
        }
        int childOffsets[] = new int[children.size()];
        int minChildOffset = childOffsets.length + 1;
        int maxChildOffset = 0;
        int i = 0;
        for (Tree c : children) {
            int o = c.prettyPrintInternal(lines);
            childOffsets[i] = o;
            if (o < minChildOffset) minChildOffset = o;
            if (o > maxChildOffset) maxChildOffset = o;
            i++;
        }
        int myOffset = minChildOffset + (maxChildOffset - minChildOffset) / 2;
        while (lines[myLine].length() < myOffset) lines[myLine] += " ";
        lines[myLine] += lbl;
        myOffset = lines[myLine].length() - 1;
        for (i = 0; i < childOffsets.length; i++) {
            char d = '|';
            if (childOffsets[i] < myOffset) d = '/'; else if (childOffsets[i] > myOffset) d = '\\';
            if (lines[myLine + 1] == null) lines[myLine + 1] = "";
            while (lines[myLine + 1].length() < childOffsets[i]) lines[myLine + 1] += " ";
            lines[myLine + 1] += d;
        }
        return myOffset;
    }
