    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.search_item, null);
        }
        try {
            if (this.getCount() > position) {
                Object o = this.getItem(position);
                if (o != null) {
                    if (o instanceof User) {
                        User user = (User) o;
                        TextView title = (TextView) v.findViewById(R.id.searchItemName);
                        title.setText(user.getName());
                        final ImageView imageView = (ImageView) v.findViewById(R.id.searchPhoto);
                        String imageUrl = user.getPhotoUrl();
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
                    } else if (o instanceof SearchEntity) {
                        SearchEntity item = (SearchEntity) o;
                        TextView title = (TextView) v.findViewById(R.id.searchItemName);
                        title.setText(item.getType() + ": " + item.getName());
                        final ImageView imageView = (ImageView) v.findViewById(R.id.searchPhoto);
                        String imageUrl = item.getPhotoUrl();
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
            }
        } catch (Exception e) {
        }
        return v;
    }
