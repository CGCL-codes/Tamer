    public static String[] fromMany(ArrayList<String> ips) {
        URL url;
        String[] out = new String[ips.size()];
        Arrays.fill(out, "(NONE)");
        String IPString = "";
        for (String x : ips) IPString += x + ";";
        try {
            url = new URL(dbAddr + IPString);
            URLConnection urlConn = url.openConnection();
            urlConn.connect();
            Scanner inc = new Scanner(urlConn.getInputStream());
            String tmpString = inc.nextLine();
            String raw[] = (tmpString.replace(";;", "; ;")).split(";");
            for (int i = 0; i < raw.length; i++) {
                if (!raw[i].trim().equalsIgnoreCase("")) out[i] = raw[i];
            }
            return out;
        } catch (MalformedURLException e) {
            System.err.println("Error getting Location");
        } catch (IOException e) {
            System.err.println("Error getting Location");
        }
        return null;
    }
