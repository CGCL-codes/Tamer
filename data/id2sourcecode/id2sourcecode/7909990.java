    private static Point getOffsetPoint(final URL url) throws IOException {
        final int BORDER_HEIGHT = 2;
        final int BORDER_WIDTH = 3;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                final String origin = (String) CollectionUtils.getFirst(RegularExpressionUtils.getMatches(line, TRUE_ORIGIN_REGEX));
                if (origin != null) {
                    final Point offsetPoint = getPoint(origin);
                    offsetPoint.setLocation(offsetPoint.x - BORDER_WIDTH, offsetPoint.y - BORDER_HEIGHT);
                    return offsetPoint;
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return new Point();
    }
