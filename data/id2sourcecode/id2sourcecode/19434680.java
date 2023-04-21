    public void run() {
        int width_big = imagenprincipal.getWidth();
        int height_big = imagenprincipal.getHeight();
        int width_small = imagenescuadros.get(0).getWidth();
        int height_small = imagenescuadros.get(0).getHeight();
        int num_imagenes = imagenescuadros.size();
        float num_pixel = width_small * height_small;
        float num_pixel_right = (width_big % width_small) * height_small;
        float num_pixel_down = (height_big % height_small) * width_small;
        float num_pixel_last = (width_big % width_small) * (height_big % height_small);
        int num_tot_cuadros = (int) Math.floor((height_big / height_small) * (width_big / width_small));
        if (num_tot_cuadros == 0) num_tot_cuadros = 1;
        int current_cuad = 0;
        int R;
        int G;
        int B;
        int X;
        int Y;
        int rgb;
        Random random = new Random();
        int prom_color;
        int R_prom;
        int G_prom;
        int B_prom;
        int cuad = 0;
        boolean lim_h = false;
        boolean lim_v = false;
        BufferedImage cuadro;
        try {
            salida = new BufferedImage(width_big, height_big, imagenprincipal.getType());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, java.util.ResourceBundle.getBundle("lang").getString("error_memory"), java.util.ResourceBundle.getBundle("lang").getString("error"), JOptionPane.ERROR_MESSAGE);
            mythread = null;
        }
        for (int i = 0; i < height_big; i += height_small) {
            if (tipo_reparticion == 1) {
                current_cuad = random.nextInt(num_imagenes);
            }
            for (int j = 0; j < width_big; j += width_small) {
                if (tipo_reparticion == 0) {
                    current_cuad = random.nextInt(num_imagenes);
                }
                if (mythread == null) return;
                cuad++;
                long tot_R = 0;
                long tot_G = 0;
                long tot_B = 0;
                for (int k = 0; k < height_small; k++) {
                    lim_v = false;
                    Y = k + i;
                    if (Y < height_big) {
                        for (int l = 0; l < width_small; l++) {
                            lim_h = false;
                            X = l + j;
                            if (X < width_big) {
                                rgb = imagenprincipal.getRGB(X, Y);
                                tot_R += (rgb >> 16) & 0xFF;
                                tot_G += (rgb >> 8) & 0xFF;
                                tot_B += rgb & 0xFF;
                            } else {
                                lim_h = true;
                            }
                        }
                    } else {
                        lim_v = true;
                    }
                }
                if (!lim_v) {
                    if (!lim_h) {
                        prom_color = ((int) (tot_R / num_pixel) << 16) + ((int) (tot_G / num_pixel) << 8) + (int) (tot_B / num_pixel);
                    } else {
                        prom_color = ((int) (tot_R / num_pixel_right) << 16) + ((int) (tot_G / num_pixel_right) << 8) + (int) (tot_B / num_pixel_right);
                    }
                } else {
                    if (!lim_h) {
                        prom_color = ((int) (tot_R / num_pixel_down) << 16) + ((int) (tot_G / num_pixel_down) << 8) + (int) (tot_B / num_pixel_down);
                    } else {
                        prom_color = ((int) (tot_R / num_pixel_last) << 16) + ((int) (tot_G / num_pixel_last) << 8) + (int) (tot_B / num_pixel_last);
                    }
                }
                R_prom = (prom_color >> 16) & 0xFF;
                G_prom = (prom_color >> 8) & 0xFF;
                B_prom = prom_color & 0xFF;
                cuadro = imagenescuadros.get(current_cuad);
                for (int k = 0; k < height_small; k++) {
                    Y = k + i;
                    if (Y < height_big) {
                        for (int l = 0; l < width_small; l++) {
                            X = l + j;
                            if (X < width_big) {
                                rgb = cuadro.getRGB(l, k);
                                R = (rgb >> 16) & 0xFF;
                                G = (rgb >> 8) & 0xFF;
                                B = rgb & 0xFF;
                                if (tipo_color == 1) {
                                    salida.setRGB(X, Y, ((R + R_prom) / 2 << 16) + (((G + G_prom) / 2) << 8) + (((B + B_prom) / 2)));
                                } else if (tipo_color == 0) {
                                    salida.setRGB(X, Y, ((int) (R * R_prom / 255.0) << 16) + ((int) (G * G_prom / 255.0) << 8) + ((int) (B * B_prom / 255.0)));
                                } else if (tipo_color == 2) {
                                    if (parametros == 0) {
                                        R = (int) (0.3 * (float) R + 0.59 * (float) G + 0.11 * (float) B);
                                    } else {
                                        int max = R;
                                        if (G > R) max = G;
                                        if (B > max) max = B;
                                        if (parametros == 1) {
                                            int min = R;
                                            if (G < R) min = G;
                                            if (B < min) min = B;
                                            R = (max + min) / 2;
                                        } else {
                                            R = max;
                                        }
                                    }
                                    salida.setRGB(X, Y, ((int) (R * R_prom / 255.0) << 16) + ((int) (R * G_prom / 255.0) << 8) + ((int) (R * B_prom / 255.0)));
                                } else if (tipo_color == 3) {
                                    salida.setRGB(X, Y, ((int) ((255.0 - R) * R_prom / 255.0) << 16) + ((int) ((255.0 - G) * G_prom / 255.0) << 8) + ((int) ((255.0 - B) * B_prom / 255.0)));
                                } else {
                                    salida.setRGB(X, Y, ((int) (Math.sqrt(R * R_prom)) << 16) + ((int) (Math.sqrt(G * G_prom)) << 8) + ((int) (Math.sqrt(B * B_prom))));
                                }
                            }
                        }
                    }
                }
                current_cuad++;
                if (current_cuad == num_imagenes) current_cuad = 0;
                progress = (cuad * 100) / num_tot_cuadros;
            }
        }
        progress = 1000;
    }
