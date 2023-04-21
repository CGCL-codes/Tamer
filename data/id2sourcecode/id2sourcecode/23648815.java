    @Override
    protected void processData(String data) {
        try {
            JSONObject contents = new JSONObject(data);
            final TextView name = (TextView) findViewById(R.id.microblogUserName);
            String userName = contents.getString("userName");
            name.setText(userName);
            final ImageView imageView = (ImageView) findViewById(R.id.microblogUserPhoto);
            String photoUrl = contents.getString("userPhotoUrl");
            String imageUrl = photoUrl;
            if ((imageUrl != null) && !imageUrl.equals("")) {
                try {
                    URL url = new URL(Configuration.getDomain() + imageUrl);
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                    Bitmap bm = BitmapFactory.decodeStream(bis);
                    bis.close();
                    imageView.setImageBitmap(bm);
                    Session.user = new User(userName, photoUrl, bm);
                } catch (Exception e) {
                    Log.e(tag, "error: " + e.getMessage());
                }
            }
            JSONArray list = contents.getJSONArray("microblogs");
            Session.currentPortletContents.clear();
            for (int i = 0; i < list.length(); i++) {
                JSONObject entity = list.getJSONObject(i);
                try {
                    Session.currentPortletContents.add(new Microblog(entity.getLong("id"), entity.getString("content"), entity.getString("displayName"), entity.getString("photoUrl"), entity.getString("date")));
                } catch (Exception e) {
                    Log.e(tag, "error: " + e.getMessage());
                }
                JSONArray comments = entity.getJSONArray("comments");
                for (int j = 0; j < comments.length(); j++) {
                    JSONObject comment = comments.getJSONObject(j);
                    try {
                        Session.currentPortletContents.add(new Microblog(comment.getLong("id"), comment.getString("comment"), comment.getString("displayName"), comment.getString("photoUrl"), comment.getString("date"), true));
                    } catch (Exception e) {
                        Log.e(tag, "error: " + e.getMessage());
                    }
                }
            }
            listAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(tag, "error: " + e.getMessage());
        }
    }
