    public ArrayList getGridDirectory(String request) throws AddeURLException {
        URL url;
        gridHeaders = new ArrayList();
        fileHeaders = new ArrayList();
        try {
            url = new URL(request);
            urlc = url.openConnection();
            InputStream is = urlc.getInputStream();
            dis = new DataInputStream(new BufferedInputStream(is));
        } catch (AddeURLException ae) {
            throw new AddeURLException("Dataset not found: " + ae);
        } catch (Exception e) {
            throw new AddeURLException("Error opening connection: " + e);
        }
        int numBytes = ((AddeURLConnection) urlc).getInitialRecordSize();
        if (numBytes == 0) {
            status = -1;
            throw new AddeURLException("No datasets found");
        }
        try {
            int check;
            byte[] header = new byte[256];
            while (numBytes == 4) {
                check = dis.readInt();
                if (check != HEARTBEAT) {
                    System.out.println("problem...not heartbeat = " + check);
                }
                numBytes = dis.readInt();
            }
            while ((check = dis.readInt()) == 0) {
                dis.readFully(header, 0, 256);
                String head = new String(header, 0, 32);
                fileHeaders.add(head);
                int check2;
                while ((check2 = dis.readInt()) == 0) {
                    dis.readFully(header, 0, 256);
                    String name = new String(header, 24, 4);
                    McIDASGridDirectory mg = new McIDASGridDirectory(header);
                    gridHeaders.add(mg);
                }
            }
        } catch (Exception re) {
            System.out.println(re);
        }
        return gridHeaders;
    }
