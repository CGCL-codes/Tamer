    public void writeEdges() {
        try {
            final SimpleFeatureType TYPE = DataUtilities.createType(this.edgeSchemaName, this.edgeSchemaString);
            FeatureCollection collection = FeatureCollections.newCollection();
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
            for (int row = 0; row < this.ds.getX().getRowCount(); row++) {
                double fromX = this.ds.getX().getAsDouble(row, 0);
                double fromY = this.ds.getY().getAsDouble(row, 0);
                for (int col = 0; col < ds.getAdj().getColumnCount(); col++) {
                    if (ds.getAdj().getAsDouble(row, col) >= 0) {
                        double toX = this.ds.getX().getAsDouble(col, 0);
                        double toY = this.ds.getY().getAsDouble(col, 0);
                        System.out.println(fromX + "," + fromY + " -> " + toX + "," + toY);
                        LineString line = geometryFactory.createLineString(new Coordinate[] { new Coordinate(fromX, fromY), new Coordinate(toX, toY) });
                        featureBuilder.add(line);
                        featureBuilder.add(ds.getAttb().getRowLabel(row));
                        featureBuilder.add(ds.getAttb().getRowLabel(col));
                        SimpleFeature feature = featureBuilder.buildFeature(null);
                        collection.add(feature);
                    }
                }
            }
            DataStoreFactorySpi dataStoreFactory = new ShapefileDataStoreFactory();
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put("url", edgeFile.toURI().toURL());
            params.put("create spatial index", Boolean.TRUE);
            ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
            newDataStore.createSchema(TYPE);
            newDataStore.forceSchemaCRS(DefaultGeographicCRS.WGS84);
            Transaction transaction = new DefaultTransaction("create");
            String typeName = newDataStore.getTypeNames()[0];
            FeatureSource featureSource = newDataStore.getFeatureSource(typeName);
            if (featureSource instanceof FeatureStore) {
                FeatureStore featureStore = (FeatureStore) featureSource;
                featureStore.setTransaction(transaction);
                try {
                    featureStore.addFeatures(collection);
                    transaction.commit();
                } catch (Exception problem) {
                    problem.printStackTrace();
                    transaction.rollback();
                } finally {
                    transaction.close();
                }
            } else {
                System.out.println(typeName + " does not support read/write access");
            }
        } catch (Exception e) {
            System.out.println("Cannot create edge features:\n" + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
