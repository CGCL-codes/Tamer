    public ReadTextFile(String request) {
        status = 0;
        statusString = "OK";
        try {
            URL url = new URL(request);
            if (debug) System.out.println("Request: " + request);
            urlc = url.openConnection();
            InputStream is = urlc.getInputStream();
            dis = new DataInputStream(is);
        } catch (AddeURLException ae) {
            status = -1;
            statusString = "No file found";
            String aes = ae.toString();
            if (aes.indexOf(" Accounting ") != -1) {
                statusString = "No accounting data";
                status = -3;
            }
            if (debug) System.out.println("ReadText AF Exception:" + aes);
        } catch (Exception e) {
            status = -2;
            if (debug) System.out.println("ReadText Exception:" + e);
            statusString = "Error opening connection: " + e;
        }
        int numBytes;
        linesOfText = new Vector();
        if (status == 0) {
            try {
                numBytes = ((AddeURLConnection) urlc).getInitialRecordSize();
                if (debug) System.out.println("ReadTextFile: initial numBytes = " + numBytes);
                numBytes = dis.readInt();
                while ((numBytes = dis.readInt()) != 0) {
                    if (debug) System.out.println("ReadTextFile: numBytes = " + numBytes);
                    byte[] data = new byte[numBytes];
                    dis.readFully(data, 0, numBytes);
                    String s = new String(data);
                    if (debug) System.out.println(s);
                    linesOfText.addElement(s);
                }
            } catch (Exception iox) {
                statusString = " " + iox;
                System.out.println(" " + iox);
            }
            if (linesOfText.size() < 1) statusString = "No data read";
            status = linesOfText.size();
        }
    }
