    @Override
    public void run() {
        Map<String, Bitmap> locImages = new HashMap<String, Bitmap>();
        locImages.putAll(this.images);
        Set<String> imageUrls = locImages.keySet();
        for (String string : imageUrls) {
            try {
                if (this.images.get(string) == null) {
                    Log.d(this.toString(), "loading a bitmap " + string);
                    long time = System.currentTimeMillis();
                    URL url = new URL(string);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    InputStream stream = con.getInputStream();
                    Bitmap b = BitmapFactory.decodeStream(stream);
                    stream.close();
                    Log.d(this.toString(), "loaded a bitmap in " + (System.currentTimeMillis() - time));
                    this.images.put(string, b);
                }
                if (this.stop == true) {
                    break;
                }
            } catch (Exception e) {
                this.images.put(string, null);
                Log.e(this.toString(), string + " could not be loaded", e);
            }
        }
        Map<String, List<ImageView>> locViews = new HashMap<String, List<ImageView>>();
        locViews.putAll(this.views);
        Set<String> keys = locViews.keySet();
        for (String key : keys) {
            if (this.stop == true) {
                break;
            }
            Bitmap b = this.images.get(key);
            if (b != null) {
                List<ImageView> iviews = this.views.get(key);
                List<Typer> itypers = this.typers.get(key);
                for (Typer typer : itypers) {
                    Log.d(this.toString(), "adding a image for " + key);
                    ImageView iview = typer.type(this.images.get(key));
                    iviews.add(iview);
                    iview.setImageBitmap(b);
                }
            }
        }
        this.stop = false;
        this.hasRun = true;
    }
