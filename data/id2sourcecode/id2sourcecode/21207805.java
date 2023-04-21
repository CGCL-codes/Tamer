    private void calcBorders(BorderAndPadding bp) {
        if (this.bSepBorders) {
            int iSep = properties.get("border-separation.inline-progression-direction").getLength().mvalue();
            int iSpacing = properties.get("border-spacing.inline-progression-direction").getLength().mvalue();
            if (iSpacing > iSep) iSep = iSpacing;
            this.startAdjust = iSep / 2 + bp.getBorderLeftWidth(false) + bp.getPaddingLeft(false);
            this.widthAdjust = startAdjust + iSep - iSep / 2 + bp.getBorderRightWidth(false) + bp.getPaddingRight(false);
            m_borderSeparation = properties.get("border-separation.block-progression-direction").getLength().mvalue();
            int m_borderSpacing = properties.get("border-spacing.block-progression-direction").getLength().mvalue();
            if (m_borderSpacing > m_borderSeparation) m_borderSeparation = m_borderSpacing;
            this.beforeOffset = m_borderSeparation / 2 + bp.getBorderTopWidth(false) + bp.getPaddingTop(false);
        } else {
            int borderStart = bp.getBorderLeftWidth(false);
            int borderEnd = bp.getBorderRightWidth(false);
            int borderBefore = bp.getBorderTopWidth(false);
            int borderAfter = bp.getBorderBottomWidth(false);
            this.startAdjust = borderStart / 2 + bp.getPaddingLeft(false);
            this.widthAdjust = startAdjust + borderEnd / 2 + bp.getPaddingRight(false);
            this.beforeOffset = borderBefore / 2 + bp.getPaddingTop(false);
            this.borderHeight = (borderBefore + borderAfter) / 2;
        }
    }
