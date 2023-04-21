    public AddePointDataReader(String request) throws AddeException {
        DataInputStream dataInputStream;
        URLConnection urlc;
        try {
            URL url = new URL(request);
            urlc = url.openConnection();
            dataInputStream = new DataInputStream(new BufferedInputStream(urlc.getInputStream()));
        } catch (AddeURLException ae) {
            throw new AddeException("No datasets found " + ae);
        } catch (Exception e) {
            throw new AddeException("Error opening connection: " + e);
        }
        int numParamBytes;
        if (urlc instanceof AddeURLConnection) {
            numParamBytes = ((AddeURLConnection) urlc).getInitialRecordSize();
        } else {
            try {
                numParamBytes = dataInputStream.readInt();
            } catch (IOException e) {
                throw new AddeException("Error reading data: " + e);
            }
        }
        if (debug) System.out.println("numParamBytes = " + numParamBytes);
        if (numParamBytes == 0) {
            status = -1;
            throw new AddeException("No data found");
        } else {
            byte[] bParamNames = new byte[numParamBytes];
            numParams = numParamBytes / 4;
            params = new String[numParams];
            try {
                dataInputStream.readFully(bParamNames, 0, numParamBytes);
                String sParamNames = new String(bParamNames);
                if (debug) System.out.println(" sParamNames = " + sParamNames);
                for (int i = 0; i < numParams; i++) params[i] = sParamNames.substring(i * 4, (i + 1) * 4).trim();
            } catch (IOException e) {
                status = -1;
                throw new AddeException("Error reading parameters:" + e);
            }
        }
        try {
            int numUnitBytes = dataInputStream.readInt();
            units = new String[numUnitBytes / 4];
            if (debug) System.out.println("numUnitBytes = " + numUnitBytes);
            byte[] bUnitNames = new byte[numUnitBytes];
            dataInputStream.readFully(bUnitNames, 0, numUnitBytes);
            String sUnitNames = new String(bUnitNames);
            if (debug) System.out.println("sUnitNames = " + sUnitNames);
            for (int i = 0; i < numUnitBytes / 4; i++) units[i] = sUnitNames.substring(i * 4, (i + 1) * 4).trim();
        } catch (IOException e) {
            status = -1;
            throw new AddeException("Error reading units:" + e);
        }
        try {
            int numScalingBytes = dataInputStream.readInt();
            if (debug) System.out.println("numScalingBytes = " + numScalingBytes);
            ScalingFactors = new int[(int) (numScalingBytes / 4)];
            for (int i = 0; i < (int) (numScalingBytes / 4); i++) {
                ScalingFactors[i] = dataInputStream.readInt();
            }
        } catch (IOException e) {
            status = -1;
            throw new AddeException("Error reading scaling factors:" + e);
        }
        data = new Vector();
        byte[] bThisUnitName = new byte[4];
        try {
            int numDataBytes = dataInputStream.readInt();
            while (numDataBytes != 0) {
                if (debug) System.out.println(" i, Param, Unit, Value ");
                int[] dataArray = new int[numParams];
                for (int i = 0; i < (int) (numDataBytes / 4); i++) {
                    dataArray[i] = dataInputStream.readInt();
                }
                data.addElement(dataArray);
                numDataBytes = dataInputStream.readInt();
                if (debug) System.out.println("numDataBytes = " + numDataBytes);
            }
        } catch (IOException e) {
            status = -1;
            throw new AddeException("Error reading data:" + e);
        }
    }
