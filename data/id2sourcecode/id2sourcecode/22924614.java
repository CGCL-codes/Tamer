    public void loadPortrait(final String url, final ImageView iv) {
        if (null == url || "".equals(url)) {
            return;
        }
        final String id = md5(url);
        if (imageCache.containsKey(id)) {
            SoftReference<Drawable> softReference = imageCache.get(id);
            if (softReference != null) {
                if (softReference.get() != null) {
                    log("image::: id = " + id + " load portrait not null , ");
                    iv.setImageDrawable(softReference.get());
                    return;
                }
            }
        }
        executorService.submit(new Runnable() {

            BitmapDrawable bitmapDrawable = null;

            @Override
            public void run() {
                try {
                    bitmapDrawable = new BitmapDrawable(new URL(url).openStream());
                    imageCache.put(id, new SoftReference<Drawable>(bitmapDrawable));
                    log("image::: put:  id = " + id + " ");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        iv.setImageDrawable(bitmapDrawable);
                    }
                });
            }
        });
    }
