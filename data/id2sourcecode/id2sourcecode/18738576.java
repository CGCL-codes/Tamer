    private Integer drawChild(ChildInfo child, Integer xLeft, Element svg) {
        String text = StringUtils.join(child.getSubSentence().toArray(), " ");
        SVGJdom.drawText(text, xLeft + DrawConfig.getTextFontWidth() / 2, treeBottom + DrawConfig.getTextVertDistance(), DrawConfig.getTextColor(), DrawConfig.getTextFontSize().toString(), svg);
        int tw = SVGJdom.textWidth(child.getLabel() + ((child.getAttr().size() > 0) ? (child.getAttrString() + "()") : ""));
        Integer xRight = xLeft + max(SVGJdom.textWidth(text), SVGJdom.rectangleWidth(tw));
        SVGJdom.drawVertLine(xRight, treeBottom, imageBottom, svg);
        Integer nodeX = (xLeft + xRight) / 2;
        SVGJdom.drawVertLine(nodeX, nodeY, horizLineY, svg);
        SVGJdom.drawHorizTriangle(xLeft, xRight, treeBottom, nodeY + DrawConfig.getPillowSize() * 3 / 2, DrawConfig.getVarTriangleStrokeColor(), DrawConfig.getVarTriangleColor(), svg);
        SVGJdom.drawTextColoredSVGNode(nodeX, nodeY, child.getLabel(), tw, child.getAttr(), child.getDiffAttr(), DrawConfig.getFillColor(), DrawConfig.getStrokeColor(), svg);
        return xRight;
    }
