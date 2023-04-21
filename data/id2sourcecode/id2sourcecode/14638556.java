    private boolean markGeotagsAsToBeWritten(Picture picture) {
        boolean isPictureNeedsWriting = false;
        Geotag geotag = picture.getGeotag();
        if (geotag == null) {
            this.logger.finer("Geo position should be written to picture but there is no geo postion (data object) for this picture.");
            return isPictureNeedsWriting;
        }
        String lat = geotag.getLatitudePicture();
        String lon = geotag.getLongitudePicture();
        if (lat != null && lon != null) {
            this.logger.finer("Picture contains already geo coordinates. Do not allow to overwrite with geo coordinates of Picasa if any.");
        } else {
            lat = geotag.getLatitudePicasa();
            lon = geotag.getLongitudePicasa();
            if (lat != null && lon != null) {
                this.logger.finer("Picture contains no geo coordinates but Picasa has geo coordinates for this Picture.");
                geotag.setNeedsToBeWritten(true);
                isPictureNeedsWriting = true;
            } else {
                this.logger.finer("Picture contains no geo coordinates. The geo coordinates should be written to this picture but Picasa has no geo coordinates for this Picture.");
                geotag.setNeedsToBeWritten(false);
            }
        }
        return isPictureNeedsWriting;
    }
