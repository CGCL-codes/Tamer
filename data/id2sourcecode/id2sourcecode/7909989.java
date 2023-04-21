    public static Map<Shape, String> getShapeToId(final URL url) throws IOException {
        final String CIRCLE = "Circle";
        final Map<Shape, String> shapeToId = new LinkedHashMap<Shape, String>();
        BufferedReader reader = null;
        try {
            final Point offsetPoint = getOffsetPoint(url);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                final String id = (String) CollectionUtils.getFirst(RegularExpressionUtils.getMatches(line, TEXT_REGEX));
                final String shapeString = (String) CollectionUtils.getFirst(RegularExpressionUtils.getMatches(line, NAME_REGEX));
                final String origin = (String) CollectionUtils.getFirst(RegularExpressionUtils.getMatches(line, ORIGIN_REGEX));
                final String size = (String) CollectionUtils.getFirst(RegularExpressionUtils.getMatches(line, SIZE_REGEX));
                final String colour = (String) CollectionUtils.getFirst(RegularExpressionUtils.getMatches(line, COLOUR_REGEX));
                if (origin != null && size != null && id != null) {
                    if (id.matches(INT_REGEX) && colour.equals(STOICHIOMETRY_COLOR)) {
                        continue;
                    }
                    final Point originPoint = getPoint(origin);
                    originPoint.setLocation(originPoint.x - offsetPoint.x, originPoint.y - offsetPoint.y);
                    final Point sizePoint = getPoint(size);
                    Shape shape = null;
                    if (shapeString != null && shapeString.equals(CIRCLE)) {
                        shape = new Ellipse2D.Double(originPoint.x, originPoint.y, sizePoint.x, sizePoint.y);
                    } else {
                        shape = new Rectangle(originPoint.x, originPoint.y, sizePoint.x, sizePoint.y);
                    }
                    shapeToId.put(shape, id);
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return shapeToId;
    }
