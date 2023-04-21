    final void _doZoom(final int delta) {
        final int MULTIPLICATION = 3;
        if (DEBUG) System.out.println("Zoom: " + delta);
        int scaled_image_width = this.scaled_image_width;
        final int old_fov_left = Math.max(0, image_origin.x);
        final int old_fov_right = Math.min(vport_dims.width, image_origin.x + scaled_image_width);
        final int old_fov_top = Math.max(0, image_origin.y);
        final int old_fov_bottom = Math.min(vport_dims.height, image_origin.y + scaled_image_height);
        final int center_old_fov_x = old_fov_left + (old_fov_right - old_fov_left) / 2;
        final int center_old_fov_y = old_fov_top + (old_fov_bottom - old_fov_top) / 2;
        final double old_scale_factor = scale_factor;
        final Point old_image_origin = image_origin;
        final double center_fov_x = ((center_old_fov_x - old_image_origin.x) / old_scale_factor);
        final double center_fov_y = (center_old_fov_y - old_image_origin.y) / old_scale_factor;
        scaled_image_width = _cappedScaledImageWidth(scaled_image_width + delta * MULTIPLICATION);
        scaled_image_height = scaled_image_width * original_image_height / original_image_width;
        scale_factor = ((double) scaled_image_width) / ((double) original_image_width);
        final double new_scale_factor = scale_factor;
        image_origin.x = Math.round((float) (center_old_fov_x - center_fov_x * new_scale_factor));
        image_origin.y = Math.round((float) (center_old_fov_y - center_fov_y * new_scale_factor));
        _updateVportCursorPosition();
        this.scaled_image_width = scaled_image_width;
        if (DEBUG) System.out.println("image_origin:" + image_origin + " scaled_image_width:" + scaled_image_width + " scaled_image_height:" + scaled_image_height);
        repaint();
    }
