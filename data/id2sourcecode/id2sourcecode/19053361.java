    public static String writeDefaultShapefile() {
        GeoDataGeneralizedStates stateData = new GeoDataGeneralizedStates();
        String fileName = System.getProperty("user.home");
        String inputFileName = fileName + "/states48.shp";
        File newDir = new File(fileName);
        newDir.mkdir();
        MapGenFile.writeShapefile(stateData.getDataForApps().getGeneralPathData(), inputFileName);
        try {
            InputStream dbfStream = GeoDataGeneralizedStates.class.getResourceAsStream("resources/states48.dbf");
            ReadableByteChannel dChan = Channels.newChannel(dbfStream);
            DbaseFileReader dBaseReader = new DbaseFileReader(dChan, true);
            DbaseFileHeader dBaseHeader = dBaseReader.getHeader();
            WritableByteChannel out = new FileOutputStream(fileName + "/states48.dbf").getChannel();
            DbaseFileWriter dBase = new DbaseFileWriter(dBaseHeader, out);
            if (logger.isLoggable(Level.INFO)) {
                logger.info("dBase length = " + dBaseHeader.getNumRecords());
                logger.info("dbasewriter " + dBase.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return inputFileName;
    }
