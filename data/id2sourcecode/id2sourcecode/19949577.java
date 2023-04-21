    public FromURL(String url) {
        try {
            if (loaded.containsKey(url)) {
                this.img = loaded.get(url);
            } else {
                this.img = BitmapFactory.decodeStream(new URL(url).openStream());
                loaded.put(url, this.img);
            }
            this.init(this.img);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Error Loading URL Image: \"" + url + "\"\n" + e.getMessage());
        }
    }
