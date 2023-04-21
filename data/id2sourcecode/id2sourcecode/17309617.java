    public static Collection findMedialAxisCollection(Collection features, FeatureSchema featureSchema) {
        ArrayList<Feature> medialAxisLineStringFeatures = new ArrayList<Feature>();
        boolean allreadyAllLines = true;
        for (Iterator j = features.iterator(); j.hasNext(); ) {
            Feature f = (Feature) j.next();
            if (f.getGeometry().getNumPoints() != 2) {
                allreadyAllLines = false;
                break;
            }
        }
        if (allreadyAllLines) return features;
        Collection combinedFeatures = GeoUtils.combineOverlappingFeatures(features, featureSchema);
        for (Iterator j = combinedFeatures.iterator(); j.hasNext(); ) {
            CoordinateList coordinateList = new CoordinateList();
            Feature f = (Feature) j.next();
            Geometry geo = f.getGeometry();
            coordinateList.add(geo.getCoordinates(), true);
            CoordinateList coordList = GeoUtils.ConvexHullWrap(coordinateList);
            if (coordList.size() == coordinateList.size()) coordList = coordinateList; else {
                int sideOne = coordList.indexOf(coordinateList.getCoordinate(0));
                if (sideOne > 0) {
                    Coordinate[] p = coordList.toCoordinateArray();
                    for (int k = 0; k < sideOne; k++) {
                        int n = p.length - 2;
                        Coordinate t = p[0];
                        for (int i = 0; i < n; i++) p[i] = p[i + 1];
                        p[n] = t;
                    }
                    p[p.length - 1] = p[0];
                    coordList = new CoordinateList();
                    for (int k = 0; k < p.length; k++) coordList.add(p[k], true);
                }
            }
            LineString medialAxis = GeoUtils.findMedialAxis(coordList.toCoordinateArray());
            Feature newFeature = new BasicFeature(featureSchema);
            if (featureSchema.hasAttribute("ASHS_ID") && geo.getUserData() != null) {
                String id = (String) geo.getUserData();
                newFeature.setAttribute("ASHS_ID", id);
                geo.setUserData(null);
            }
            newFeature.setGeometry(medialAxis);
            medialAxisLineStringFeatures.add(newFeature);
        }
        return medialAxisLineStringFeatures;
    }
