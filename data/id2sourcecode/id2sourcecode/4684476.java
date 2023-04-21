    public static void main(String args[]) throws Exception {
        dataList = new ArrayList<IpCountryRange>();
        String[] rirs = { LACNIC, APNIC, RIPE, ARIN, AFRINIC };
        for (int i = 0; i < rirs.length; i++) {
            System.out.println("Loading " + rirs[i]);
            URL url = new URL(rirs[i]);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            readData(inputStream);
            System.out.println("Total data read: " + dataList.size());
            inputStream.close();
        }
        Collections.sort(dataList);
        System.out.println("before size: " + dataList.size());
        consolidateList();
        System.out.println("after size: " + dataList.size());
        writeToOutputFile("ip2country.csv");
    }
