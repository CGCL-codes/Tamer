    public boolean dlTransactionTest(JpxiServiceDelegate theDelegate, UID theUID) {
        JpxiTransactionAck myAck = null;
        File myFile = new File(getDlDoc());
        File[] myFileList = null;
        boolean isDirectory = false;
        int nbFiles = 1;
        if (myFile.isDirectory()) {
            isDirectory = true;
            myFileList = myFile.listFiles();
        }
        if (isDirectory) {
            nbFiles = myFileList.length;
            setTotalPages(nbFiles);
        } else setTotalPages(1);
        setDocSize(0);
        int myNbFullRecord = 0;
        int myRestOfRecord = 0;
        int mySizeOfST33Array = 0;
        setTotalRecords(0);
        for (int i = 0; i < nbFiles; i++) {
            myNbFullRecord = 0;
            myRestOfRecord = 0;
            mySizeOfST33Array = 0;
            if (isDirectory) setDocSize((int) myFileList[i].length() + getDocSize()); else setDocSize((int) myFile.length());
            myNbFullRecord = getDocSize() / DATA_RECORD_LENGTH;
            myRestOfRecord = getDocSize() % DATA_RECORD_LENGTH;
            mySizeOfST33Array = myNbFullRecord * (DATA_RECORD_LENGTH + ST33_HEADER_LENGTH);
            if (myRestOfRecord != 0) {
                mySizeOfST33Array += ST33_HEADER_LENGTH + myRestOfRecord;
                setTotalRecords(myNbFullRecord + 1 + getTotalRecords());
            } else setTotalRecords(myNbFullRecord + getTotalRecords());
        }
        setTotalPages(nbFiles);
        String myXml = generateXmlDescr();
        JpxiDmsDbRec myDMS = new JpxiDmsDbRec(getDlDocId(), 0, new Timestamp(System.currentTimeMillis()), myXml);
        try {
            myAck = theDelegate.newDLTransaction(theUID, myDMS, getElectronicFormat(), getElectronicFormat(), "DEFAULT", getPublicationDate(), getCopyright());
        } catch (Exception e) {
            return (false);
        }
        for (int i = 0; i < nbFiles; i++) {
            int j = 0;
            int nbByteDone = 0;
            int nbByteToDo;
            int currentSize;
            if (isDirectory) {
                nbByteToDo = (int) myFileList[i].length();
                currentSize = nbByteToDo;
            } else {
                nbByteToDo = (int) myFile.length();
                currentSize = nbByteToDo;
            }
            RandomAccessFile myRandom = null;
            byte[] theData = new byte[nbByteToDo];
            try {
                if (isDirectory) myRandom = new RandomAccessFile(myFileList[i], "rw"); else myRandom = new RandomAccessFile(myFile, "rw");
            } catch (FileNotFoundException e2) {
                return (false);
            }
            try {
                myRandom.read(theData);
            } catch (IOException e1) {
                return (false);
            }
            while (nbByteToDo >= 20000) {
                byte[] myTempdata = new byte[20000];
                System.arraycopy(theData, j * 20000, myTempdata, 0, 20000);
                try {
                    theDelegate.write(myAck.getTransactionUID(), myTempdata);
                } catch (JpxiException e3) {
                    closeDlTrans(theDelegate, myAck.getTransactionUID());
                    return (false);
                } catch (ServiceNotAvailableException e) {
                    closeDlTrans(theDelegate, myAck.getTransactionUID());
                    return (false);
                }
                j++;
                nbByteDone = j * 20000;
                nbByteToDo = currentSize - nbByteDone;
            }
            if (currentSize > nbByteDone) {
                byte[] myTempdata = new byte[nbByteToDo];
                System.arraycopy(theData, j * 20000, myTempdata, 0, nbByteToDo);
                try {
                    theDelegate.write(myAck.getTransactionUID(), myTempdata);
                } catch (JpxiException e3) {
                    closeDlTrans(theDelegate, myAck.getTransactionUID());
                    return (false);
                } catch (ServiceNotAvailableException e) {
                    closeDlTrans(theDelegate, myAck.getTransactionUID());
                    return (false);
                }
            }
        }
        closeDlTrans(theDelegate, myAck.getTransactionUID());
        return (true);
    }
