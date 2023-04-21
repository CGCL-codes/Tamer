    public static void regularize(CoordinateTransform t, int index) {
        if (t instanceof AffineModel2D || t instanceof SimilarityModel2D) {
            final AffineTransform a = (t instanceof AffineModel2D) ? ((AffineModel2D) t).createAffine() : ((SimilarityModel2D) t).createAffine();
            a.translate(centerX[index], centerY[index]);
            final double a11 = a.getScaleX();
            final double a21 = a.getShearY();
            final double scaleX = Math.sqrt(a11 * a11 + a21 * a21);
            final double rotang = Math.atan2(a21 / scaleX, a11 / scaleX);
            final double a12 = a.getShearX();
            final double a22 = a.getScaleY();
            final double shearX = Math.cos(-rotang) * a12 - Math.sin(-rotang) * a22;
            final double scaleY = Math.sin(-rotang) * a12 + Math.cos(-rotang) * a22;
            final double transX = Math.cos(-rotang) * a.getTranslateX() - Math.sin(-rotang) * a.getTranslateY();
            final double transY = Math.sin(-rotang) * a.getTranslateX() + Math.cos(-rotang) * a.getTranslateY();
            final double new_shearX = shearX * (1.0 - tweakShear);
            final double avgScale = (scaleX + scaleY) / 2;
            final double aspectRatio = scaleX / scaleY;
            final double regAvgScale = avgScale * (1.0 - tweakScale) + 1.0 * tweakScale;
            final double regAspectRatio = aspectRatio * (1.0 - tweakIso) + 1.0 * tweakIso;
            final double new_scaleY = (2.0 * regAvgScale) / (regAspectRatio + 1.0);
            final double new_scaleX = regAspectRatio * new_scaleY;
            final AffineTransform b = makeAffineMatrix(new_scaleX, new_scaleY, new_shearX, 0, rotang, transX, transY);
            b.translate(-centerX[index], -centerY[index]);
            if (t instanceof AffineModel2D) ((AffineModel2D) t).set(b); else ((SimilarityModel2D) t).set((float) b.getScaleX(), (float) b.getShearY(), (float) b.getTranslateX(), (float) b.getTranslateY());
        }
    }
