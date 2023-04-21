    public boolean saveThumbnailImage(BasicJpeg im, OutputStream os) throws IOException {
        try {
            String tnfn = heapcontent.get(new Integer(K_TC_THUMBNAILFILENAME)).toString();
            if (tnfn != null) {
                InputStream is = new FileInputStream(new File(new File(im.getLocationName()).getParent(), tnfn));
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) > 0) os.write(buffer, 0, len);
                return true;
            }
        } catch (NullPointerException e) {
        }
        return super.saveThumbnailImage(im, os);
    }
