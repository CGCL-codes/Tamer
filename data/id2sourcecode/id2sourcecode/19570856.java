    public static List<Wallpaper> copyWallpaper(Collection<Wallpaper> wallpaper, Map<String, Integer> dimmensions, String src_path, String dst_path, boolean append, boolean remove_copied) {
        List<Wallpaper> rez = new Vector<Wallpaper>();
        Set<Entry<String, Integer>> entrySet = dimmensions.entrySet();
        Iterator<Entry<String, Integer>> entries = entrySet.iterator();
        Iterator<Wallpaper> wallpapers = wallpaper.iterator();
        try {
            File dst_folder = new File(dst_path);
            if (!dst_folder.exists()) dst_folder.mkdirs();
            while (entries.hasNext()) {
                Entry<String, Integer> e = entries.next();
                File dst_subfolder = new File(dst_path + File.separator + e.getKey());
                if (!dst_subfolder.exists()) dst_subfolder.mkdir();
            }
            while (wallpapers.hasNext()) {
                Wallpaper p = wallpapers.next();
                boolean copied = false;
                entries = entrySet.iterator();
                while (entries.hasNext()) {
                    Entry<String, Integer> e = entries.next();
                    File src = new File(src_path + File.separator + e.getKey() + File.separator + p.getName());
                    File dst = new File(dst_path + File.separator + e.getKey() + File.separator + p.getName());
                    if (src.exists()) {
                        boolean copy = true;
                        if (!append && dst.exists() && (src.lastModified() <= dst.lastModified())) {
                            copy = false;
                            copied = true;
                        }
                        if (copy) {
                            FileUtils.copyFile(src, dst);
                            copied = true;
                            dst.setLastModified(src.lastModified());
                        }
                    }
                }
                if (remove_copied && copied) {
                    rez.add(p);
                    wallpapers.remove();
                }
            }
        } catch (FileNotFoundException ex) {
            logger.error("backing up wallpaper", ex);
        } catch (IOException ex) {
            logger.error("backing up wallpaper", ex);
        }
        return rez;
    }
