    private void getForumPostView(View v, ForumPost o) {
        if (o != null) {
            TextView title = (TextView) v.findViewById(R.id.forumItemName);
            StringBuilder text = new StringBuilder();
            text.append(o.getTitle());
            if (o.getPosts() > 0) text.append(" (").append(o.getPosts()).append(")");
            title.setText(text.toString());
            final ImageView imageView = (ImageView) v.findViewById(R.id.forumPhoto);
            String imageUrl = o.getPhotoUrl();
            if ((imageUrl != null) && !imageUrl.equals("")) {
                try {
                    if (!imageUrl.startsWith("http")) imageUrl = Configuration.getDomain() + imageUrl;
                    URL url = new URL(imageUrl);
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                    Bitmap bm = BitmapFactory.decodeStream(bis);
                    bis.close();
                    if (bm != null) imageView.setImageBitmap(bm);
                } catch (Exception e) {
                }
            }
        }
    }
