    private int checkDragState(Point mousepoint) {
        VGArrow vg = (VGArrow) refBase;
        Point p = translateScreenToComponent(mousepoint);
        int radius = 7;
        Rectangle r = this.getAnchorRect();
        r.setBounds(r.x + anchorThickness, r.y + anchorThickness, r.width - anchorThickness, r.height - anchorThickness);
        int x = r.x;
        int y = r.y;
        int width = r.x + r.width - 6;
        int height = r.y + r.height - 6;
        int centerx = (x + width) / 2;
        int centery = (y + height) / 2;
        int dx = vg.getMinBoxWidth();
        int dy = vg.getMinBoxHeight();
        if (xChange == -1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BBR && refBase.getWidth() < dx) {
            vg.decArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BBM;
        }
        if (xChange == -1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BBM && p.x < dx) {
            vg.decArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BBL;
        }
        if (yChange == -1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BBL && refBase.getHeight() < dy) {
            vg.decArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BML;
        }
        if (yChange == -1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BML && p.y < dy) {
            vg.decArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BTL;
        }
        if (xChange == +1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BTL && refBase.getWidth() < dx) {
            vg.decArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BTM;
        }
        if (xChange == +1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BTM && p.x > width - dx) {
            vg.decArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BTR;
        }
        if (yChange == 1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BTR && refBase.getHeight() < dy) {
            vg.decArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BMR;
        }
        if (yChange == 1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BMR && p.y > height - dy) {
            vg.decArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BBR;
        }
        if (yChange == -1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BBR && refBase.getHeight() < dy) {
            vg.incArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BMR;
        }
        if (yChange == -1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BMR && p.y > height - dy) {
            vg.incArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BTR;
        }
        if (xChange == -1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BTR && refBase.getWidth() < dy) {
            vg.incArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BTM;
        }
        if (xChange == -1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BTM && p.x < dx) {
            vg.incArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BTL;
        }
        if (yChange == 1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BTL && refBase.getHeight() < dy) {
            vg.incArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BML;
        }
        if (yChange == 1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BML && p.y > height - dy) {
            vg.incArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BBL;
        }
        if (xChange == 1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BBL && refBase.getWidth() < dx) {
            vg.incArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BBM;
        }
        if (xChange == 1 && this.getResizeMode() == FeatureArrowArrangement.ACTIVATION_BBM && p.x > width - dx) {
            vg.incArrowHeadQuarter();
            return FeatureArrowArrangement.ACTIVATION_BBR;
        }
        return this.getResizeMode();
    }
