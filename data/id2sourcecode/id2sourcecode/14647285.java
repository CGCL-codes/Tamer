    public static XSectionProfile calculateProfileFrom(XSection xSection, Channel channel, Node upNode, Node downNode) {
        XSectionProfile profile = new XSectionProfile();
        profile.setChannelId(Integer.parseInt(xSection.getChannelId()));
        profile.setDistance(xSection.getDistance());
        List<double[]> endPoints = new ArrayList<double[]>();
        profile.setEndPoints(endPoints);
        LatLng[] endPointLatLngs = calculateEndPoints(xSection, channel, upNode, downNode);
        for (LatLng endPointLatLng : endPointLatLngs) {
            double[] point = new double[2];
            point[0] = endPointLatLng.getLatitude();
            point[1] = endPointLatLng.getLongitude();
            endPoints.add(point);
        }
        List<double[]> profilePoints = new ArrayList<double[]>();
        profile.setProfilePoints(profilePoints);
        double maxWidth = getMaxTopWidth(xSection);
        for (XSectionLayer layer : xSection.getLayers()) {
            double w = layer.getTopWidth();
            double[] point1 = new double[2];
            double[] point2 = new double[2];
            point1[0] = maxWidth / 2 - w / 2;
            point1[1] = layer.getElevation();
            if (w > 0) {
                point2[0] = maxWidth / 2 + w / 2;
                point2[1] = layer.getElevation();
            }
            profilePoints.add(0, point1);
            if (w > 0) {
                profilePoints.add(profilePoints.size(), point2);
            }
        }
        return profile;
    }
