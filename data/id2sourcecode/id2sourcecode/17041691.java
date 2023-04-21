    public SyntaxPoint[] getSyntaxPoints() {
        ArrayList list = new ArrayList(syntaxPoints);
        Collections.sort(list);
        SyntaxPoint[] result = (SyntaxPoint[]) list.toArray(new SyntaxPoint[syntaxPoints.size()]);
        for (int i = 0; i + 1 < result.length; i++) {
            if (result[i].getType().equals("TextLine") && result[i + 1].getType().equals("TextLine") && result[i].getLine() == result[i + 1].getLine() && result[i].getColumn() == result[i + 1].getColumn() && !result[i].isBegin() && result[i + 1].isBegin()) {
                SyntaxPoint temp = result[i];
                result[i] = result[i + 1];
                result[i + 1] = temp;
            }
        }
        return result;
    }
