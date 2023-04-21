    public static HashMap getPhotos(Application application, FBPhotoAlbum album, String next) {
        ArrayList<FBPhoto> rtn = new ArrayList<FBPhoto>();
        JSONObject paging = null;
        try {
            String queryparms = "access_token=" + URLEncoder.encode(((PossessedApplication) application).getAuthCode(), "UTF-8");
            URL url = null;
            try {
                if (next != null) {
                    url = new URL(next);
                } else {
                    url = new URL("https://graph.facebook.com/" + album.getId() + "/photos?" + queryparms);
                }
            } catch (RuntimeException err) {
                err.printStackTrace();
            }
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JSONParser p = new JSONParser();
            JSONObject ob = (JSONObject) p.parse(rd);
            JSONArray ob2 = (JSONArray) p.parse(ob.get("data").toString());
            if (ob.get("paging") != null) {
                paging = (JSONObject) p.parse(ob.get("paging").toString());
            }
            for (Object photos : ob2.toArray()) {
                JSONObject ob3 = (JSONObject) photos;
                FBPhoto photo = new FBPhoto();
                photo.setSmallurl(ob3.get("picture").toString());
                photo.setBigurl(ob3.get("source").toString());
                photo.setUid(ob3.get("id").toString());
                rtn.add(photo);
            }
            rd.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        HashMap rtn1 = new HashMap();
        rtn1.put("fbphoto", rtn.toArray(new FBPhoto[] {}));
        if (paging != null) rtn1.put("next", paging.get("next"));
        return rtn1;
    }
