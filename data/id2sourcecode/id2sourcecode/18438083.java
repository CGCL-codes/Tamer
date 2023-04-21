    @Override
    public DropFeedback onDropEnter(final INode targetNode, final IDragElement[] elements) {
        if (elements.length == 0) {
            return null;
        }
        Rect bn = targetNode.getBounds();
        if (!bn.isValid()) {
            return null;
        }
        boolean isVertical = isVertical(targetNode);
        List<MatchPos> indexes = new ArrayList<MatchPos>();
        int last = isVertical ? bn.y : bn.x;
        int pos = 0;
        boolean lastDragged = false;
        int selfPos = -1;
        for (INode it : targetNode.getChildren()) {
            Rect bc = it.getBounds();
            if (bc.isValid()) {
                boolean isDragged = false;
                for (IDragElement element : elements) {
                    if (bc.equals(element.getBounds())) {
                        isDragged = true;
                    }
                }
                if (isDragged) {
                    int v = isVertical ? bc.y + (bc.h / 2) : bc.x + (bc.w / 2);
                    selfPos = pos;
                    indexes.add(new MatchPos(v, pos++));
                } else if (lastDragged) {
                    pos++;
                } else {
                    int v = isVertical ? bc.y : bc.x;
                    v = (last + v) / 2;
                    indexes.add(new MatchPos(v, pos++));
                }
                last = isVertical ? (bc.y + bc.h) : (bc.x + bc.w);
                lastDragged = isDragged;
            } else {
                pos++;
            }
        }
        if (!lastDragged) {
            int v = last + 1;
            indexes.add(new MatchPos(v, pos));
        }
        int posCount = targetNode.getChildren().length + 1;
        return new DropFeedback(new LinearDropData(indexes, posCount, isVertical, selfPos), new IFeedbackPainter() {

            public void paint(IGraphics gc, INode node, DropFeedback feedback) {
                drawFeedback(gc, node, elements, feedback);
            }
        });
    }
