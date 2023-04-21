    private void calculateCenter() {
        if (container == null) {
            return;
        }
        double min = container.getMinlat();
        double max = container.getMaxlat();
        centerLat = (min + max) / 2;
        min = container.getMinlon();
        max = container.getMaxlon();
        centerLon = (min + max) / 2;
    }
