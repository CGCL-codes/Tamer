    public CanbusPoll(CanbusVariables canbusVariables, ArrayList<String> rxBuffer, ArrayList<String> txBuffer, boolean sampledata, boolean datalogging) {
        this.canbusVariables = canbusVariables;
        this.rxBuffer = rxBuffer;
        this.txBuffer = txBuffer;
        defaultPort = "";
        this.sampledata = sampledata;
        String osname = System.getProperty("os.name", "").toLowerCase();
        if (osname.startsWith("windows")) {
            defaultPort = "";
        } else if (osname.startsWith("linux")) {
            defaultPort = "/dev/ttyUSB0";
        } else if (osname.startsWith("mac")) {
            defaultPort = "";
        } else {
            System.out.println("Sorry, your operating system is not supported");
            System.exit(1);
        }
        if (sampledata == true) defaultPort = "SAMPLEDATA";
        if (datalogging == true) {
            try {
                zipLog = new ZipOutputStream(new FileOutputStream("log.zip"));
                zipLog.putNextEntry(new ZipEntry("log.txt"));
            } catch (IOException io) {
            }
            this.datalogging = datalogging;
        }
    }
