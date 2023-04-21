    private void addGradientPaint(Entry e) throws IOException {
        GradientPaint gp = (GradientPaint) e.paint;
        PDFDictionary pattern = pdf.openDictionary(e.name);
        pattern.entry("Type", pdf.name("Pattern"));
        pattern.entry("PatternType", 2);
        setMatrix(pattern, e, 0, 0);
        PDFDictionary shading = pattern.openDictionary("Shading");
        shading.entry("ShadingType", 2);
        shading.entry("ColorSpace", pdf.name("DeviceRGB"));
        Point2D p1 = gp.getPoint1();
        Point2D p2 = gp.getPoint2();
        shading.entry("Coords", new double[] { p1.getX(), p1.getY(), p2.getX(), p2.getY() });
        double[] domain = new double[] { 0, 1 };
        shading.entry("Domain", domain);
        String functionRef = e.name + "Function";
        shading.entry("Function", pdf.ref(functionRef));
        shading.entry("Extend", new boolean[] { true, true });
        pattern.close(shading);
        pdf.close(pattern);
        float[] col0 = new float[3];
        gp.getColor1().getRGBColorComponents(col0);
        double c0[] = new double[] { col0[0], col0[1], col0[2] };
        float[] col1 = new float[3];
        gp.getColor2().getRGBColorComponents(col1);
        double c1[] = new double[] { col1[0], col1[1], col1[2] };
        if (gp.isCyclic()) {
            addLinearFunction(functionRef, c0, c1, domain);
        } else {
            addLinearFunction(functionRef, c0, c1, domain);
        }
    }
