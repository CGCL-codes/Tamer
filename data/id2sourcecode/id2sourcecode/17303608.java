    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.microblog_item, null);
        }
        try {
            if (this.getCount() > position) {
                final Microblog o = (Microblog) this.getItem(position);
                if (o != null) {
                    TextView title = (TextView) v.findViewById(R.id.microblogItemName);
                    StringBuilder text = new StringBuilder();
                    text.append(o.getContent());
                    title.setText(text.toString());
                    TextView date = (TextView) v.findViewById(R.id.microblogItemDate);
                    date.setText(o.getDate());
                    final ImageView action = (ImageView) v.findViewById(R.id.microblogItemAction);
                    if (o.isComment()) {
                        action.setVisibility(View.INVISIBLE);
                    } else {
                        action.setVisibility(View.VISIBLE);
                        action.setOnClickListener(new OnClickListener() {

                            public void onClick(View v) {
                                try {
                                    Intent myIntent = new Intent(v.getContext(), CommentActivity.class);
                                    myIntent.putExtra("objectId", o.getId());
                                    myIntent.putExtra("objectType", _OBJECT_TYPE_MICROBLOG);
                                    myIntent.putExtra("parentId", o.isComment() ? o.getId() : 0);
                                    myIntent.putExtra("topic", o.getContent());
                                    v.getContext().startActivity(myIntent);
                                    Log.i(tag, "action onClick complete.");
                                } catch (Exception e) {
                                    Log.e(tag, "error: " + e.getMessage());
                                }
                            }
                        });
                    }
                    final ImageView imageView = (ImageView) v.findViewById(R.id.microblogPhoto);
                    if (o.isComment()) {
                        imageView.setPadding(20, 0, 0, 0);
                    } else {
                        imageView.setPadding(0, 0, 0, 0);
                    }
                    if (o.getImage() != null) {
                        imageView.setImageBitmap(o.getImage());
                    } else {
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
                                o.setImage(bm);
                            } catch (Exception e) {
                            }
                        }
                    }
                    imageView.getLayoutParams().width = 50;
                    imageView.getLayoutParams().height = 50;
                }
            }
        } catch (Exception e) {
        }
        return v;
    }
