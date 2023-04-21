    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        Message.debug("Starting test program for DataManager class...", Message.LEVEL.TWO);
        Data theData = DataFactory.createData("data1");
        theData.setRecord("d", "1");
        theData.setRecord("g", "2");
        theData.setRecord("c", "3");
        Message.debug("Adding a Data object to the DataManager...", Message.LEVEL.TWO);
        DataManager dm = new DataManager("dm1");
        dm.add("1", theData);
        Message.debug("Name of data manager: " + dm.getName(), Message.LEVEL.TWO);
        Data theData2 = DataFactory.createData("data2");
        theData2.setRecord("name", "Matthew Pearson");
        IOXMLFile dmFile = new IOXMLFile();
        try {
            System.out.print("Enter encryption password:  ");
            System.out.flush();
            dmFile.writeDataManager(dm, "C:\\hello1.xml", KeyManager.readPassword(System.in));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message.debug("Now try to read file....", Message.LEVEL.TWO);
        DataManager dm2 = null;
        try {
            System.out.print("Enter decryption password:  ");
            System.out.flush();
            dm2 = dmFile.readDataManager("C:\\hello1.xml", KeyManager.readPassword(System.in));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dm2 != null) {
            IOUtils.dump(dm2);
        }
        Message.debug("Finished.", Message.LEVEL.TWO);
    }
