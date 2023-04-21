    private void compMidPoint() {
        if (imgObjects.size() > 0) {
            IObjectImage pObject = (IObjectImage) imgObjects.get(0);
            lowX = pObject.getOrgX() - pObject.getWidthX() / 2;
            highX = pObject.getOrgX() + pObject.getWidthX() / 2;
            lowY = pObject.getOrgY() - pObject.getWidthY() / 2;
            highY = pObject.getOrgY() + pObject.getWidthY() / 2;
        }
        if (imgObjects.size() > 1) for (int i = 0; i < imgObjects.size(); i++) {
            IObjectImage pObject = (IObjectImage) imgObjects.get(i);
            double lowXo = pObject.getOrgX() - pObject.getWidthX() / 2;
            double highXo = pObject.getOrgX() + pObject.getWidthX() / 2;
            double lowYo = pObject.getOrgY() - pObject.getWidthY() / 2;
            double highYo = pObject.getOrgY() + pObject.getWidthY() / 2;
            if (lowXo < lowX) lowX = lowXo;
            if (highXo > highX) highX = highXo;
            if (lowYo < lowY) lowY = lowYo;
            if (highYo > highY) highY = highYo;
        }
        midPoint.x = (lowX + highX) / 2;
        midPoint.y = (lowY + highY) / 2;
    }
