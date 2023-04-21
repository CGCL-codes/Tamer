    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.forum_post_item, null);
        }
        ForumPost o = (ForumPost) this.getItem(position);
        if (o != null) {
            TextView title = (TextView) v.findViewById(R.id.forumItemName);
            title.setText(o.getTitle());
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
        return v;
    }
