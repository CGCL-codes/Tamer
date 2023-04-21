    private Result registerWithService(String username) {
        secret = UUID.randomUUID().toString();
        String text = "";
        try {
            URL url = new URL("http://cslvm157.csc.calpoly.edu/fieldguideservice/user/register.php?username=" + username + "&secret=" + secret);
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
