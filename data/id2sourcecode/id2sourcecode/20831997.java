    public final synchronized void doLayout() {
        final Dimension new_vport_dims = getSize();
        if (new_vport_dims.height <= 0 || new_vport_dims.width <= 0) return;
        if (DEBUG) {
            System.out.println(this + " vport_dims: " + vport_dims);
            System.out.println("\timage_origin: " + image_origin);
            System.out.println("\tscaled_image_width: " + scaled_image_width);
        }
        if (offscreen_buffer != null) {
            offscreen_gc.dispose();
            offscreen_gc = null;
            offscreen_buffer = null;
        }
        final double old_scale_factor = scale_factor;
        final int old_fov_left = Math.max(0, image_origin.x);
        final int old_fov_right = Math.min(vport_dims.width, image_origin.x + scaled_image_width);
        final int old_fov_top = Math.max(0, image_origin.y);
        final int old_fov_bottom = Math.min(vport_dims.height, image_origin.y + scaled_image_height);
        final int vport_center_old_fov_x = old_fov_left + (old_fov_right - old_fov_left) / 2;
        final int vport_center_old_fov_y = old_fov_top + (old_fov_bottom - old_fov_top) / 2;
        final int old_fov_width = Math.max(1, old_fov_right - old_fov_left);
        final int old_fov_height = Math.max(1, old_fov_bottom - old_fov_top);
        final float old_fov_aspect = ((float) old_fov_width) / old_fov_height;
        final float new_vport_aspect = ((float) new_vport_dims.width) / new_vport_dims.height;
        final double new_scale_factor = old_scale_factor * ((new_vport_aspect > old_fov_aspect) ? (((float) new_vport_dims.height) / old_fov_height) : (((float) new_vport_dims.width) / old_fov_width));
        final Point old_image_origin = image_origin;
        vport_dims = new_vport_dims;
        scaled_image_width = _cappedScaledImageWidth((int) (new_scale_factor * original_image_width));
        scaled_image_height = scaled_image_width * original_image_height / original_image_width;
        scale_factor = ((double) scaled_image_width) / ((double) original_image_width);
        final double center_old_fov_x = (vport_center_old_fov_x - old_image_origin.x) / old_scale_factor;
        final double center_old_fov_y = (vport_center_old_fov_y - old_image_origin.y) / old_scale_factor;
        image_origin.x = Math.round((float) (vport_dims.width / 2.0 - center_old_fov_x * scale_factor));
        image_origin.y = Math.round((float) (vport_dims.height / 2.0 - center_old_fov_y * scale_factor));
        _updateVportCursorPosition();
        if (DEBUG) {
            System.out.println("New values: " + " vport_dims: " + vport_dims);
            System.out.println("\timage_origin: " + image_origin);
            System.out.println("\tscaled_image_width: " + scaled_image_width);
            System.out.println("\tscaled_image_height: " + scaled_image_height);
        }
        repaint();
    }
