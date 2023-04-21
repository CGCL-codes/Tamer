    public static Dimension saveScaledWallpaperFileToDisk(Wallpaper wallpaper, Map<String, Integer> dimmensions, String basepath, File resized_folder, String orig_file_name) {
        Set<Entry<String, Integer>> entrySet = dimmensions.entrySet();
        Iterator<Entry<String, Integer>> entries = entrySet.iterator();
        Vector<File> files = new Vector<File>(entrySet.size());
        try {
            long ms = System.currentTimeMillis();
            BufferedImageHolder holder = null;
            if (wallpaper.getContent() != null) {
                holder = ImageUtils.readImage(wallpaper.getContent());
            } else if (wallpaper.getContent_file() != null) {
                holder = ImageUtils.readImage(wallpaper.getContent_file());
            }
            ms = System.currentTimeMillis() - ms;
            logger.debug("reading wallpaper: " + ms + "ms");
            if (holder != null) {
                while (entries.hasNext()) {
                    Entry<String, Integer> e = entries.next();
                    File rez = new File(basepath + File.separator + e.getKey() + File.separator + wallpaper.getName());
                    File rezult_candidate = null;
                    if (resized_folder != null) {
                        rezult_candidate = new File(resized_folder, e.getKey() + File.separator + orig_file_name);
                    }
                    if (rezult_candidate != null && rezult_candidate.exists()) {
                        FileUtils.copyFile(rezult_candidate, rez);
                    } else {
                        double scale_factor = ImageUtils.getScaling(holder.getImage().getWidth(), e.getValue());
                        ms = System.currentTimeMillis();
                        if ((scale_factor < 1 && scale_factor > 0) || (holder.needEncode())) {
                            logger.debug("encoded: " + holder.getFormat_name());
                            ImageUtils.saveScaledImageWidth(holder.getImage(), scale_factor, rez);
                        } else {
                            logger.debug("not encoded: " + holder.getFormat_name());
                            InputStream is = holder.getInputStream();
                            FileUtils.saveToFile(is, rez);
                            is.close();
                        }
                    }
                    ms = System.currentTimeMillis() - ms;
                    logger.debug("resizing-->" + e.getKey() + ": " + ms + "ms");
                    files.add(rez);
                }
                int max_width = dimmensions.get(FULL_DIMMENSION_NAME);
                double scale_factor = ImageUtils.getScaling(holder.getImage().getWidth(), max_width);
                if (scale_factor < 1 && scale_factor > 0) {
                    return ImageUtils.getDimmension(scale_factor, holder.getImage());
                } else {
                    return new Dimension(holder.getImage().getWidth(), holder.getImage().getHeight());
                }
            } else {
                return null;
            }
        } catch (IOException ex) {
            logger.error("saving scaled wallpaper to disk", ex);
            for (File f : files) {
                f.delete();
            }
            return null;
        }
    }
