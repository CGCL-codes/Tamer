    public static void main(String[] args) throws Exception {
        if (args.length < 11) {
            System.out.println("USAGE: ContextualImport shapeFilePath reprojectedShapeFileDestination dbJdbcUrl dbUsername dbPassword geoserverUsername geoserverPassword layerName layerDescription fieldsSid fieldsSname [fieldsSdesc]");
            return;
        }
        System.out.println("Beginning contextual load");
        String shapeFilePath = args[0];
        String reprojectedShapeFileDestination = args[1];
        String dbJdbcUrl = args[2];
        String dbUsername = args[3];
        String dbPassword = args[4];
        String geoserverUsername = args[5];
        String geoserverPassword = args[6];
        String layerName = args[7];
        String layerDescription = args[8];
        String fieldsSid = args[9];
        String fieldsSname = args[10];
        String fieldsSdesc = null;
        if (args.length >= 12) {
            fieldsSdesc = args[11];
        }
        System.out.println("Checking supplied paths for shape file and destination directory");
        File shapeFile = new File(shapeFilePath);
        if (!shapeFile.exists() || !shapeFile.isFile()) {
            throw new RuntimeException("Shape file " + shapeFilePath + " does not exist or is a directory");
        }
        File reprojectedShapeFileDestinationDir = new File(reprojectedShapeFileDestination);
        if (!reprojectedShapeFileDestinationDir.exists() || !reprojectedShapeFileDestinationDir.isDirectory()) {
            throw new RuntimeException("Directory " + reprojectedShapeFileDestinationDir + " does not exist or is not a directory");
        }
        System.out.println("Connecting to database");
        Class.forName("org.postgresql.Driver");
        Properties props = new Properties();
        props.setProperty("user", dbUsername);
        props.setProperty("password", dbPassword);
        Connection conn = DriverManager.getConnection(dbJdbcUrl, props);
        conn.setAutoCommit(false);
        try {
            System.out.println("Generating ID for new layer...");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT MAX(id) from layers");
            rs.next();
            int id = 1;
            String idAsString = rs.getString(1);
            if (idAsString != null) {
                id = Integer.parseInt(idAsString);
                id++;
            }
            System.out.println("Reprojecting to WGS 84...");
            File reprojectedShapeFile = new File(reprojectedShapeFileDestinationDir, shapeFile.getName());
            Process procOgr2Ogr = Runtime.getRuntime().exec(new String[] { "ogr2ogr", "-t_srs", "EPSG:4326", reprojectedShapeFile.getAbsolutePath(), shapeFile.getAbsolutePath() });
            int ogr2ogrReturnVal = procOgr2Ogr.waitFor();
            if (ogr2ogrReturnVal != 0) {
                String ogr2ogrErrorOutput = IOUtils.toString(procOgr2Ogr.getErrorStream());
                throw new RuntimeException("ogr2ogr failed: " + ogr2ogrErrorOutput);
            }
            System.out.println("Getting extents...");
            double minLatitude;
            double maxLatitude;
            double minLongitude;
            double maxLongitude;
            Process procOgrinfo = Runtime.getRuntime().exec(new String[] { "ogrinfo", "-so", reprojectedShapeFile.getAbsolutePath(), layerName });
            String ogrinfoOutput = IOUtils.toString(procOgrinfo.getInputStream());
            int ogrinfoReturnVal = procOgrinfo.waitFor();
            if (ogrinfoReturnVal != 0) {
                String ogrinfoErrorOutput = IOUtils.toString(procOgrinfo.getErrorStream());
                throw new RuntimeException("ogrinfo failed: " + ogrinfoErrorOutput);
            }
            Pattern p = Pattern.compile("^Extent: \\((.+), (.+)\\) \\- \\((.+), (.+)\\)$", Pattern.MULTILINE);
            Matcher m = p.matcher(ogrinfoOutput);
            if (m.find()) {
                if (m.groupCount() == 4) {
                    minLongitude = Double.parseDouble(m.group(1));
                    minLatitude = Double.parseDouble(m.group(2));
                    maxLongitude = Double.parseDouble(m.group(3));
                    maxLatitude = Double.parseDouble(m.group(4));
                } else {
                    throw new RuntimeException("error reading extents from ogrinfo: " + ogrinfoOutput);
                }
            } else {
                throw new RuntimeException("error reading extents from ogrinfo: " + ogrinfoOutput);
            }
            System.out.println("Dropping old geometry table if present");
            conn.prepareStatement(String.format("DROP TABLE IF EXISTS \"%s\"", id)).execute();
            System.out.println("Converting shape file for insertion in database...");
            Process procShp2Pgsql = Runtime.getRuntime().exec(new String[] { "shp2pgsql", "-I", "-s", "4326", shapeFile.getAbsolutePath(), Integer.toString(id) });
            String shp2pgsqlOutput = IOUtils.toString(procShp2Pgsql.getInputStream());
            shp2pgsqlOutput = shp2pgsqlOutput.replace("BEGIN;", "");
            shp2pgsqlOutput = shp2pgsqlOutput.replace("COMMIT;", "");
            int shp2pgsqlReturnVal = procShp2Pgsql.waitFor();
            if (shp2pgsqlReturnVal != 0) {
                String shp2pgsqlErrorOutput = IOUtils.toString(procShp2Pgsql.getErrorStream());
                throw new RuntimeException("shp2pgsql failed: " + shp2pgsqlErrorOutput);
            }
            System.out.println("Writing shape file to database...");
            conn.prepareStatement(shp2pgsqlOutput).execute();
            System.out.println("Checking row count of shape geometry table");
            rs = st.executeQuery(String.format("SELECT COUNT(*) from \"%s\"", id));
            rs.next();
            int numRows;
            String numRowsAsString = rs.getString(1);
            if (numRowsAsString != null) {
                numRows = Integer.parseInt(numRowsAsString);
                if (numRows == 1) {
                    System.out.println("Shape geometry table has only 1 row - creating ala_name column for use with fields and objects");
                    fieldsSid = ALA_NAME_COLUMN_NAME;
                    fieldsSname = ALA_NAME_COLUMN_NAME;
                    fieldsSdesc = null;
                    PreparedStatement alaNameColumnCreationStatement = conn.prepareStatement(String.format("ALTER TABLE \"%s\" ADD COLUMN %s text;", id, ALA_NAME_COLUMN_NAME));
                    alaNameColumnCreationStatement.execute();
                    PreparedStatement alaNameInsertionStatement = conn.prepareStatement(String.format("UPDATE \"%s\" SET %s = '%s' where true;", id, ALA_NAME_COLUMN_NAME, layerDescription));
                    alaNameInsertionStatement.execute();
                }
            }
            String displayPath = MessageFormat.format(GEOSERVER_QUERY_TEMPLATE, layerName);
            System.out.println("Creating layers table entry...");
            PreparedStatement createLayersStatement = createLayersInsert(conn, id, layerDescription, reprojectedShapeFileDestinationDir.getAbsolutePath(), layerName, displayPath, minLatitude, minLongitude, maxLatitude, maxLongitude);
            createLayersStatement.execute();
            System.out.println("Creating fields table entry...");
            PreparedStatement createFieldsStatement = createFieldsInsert(conn, id, layerName, layerDescription, fieldsSid, fieldsSname, fieldsSdesc);
            createFieldsStatement.execute();
            System.out.println("Creating objects table entries...");
            PreparedStatement createObjectsStatement = createObjectsInsert(conn, id, fieldsSid, fieldsSname, fieldsSdesc);
            createObjectsStatement.execute();
            System.out.println("Generating object names...");
            PreparedStatement createObjectNamesStatement = createObjectNameGenerationStatement(conn);
            createObjectNamesStatement.execute();
            System.out.println("Generating object bounding boxes and areas...");
            PreparedStatement createBBoxesAndAreaStatement = createGenerateObjectsBBoxAndAreaStatement(conn);
            createBBoxesAndAreaStatement.execute();
            conn.commit();
            System.out.println("Creating layer in geoserver...");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.getCredentialsProvider().setCredentials(new AuthScope("localhost", 8082), new UsernamePasswordCredentials(geoserverUsername, geoserverPassword));
            HttpPost post = new HttpPost("http://localhost:8082/geoserver/rest/workspaces/ALA/datastores/LayersDB/featuretypes");
            post.setHeader("Content-type", "text/xml");
            post.setEntity(new StringEntity(String.format("<featureType><name>%s</name><nativeName>%s</nativeName><title>%s</title></featureType>", layerName, id, layerDescription)));
            HttpResponse response = httpClient.execute(post);
            if (response.getStatusLine().getStatusCode() != 201) {
                throw new RuntimeException("Error creating layer in geoserver: " + response.toString());
            }
            EntityUtils.consume(response.getEntity());
            HttpPut put = new HttpPut(String.format("http://localhost:8082/geoserver/rest/layers/ALA:%s", layerName));
            put.setHeader("Content-type", "text/xml");
            put.setEntity(new StringEntity("<layer><defaultStyle><name>generic_border</name></defaultStyle><enabled>true</enabled></layer>"));
            HttpResponse response2 = httpClient.execute(put);
            if (response2.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Error setting layer border in geoserver: " + response2.toString());
            }
            EntityUtils.consume(response2.getEntity());
        } catch (Exception ex) {
            ex.printStackTrace();
            conn.rollback();
        }
    }
