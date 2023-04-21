    public void writeNodes() {
        try {
            final SimpleFeatureType TYPE = DataUtilities.createType(this.nodeSchemaName, this.nodeSchemaString);
            FeatureCollection collection = FeatureCollections.newCollection();
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
            for (int row = 0; row < this.ds.getX().getRowCount(); row++) {
                double xcoord = this.ds.getX().getAsDouble(row, 0);
                double ycoord = this.ds.getY().getAsDouble(row, 0);
                String name = this.ds.getAttb().getAsString(row, 0);
                Point point = geometryFactory.createPoint(new Coordinate(xcoord, ycoord));
                featureBuilder.add(point);
                featureBuilder.add(name);
                SimpleFeature feature = featureBuilder.buildFeature(null);
                collection.add(feature);
            }
            DataStoreFactorySpi dataStoreFactory = new ShapefileDataStoreFactory();
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put("url", nodeFile.toURI().toURL());
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
            System.out.println("Cannot create features!\n" + e.getMessage());
        }
    }
