    public String getPlayerImage() {
        int id = getPlayerSelected().getId();
        String player = "images/team/" + id + ".jpg";
        String noPlayer = "images/team/no_picture.jpg";
        String url = ee.fctwister.index.MainBorder.IMG_URL + player;
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) return player; else return noPlayer;
        } catch (Exception e) {
            e.printStackTrace();
            return noPlayer;
        }
    }
