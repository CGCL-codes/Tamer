    public void processEvent(JSONObject evt) {
        if ("moveCenter".equals(evt.get("method"))) {
            moveCenter(((Number) evt.get("xOffset")).intValue(), ((Number) evt.get("yOffset")).intValue(), ((Number) evt.get("width")).intValue(), ((Number) evt.get("height")).intValue());
        } else if ("mapClicked".equals(evt.get("method"))) {
            mapClicked(((Number) evt.get("x")).intValue(), ((Number) evt.get("y")).intValue(), ((Number) evt.get("width")).intValue(), ((Number) evt.get("height")).intValue());
        } else if ("mapDragStarted".equals(evt.get("method"))) {
            mapDragStarted(((Number) evt.get("x")).intValue(), ((Number) evt.get("y")).intValue(), ((Number) evt.get("width")).intValue(), ((Number) evt.get("height")).intValue());
        } else if ("mapDragging".equals(evt.get("method"))) {
            mapDragging(((Number) evt.get("startX")).intValue(), ((Number) evt.get("startY")).intValue(), ((Number) evt.get("dragX")).intValue(), ((Number) evt.get("dragY")).intValue(), ((Number) evt.get("width")).intValue(), ((Number) evt.get("height")).intValue());
        } else if ("mapDragged".equals(evt.get("method"))) {
            int x1, x2, y1, y2, width, height;
            try {
                x1 = ((Number) evt.get("x1")).intValue();
                x2 = ((Number) evt.get("x2")).intValue();
                y1 = ((Number) evt.get("y1")).intValue();
                y2 = ((Number) evt.get("y2")).intValue();
                width = ((Number) evt.get("width")).intValue();
                height = ((Number) evt.get("height")).intValue();
            } catch (Exception e) {
                throw new IllegalArgumentException("Expected integers: x1, x2, y1, y2, width, height", e);
            }
            mapDragged(x1, y1, x2, y2, width, height);
        } else if ("areaSelected".equals(evt.get("method"))) {
            int x1, x2, y1, y2, width, height;
            try {
                x1 = ((Number) evt.get("x1")).intValue();
                x2 = ((Number) evt.get("x2")).intValue();
                y1 = ((Number) evt.get("y1")).intValue();
                y2 = ((Number) evt.get("y2")).intValue();
                width = ((Number) evt.get("width")).intValue();
                height = ((Number) evt.get("height")).intValue();
            } catch (Exception e) {
                throw new IllegalArgumentException("Expected integers: x1, x2, y1, y2, width, height", e);
            }
            com.bbn.openmap.proj.Projection pro = createProjection(width, height);
            LatLonPoint omPoint1 = pro.inverse(x1, y1);
            LatLonPoint omPoint2 = pro.inverse(x2, y2);
            float lat1, lon1, lat2, lon2;
            lat1 = omPoint1.getLatitude();
            lon1 = omPoint1.getLongitude();
            lat2 = omPoint2.getLatitude();
            lon2 = omPoint2.getLongitude();
            if (lat1 > lat2) {
                float temp = lat1;
                lat1 = lat2;
                lat2 = temp;
            }
            if (lon1 > lon2) {
                float temp = lon1;
                lon1 = lon2;
                lon2 = temp;
            }
            if (x2 - x1 < 2 || y2 - y1 < 2) move((lat1 + lat2) / 2, (lon1 + lon2) / 2); else setExtent(lat1, lon1, lat2, lon2);
            draw();
        } else if ("panLeft".equals(evt.get("method"))) {
            float width = theMaxLon - theMinLon;
            if (width < 0) width += 360;
            float newMinLon = theMinLon - width * theHorizPanAmount;
            float newMaxLon = theMaxLon - width * theHorizPanAmount;
            if (newMinLon <= -180) {
                newMinLon += 360;
                newMaxLon += 360;
            }
            theMinLon = newMinLon;
            theMaxLon = newMaxLon;
            draw();
        } else if ("panRight".equals(evt.get("method"))) {
            float width = theMaxLon - theMinLon;
            if (width < 0) width += 360;
            float newMinLon = theMinLon + width * theHorizPanAmount;
            float newMaxLon = theMaxLon + width * theHorizPanAmount;
            if (newMaxLon > 180) {
                newMinLon -= 360;
                newMaxLon -= 360;
            }
            theMinLon = newMinLon;
            theMaxLon = newMaxLon;
            draw();
        } else if ("panUp".equals(evt.get("method"))) {
            float height = theMaxLat - theMinLat;
            float newMinLat = theMinLat + height * theVertPanAmount;
            float newMaxLat = theMaxLat + height * theVertPanAmount;
            if (newMinLat > 90 - height) newMinLat = 90 - height;
            if (newMaxLat > 90) newMaxLat = 90;
            theMinLat = newMinLat;
            theMaxLat = newMaxLat;
            draw();
        } else if ("panDown".equals(evt.get("method"))) {
            float height = theMaxLat - theMinLat;
            float newMinLat = theMinLat - height * theVertPanAmount;
            float newMaxLat = theMaxLat - height * theVertPanAmount;
            if (newMinLat < -90) newMinLat = -90;
            if (newMaxLat < -90 + height) newMaxLat = -90 + height;
            theMinLat = newMinLat;
            theMaxLat = newMaxLat;
            draw();
        } else if ("zoomIn".equals(evt.get("method"))) {
            float cLat = (theMinLat + theMaxLat) / 2;
            float cLon = (theMinLon + theMaxLon) / 2;
            float width = theMaxLon - theMinLon;
            float height = theMaxLat - theMinLat;
            width *= (1 - theZoomAmount);
            height *= (1 - theZoomAmount);
            theMinLon = cLon - width / 2;
            theMaxLon = cLon + width / 2;
            theMinLat = cLat - height / 2;
            theMaxLat = cLat + height / 2;
            draw();
        } else if ("zoomOut".equals(evt.get("method"))) {
            float cLat = (theMinLat + theMaxLat) / 2;
            float cLon = (theMinLon + theMaxLon) / 2;
            float width = theMaxLon - theMinLon;
            float height = theMaxLat - theMinLat;
            width *= (1 + theZoomAmount);
            height *= (1 + theZoomAmount);
            if (width > 360) {
                height *= 360 / width;
                width = 360;
            }
            if (height > 180) {
                width *= 180 / height;
                height = 180;
            }
            theMinLon = cLon - width / 2;
            theMaxLon = cLon + width / 2;
            theMinLat = cLat - height / 2;
            theMaxLat = cLat + height / 2;
            draw();
        } else if ("getPointActions".equals(evt.get("method"))) {
            JSONObject retEvt = new JSONObject();
            retEvt.put("plugin", theName);
            retEvt.put("method", "setPointActions");
            retEvt.put("pointActions", getPointActions(((Number) evt.get("x")).intValue(), ((Number) evt.get("y")).intValue(), ((Number) evt.get("width")).intValue(), ((Number) evt.get("height")).intValue()));
            theSession.postOutgoingEvent(retEvt);
        } else if ("performAction".equals(evt.get("method"))) {
            performAction((String) evt.get("action"), ((Number) evt.get("x")).intValue(), ((Number) evt.get("y")).intValue(), ((Number) evt.get("width")).intValue(), ((Number) evt.get("height")).intValue());
        } else throw new IllegalArgumentException("Unrecognized " + theName + " event: " + evt);
    }
