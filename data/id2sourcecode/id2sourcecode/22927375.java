    @Override
    protected void processData(String data) {
        Session.currentEntity = null;
        try {
            JSONObject content = new JSONObject(data);
            JSONObject user = content.getJSONObject("user");
            this.setTitle(user.getString("name"));
            final TextView name = (TextView) findViewById(R.id.userName);
            name.setText(user.getString("url"));
            final ImageView imageView = (ImageView) findViewById(R.id.userImageView);
            String imageUrl = user.getString("photoUrl");
            if ((imageUrl != null) && !imageUrl.equals("")) {
                URL url = new URL(Configuration.getDomain() + imageUrl);
                URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(bis);
                bis.close();
                imageView.setImageBitmap(bm);
            }
        } catch (Exception e) {
            Log.i(tag, e.getMessage());
        }
    }
