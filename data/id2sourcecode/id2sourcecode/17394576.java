    public static Result share(Identification mIdentification, String username, String secret) {
        String text = "";
        java.util.Date d = mIdentification.getDate();
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            URL url = new URL("http://cslvm157.csc.calpoly.edu/fieldguideservice/identification/save.php" + "?username=" + username + "&secret=" + secret + "&plantid=" + mIdentification.plantId + "&lat=" + String.format("%.0f", mIdentification.lat) + "&lng=" + String.format("%.0f", mIdentification.lng) + "&date=" + URLEncoder.encode(dateFormatGmt.format(d)));
            Scanner in = new Scanner(new InputStreamReader(url.openStream())).useDelimiter("\n");
            while (in.hasNext()) {
                text = in.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.SERVER_CONNECTION_FAILED;
        }
        if (text.equals("Okay")) return Result.OK; else return Result.SERVER_REJECT;
    }
