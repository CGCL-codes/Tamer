    public Bitmap getNextImage() {
        Log.w("Debug main", Long.toString(Thread.currentThread().getId()));
        if (list.getPosition() != list.toNext()) {
            if (images[0] != null) {
                garbage.add(images[0]);
                images[0] = null;
            }
            for (int i = 0; i < imageCount - 1; i++) images[i] = images[i + 1];
            images[imageCount - 1] = null;
            new LoadImageTask().execute(new Integer[] { LOAD_LAST_IMAGE });
        }
        return images[startPosition];
    }
