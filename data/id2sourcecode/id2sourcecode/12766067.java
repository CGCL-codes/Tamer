    public static int findIntersect(IndexedPanel parent, Location location, LocationWidgetComparator comparator) {
        int widgetCount = parent.getWidgetCount();
        if (widgetCount == 0) {
            return 0;
        }
        if (DEBUG) {
            for (int i = 0; i < widgetCount; i++) {
                debugWidgetWithColor(parent, i, "white");
            }
        }
        int low = 0;
        int high = widgetCount;
        while (true) {
            int mid = (low + high) / 2;
            assert mid >= low;
            assert mid < high;
            Widget widget = parent.getWidget(mid);
            WidgetArea midArea = new WidgetArea(widget, null);
            if (mid == low) {
                if (mid == 0) {
                    if (comparator.locationIndicatesIndexFollowingWidget(midArea, location)) {
                        debugWidgetWithColor(parent, high, "green");
                        return high;
                    } else {
                        debugWidgetWithColor(parent, mid, "green");
                        return mid;
                    }
                } else {
                    debugWidgetWithColor(parent, high, "green");
                    return high;
                }
            }
            if (midArea.getBottom() < location.getTop()) {
                debugWidgetWithColor(parent, mid, "blue");
                low = mid;
            } else if (midArea.getTop() > location.getTop()) {
                debugWidgetWithColor(parent, mid, "red");
                high = mid;
            } else if (midArea.getRight() < location.getLeft()) {
                debugWidgetWithColor(parent, mid, "blue");
                low = mid;
            } else if (midArea.getLeft() > location.getLeft()) {
                debugWidgetWithColor(parent, mid, "red");
                high = mid;
            } else {
                if (comparator.locationIndicatesIndexFollowingWidget(midArea, location)) {
                    debugWidgetWithColor(parent, mid + 1, "green");
                    return mid + 1;
                } else {
                    debugWidgetWithColor(parent, mid, "green");
                    return mid;
                }
            }
        }
    }
