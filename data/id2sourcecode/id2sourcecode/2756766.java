    private int checkState(Point mousepoint) {
        Point p = translateScreenToComponent(mousepoint);
        int radius = 7;
        Rectangle r = this.getAnchorRect();
        r.setBounds(r.x + anchorThickness, r.y + anchorThickness, r.width - anchorThickness, r.height - anchorThickness);
        if (r.x < p.x && r.width > p.x && r.y < p.y && r.height > p.y) {
            return LabelArrangement.ACTIVATION_INNER_AREA;
        } else {
            int x = r.x;
            int y = r.y;
            int width = r.x + r.width - 6;
            int height = r.y + r.height - 6;
            int centerx = (x + width) / 2;
            int centery = (y + height) / 2;
            if (p.distance(x, y) < radius) return LabelArrangement.ACTIVATION_BTL;
            if (p.distance(centerx, y) < radius) return LabelArrangement.ACTIVATION_BTM;
            if (p.distance(width, y) < radius) return LabelArrangement.ACTIVATION_BTR;
            if (p.distance(x, centery) < radius) return LabelArrangement.ACTIVATION_BML;
            if (p.distance(width, centery) < radius) return LabelArrangement.ACTIVATION_BMR;
            if (p.distance(x, height) < radius) return LabelArrangement.ACTIVATION_BBL;
            if (p.distance(centerx, height) < radius) return LabelArrangement.ACTIVATION_BBM;
            if (p.distance(width, height) < radius) return LabelArrangement.ACTIVATION_BBR;
            return LabelArrangement.ACTIVATION_BORDER_AREA;
        }
    }
