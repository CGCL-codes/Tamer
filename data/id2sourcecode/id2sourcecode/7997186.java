    private Envelope coordinatePNU(Envelope currEnvelope, Envelope toEnvelope) throws Exception {
        try {
            double maxX = currEnvelope.getMaxX();
            double maxY = currEnvelope.getMaxY();
            double minX = currEnvelope.getMinX();
            double minY = currEnvelope.getMinY();
            double newMaxX = toEnvelope.getMaxX();
            double newMaxY = toEnvelope.getMaxY();
            double newMinX = toEnvelope.getMinX();
            double newMinY = toEnvelope.getMinY();
            double pointX = (newMaxX + newMinX) / 2;
            double pointY = (newMaxY + newMinY) / 2;
            newMaxX += (newMaxX - pointX) / 2;
            newMaxY += (newMaxY - pointY) / 2;
            newMinX -= (newMaxX - pointX) / 2;
            newMinY -= (newMaxY - pointY) / 2;
            double ratioX = (maxX - minX) / (newMaxX - newMinX);
            double ratioY = (maxY - minY) / (newMaxY - newMinY);
            if (ratioX > ratioY) {
                double distY = (newMaxX - newMinX) * (maxY - minY) / (maxX - minX);
                newMinY = pointY - distY / 2;
                newMaxY = pointY + distY / 2;
            } else if (ratioX < ratioY) {
                double distX = (maxX - minX) * (newMaxY - newMinY) / (maxY - minY);
                newMinX = pointX - distX / 2;
                newMaxX = pointX + distX / 2;
            }
            toEnvelope.setMinX(newMinX);
            toEnvelope.setMinY(newMinY);
            toEnvelope.setMaxX(newMaxX);
            toEnvelope.setMaxY(newMaxY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toEnvelope;
    }
