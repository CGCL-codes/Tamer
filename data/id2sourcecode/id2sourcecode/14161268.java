    public static void getConnection(String query, String fileName) throws Exception {
        ibmUrl = new URL(query);
        urlCon = ibmUrl.openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
        StringBuffer sb = new StringBuffer();
        File f = new File(fileName);
        BufferedWriter bwr = new BufferedWriter(new FileWriter(f));
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
            bwr.write(line + "\n");
        }
        rd.close();
        bwr.close();
    }
