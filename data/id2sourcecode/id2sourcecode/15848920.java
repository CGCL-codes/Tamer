    public static void substitute(File demoObject) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(demoObject));
            String tempFile = demoObject.toString() + "-temp";
            OutputStream os = new FileOutputStream(new File(tempFile));
            OutputStreamWriter out = new OutputStreamWriter(os, "UTF-8");
            String nextLine = "";
            String newUrlStart;
            if (toProtocol.equalsIgnoreCase("http") && ((toPortNum.equals("")) || (toPortNum.equals("80")))) {
                newUrlStart = "http://" + toHostName + "/";
            } else if (toProtocol.equalsIgnoreCase("https") && ((toPortNum.equals("")) || (toPortNum.equals("443")))) {
                newUrlStart = "https://" + toHostName + "/";
            } else {
                newUrlStart = toProtocol + "://" + toHostName + ":" + toPortNum + "/";
            }
            String a = fromProtocol + "://" + fromHostName;
            String fromURLStartNoPort = a + "/";
            String fromURLStartPort80 = a + ":80" + "/";
            String fromURLStartPort443 = a + ":443" + "/";
            String fromURLStartWithPort = a + ":" + fromPortNum + "/";
            if (fromProtocol.equalsIgnoreCase("http") && (fromPortNum.equals("") || fromPortNum.equals("80"))) {
                System.out.println("searching for " + fromURLStartNoPort);
                System.out.println("searching for " + fromURLStartPort80);
                System.out.println("replacing with " + newUrlStart);
            } else if (fromProtocol.equalsIgnoreCase("https") && (fromPortNum.equals("") || fromPortNum.equals("443"))) {
                System.out.println("searching for " + fromURLStartNoPort);
                System.out.println("searching for " + fromURLStartPort443);
                System.out.println("replacing with " + newUrlStart);
            } else {
                System.out.println("searching for " + fromURLStartWithPort);
                System.out.println("replacing with " + newUrlStart);
            }
            while (nextLine != null) {
                nextLine = in.readLine();
                if (nextLine != null) {
                    if (fromProtocol.equalsIgnoreCase("http") && (fromPortNum.equals("") || fromPortNum.equals("80"))) {
                        nextLine = nextLine.replaceAll(fromURLStartNoPort, newUrlStart);
                        nextLine = nextLine.replaceAll(fromURLStartPort80, newUrlStart);
                    } else if (fromProtocol.equalsIgnoreCase("https") && (fromPortNum.equals("") || fromPortNum.equals("443"))) {
                        nextLine = nextLine.replaceAll(fromURLStartNoPort, newUrlStart);
                        nextLine = nextLine.replaceAll(fromURLStartPort443, newUrlStart);
                    } else {
                        nextLine = nextLine.replaceAll(fromURLStartWithPort, newUrlStart);
                    }
                    out.write(nextLine + "\n");
                }
            }
            in.close();
            out.close();
            if (demoObject.delete()) {
                File file = new File(tempFile);
                if (!file.renameTo(demoObject)) {
                    System.out.println("ERROR: unable to rename file: " + demoObject);
                } else {
                    System.out.println("Replaced File: " + demoObject);
                }
            } else {
                System.out.println("ERROR: Unable to delete file: " + demoObject);
            }
        } catch (IOException ioe) {
            System.out.println("IO ERROR: " + ioe.getMessage());
        }
    }
