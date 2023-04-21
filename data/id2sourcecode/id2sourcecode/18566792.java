    public void createSchema(final FeatureType _featureType) throws IOException {
        if (!isLocal()) {
            throw new IOException("Cannot create FeatureType on remote shapefile");
        }
        clear();
        schema_ = _featureType;
        CoordinateReferenceSystem cs = _featureType.getDefaultGeometry().getCoordinateSystem();
        final long temp = System.currentTimeMillis();
        if (isLocal()) {
            final Class geomType = _featureType.getDefaultGeometry().getType();
            ShapeType shapeType;
            if (Point.class.isAssignableFrom(geomType)) {
                shapeType = ShapeType.POINT;
            } else if (MultiPoint.class.isAssignableFrom(geomType)) {
                shapeType = ShapeType.MULTIPOINT;
            } else if (LineString.class.isAssignableFrom(geomType) || MultiLineString.class.isAssignableFrom(geomType)) {
                shapeType = ShapeType.ARC;
            } else if (Polygon.class.isAssignableFrom(geomType) || MultiPolygon.class.isAssignableFrom(geomType)) {
                shapeType = ShapeType.POLYGON;
            } else {
                return;
            }
            final FileChannel shpChannel = (FileChannel) getWriteChannel(getStorageURL(shpURL_, temp));
            final FileChannel shxChannel = (FileChannel) getWriteChannel(getStorageURL(shxURL_, temp));
            ShapefileWriter writer = null;
            try {
                writer = new ShapefileWriter(shpChannel, shxChannel, readWriteLock_, GISGeometryFactory.INSTANCE);
                final Envelope env = new Envelope(-179, 179, -89, 89);
                Envelope transformedBounds;
                if (cs != null) {
                    try {
                        transformedBounds = JTS.transform(env, CRS.transform(DefaultGeographicCRS.WGS84, cs, true));
                    } catch (final Exception e) {
                        cs = null;
                        transformedBounds = env;
                    }
                } else {
                    transformedBounds = env;
                }
                writer.writeHeaders(transformedBounds, shapeType, 0, 100);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
            final DbaseFileHeader dbfheader = createDbaseHeader(_featureType);
            dbfheader.setNumRecords(0);
            final WritableByteChannel writeChannel = getWriteChannel(getStorageURL(dbfURL_, temp));
            try {
                dbfheader.writeHeader(writeChannel);
            } finally {
                writeChannel.close();
            }
        }
        if (cs != null) {
            final String s = cs.toWKT();
            final FileWriter out = new FileWriter(getStorageFile(prjURL_, temp));
            try {
                out.write(s);
            } finally {
                out.close();
            }
        }
        copyAndDelete(shpURL_, temp);
        copyAndDelete(shxURL_, temp);
        copyAndDelete(dbfURL_, temp);
        if (!prjURL_.getPath().equals("")) {
            try {
                copyAndDelete(prjURL_, temp);
            } catch (final FileNotFoundException e) {
                LOGGER.warning(".prj could not be created.");
            }
        }
    }
