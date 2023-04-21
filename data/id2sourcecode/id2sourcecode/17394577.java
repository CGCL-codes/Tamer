    public static Result download(Context c, String username, int latitudeE6Min, int latitudeE6Max, int longitudeE6Min, int longitudeE6Max) {
        String text = "";
        try {
            URL url = new URL("http://cslvm157.csc.calpoly.edu/fieldguideservice/identification/get.php" + "?username=" + username + "&latmin=" + String.format("%d", latitudeE6Min) + "&latmax=" + String.format("%d", latitudeE6Max) + "&lngmin=" + String.format("%d", longitudeE6Min) + "&lngmax=" + String.format("%d", longitudeE6Max));
            Scanner in = new Scanner(new InputStreamReader(url.openStream())).useDelimiter("\n");
            while (in.hasNext()) {
                text = in.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.SERVER_CONNECTION_FAILED;
        }
        resultNum = 0;
        if (text.equals("")) return Result.OK;
        DBAdapter db = new DBAdapter(c);
        db.open();
        String[] sightings = text.split(";");
        String[] sighting;
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat df = SimpleDateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US);
        for (int i = 0; i < sightings.length; i++) {
            sighting = sightings[i].split("\\|");
            String date = "";
            try {
                date = df.format(dateFormatGmt.parse(sighting[5]));
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            if (db.insertSharedIdentification(sighting[1], Integer.parseInt(sighting[2]), Double.parseDouble(sighting[3]), Double.parseDouble(sighting[4]), date, sighting[0]) != -1) resultNum++;
        }
        db.close();
        return Result.OK;
    }
