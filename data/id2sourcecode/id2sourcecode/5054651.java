    public static void main(String args[]) {
        System.out.println("AusStage RdfExport - Export AusStage data into a variety of RDF related formats");
        System.out.println("Version: " + VERSION + " Build Date: " + BUILD_DATE);
        System.out.println("More Info: " + INFO_URL + "\n");
        java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
        java.util.Date startDate = calendar.getTime();
        System.out.println("INFO: Process Started: " + DateUtils.getCurrentDateAndTime());
        CommandLineParser parser = new CommandLineParser(args);
        String taskType = parser.getValue("task");
        String propsPath = parser.getValue("properties");
        String dataFormat = parser.getValue("format");
        String output = parser.getValue("output");
        if (InputUtils.isValid(taskType, TASK_TYPES) == false || InputUtils.isValid(propsPath) == false) {
            System.err.println("ERROR: the following parameters are expected");
            System.err.println("-task   the type of task to undertake");
            System.err.println("-properties the location of the properties file");
            System.err.println("-format     (optional) the data format used in an export task");
            System.err.println("-output     (optional) the output file to create for an export task");
            System.err.println("\nValid task types are:");
            System.err.println(InputUtils.arrayToString(TASK_TYPES));
            System.err.println("\nValid data formats are:");
            System.err.println(InputUtils.arrayToString(DATA_FORMATS));
            System.exit(-1);
        }
        if (taskType.equals("export-network-data")) {
            if (InputUtils.isValid(dataFormat) == false) {
                System.out.println("INFO: No data format specified. Using 'RDF/XML' by default");
                dataFormat = "RDF/XML";
            } else {
                if (InputUtils.isValid(dataFormat, DATA_FORMATS) == false) {
                    System.err.println("ERROR: the specified data format '" + dataFormat + "' is invalid");
                    System.err.println("Valid data formats are:");
                    System.err.println(InputUtils.arrayToString(DATA_FORMATS));
                    System.exit(-1);
                }
            }
        }
        boolean status = false;
        PropertiesManager properties = new PropertiesManager();
        if (properties.loadFile(propsPath) == false) {
            System.err.println("ERROR: unable to open the specified properties file");
            System.exit(-1);
        }
        if (taskType.equals("build-network-data") == true) {
            DbManager database = new DbManager(properties.getProperty("db-connection-string"));
            System.out.println("INFO: Connecting to the database...");
            if (database.connect() == false) {
                System.err.println("ERROR: a connection to the database could not be made");
                System.exit(-1);
            } else {
                System.out.println("INFO: Connection to the database established");
            }
            BuildNetworkData task = new BuildNetworkData(database, properties);
            status = task.doReset();
            if (status == false) {
                System.err.println("ERROR: A fatal error has occured, see previous error message for details");
                System.exit(-1);
            }
            status = task.doTask();
        } else if (taskType.equals("export-network-data")) {
            ExportNetworkData task = new ExportNetworkData(properties);
            if (FileUtils.doesFileExist(output) == false) {
                File outputFile = new File(output);
                status = task.doTask(dataFormat, outputFile);
            } else {
                System.err.println("ERROR: Output file already exists, refusing to delete / overwrite");
                System.exit(-1);
            }
        }
        if (status == false) {
            System.err.println("ERROR: An error has occured, see previous messages for details");
            System.exit(-1);
        } else {
            System.out.println("INFO: Task completed successfully");
            System.out.println("INFO: Process Finished: " + DateUtils.getCurrentDateAndTime());
            calendar = new java.util.GregorianCalendar();
            java.util.Date finishDate = calendar.getTime();
            long startTime = startDate.getTime();
            long finishTime = finishDate.getTime();
            float minuteConstant = 60000;
            float timeDifference = finishTime - startTime;
            timeDifference = timeDifference / minuteConstant;
            if (timeDifference > 1) {
                System.out.format("INFO: Elapsed Time: %.2f minutes%n", timeDifference);
            } else {
                timeDifference = timeDifference * 60;
                System.out.format("INFO: Elapsed Time: %.2f seconds%n", timeDifference);
            }
            System.exit(0);
        }
    }
