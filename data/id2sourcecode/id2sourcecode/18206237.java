    public static String[] getAppList(int[] availServices) {
        try {
            String strServices = "services=0";
            for (int i = 0; i < availServices.length; i++) {
                strServices += "+" + availServices[i];
            }
            URL url = new URL(SERVER_URL);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            Performance.printEvent("HTTPSend services, size: [" + strServices.length() + "]");
            writer.write(strServices);
            writer.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String strLine;
            List services = new ArrayList();
            while ((strLine = reader.readLine()) != null) {
                Performance.printEvent("HTTPRecv services, size: [" + strLine.length() + "]");
                try {
                    services.add(new Service(strLine));
                } catch (Exception e) {
                    System.err.println("Could not parse service line: \"" + strLine + "\"");
                }
            }
            writer.close();
            reader.close();
            String[] strReturn = new String[services.size()];
            for (int i = 0; i < strReturn.length; i++) {
                strReturn[i] = ((Service) services.get(i)).getName();
            }
            return strReturn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
