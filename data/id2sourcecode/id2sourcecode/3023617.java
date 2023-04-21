    public static FBPhotoAlbum[] getAlbums(Application application) throws IOException {
        ArrayList<FBPhotoAlbum> rtn = new ArrayList<FBPhotoAlbum>();
        try {
            String queryparms = "access_token=" + URLEncoder.encode(((PossessedApplication) application).getAuthCode(), "UTF-8");
            URL url = new URL("https://graph.facebook.com/me/albums?" + queryparms);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JSONParser p = new JSONParser();
            JSONObject ob = (JSONObject) p.parse(rd);
            if (ob.containsKey("error")) {
                rd.close();
                queryparms = "access_token=" + URLEncoder.encode(((PossessedApplication) application).getAuthCode(true), "UTF-8");
                url = new URL("https://graph.facebook.com/me/albums?" + queryparms);
                conn = url.openConnection();
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                p = new JSONParser();
                ob = (JSONObject) p.parse(rd);
            }
            JSONArray ob2 = (JSONArray) p.parse(ob.get("data").toString());
            for (Object album : ob2.toArray()) {
                JSONObject ob3 = (JSONObject) album;
                FBPhotoAlbum alb = new FBPhotoAlbum();
                alb.setName(ob3.get("name").toString());
                alb.setUrl(ob3.get("link").toString());
                alb.setId(ob3.get("id").toString());
                rtn.add(alb);
            }
            rd.close();
        } catch (IOException err) {
            throw err;
        } catch (Exception err) {
            err.printStackTrace();
        }
        return rtn.toArray(new FBPhotoAlbum[] {});
    }
