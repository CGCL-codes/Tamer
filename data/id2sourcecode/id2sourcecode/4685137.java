    private static List<Geometry> readPointsShapefile(File file, List<Area> theAreas, boolean writeFeatureSource) {
        List<Geometry> geometryList = new ArrayList<Geometry>();
        Map<String, Serializable> connectParameters = new HashMap<String, Serializable>();
        try {
            connectParameters.put("url", file.toURI().toURL());
            connectParameters.put("create spatial index", true);
            DataStore dataStore = DataStoreFinder.getDataStore(connectParameters);
            String[] typeNames = dataStore.getTypeNames();
            String typeName = typeNames[0];
            FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = dataStore.getFeatureSource(typeName);
            if (writeFeatureSource) {
                Area.featureSource = featureSource;
            }
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource.getFeatures();
            FeatureIterator<SimpleFeature> iterator = collection.features();
            try {
                while (iterator.hasNext()) {
                    SimpleFeature feature = iterator.next();
                    Geometry geometry = (Geometry) feature.getDefaultGeometry();
                    geometryList.add(geometry);
                    if (theAreas != null) {
                        Area a = new Area(geometry);
                        a.feature = feature;
                        theAreas.add(a);
                    }
                }
            } finally {
                if (iterator != null) {
                    iterator.close();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return geometryList;
    }
