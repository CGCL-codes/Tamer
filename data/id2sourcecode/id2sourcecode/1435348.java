    public boolean loadFile() {
        File testFile;
        Hashtable infoTable;
        BranchGroup bg;
        Group dummygroup;
        DetailGroup lod;
        DistanceDetailGroup dlod;
        Group group;
        Group recordGroup;
        String fieldName, fieldData;
        TransformGroup tg;
        TransformGroup tg2;
        Text text;
        ESRIRecord rec;
        ESRIPoly[] polygons;
        int i;
        boolean nameSet;
        float[] distances;
        ShapeFile inshape;
        DBaseFile dbase;
        de.fhg.igd.earth.model.graph.Polygon pol;
        int nPolys;
        int len;
        float[] pts;
        boolean dbaseExists;
        showPleaseWait(true);
        bg = new BranchGroup();
        bg.setName("Shape: " + plainName_);
        group = bg;
        if (lod_ == true) {
            lod = new DetailGroup();
            lod.setName("LoD");
            bg.addChild(lod);
            group = lod;
        }
        if (distance_ > 0) {
            distances = new float[1];
            distances[0] = distance_;
            dlod = new DistanceDetailGroup(distances);
            dlod.setName("DLoD " + String.valueOf(distance_) + "%");
            group.addChild(dlod);
            if (detailLevel_ == 1) {
                dummygroup = new Group();
                dummygroup.setName("dummy");
                dlod.addChild(dummygroup);
            }
            group = dlod;
        }
        tg = new TransformGroup();
        tg.setName("Data");
        group.addChild(tg);
        testFile = new File(filenameSHP_);
        if (testFile == null || !testFile.exists()) {
            JOptionPane.showMessageDialog(null, filenameSHP_, "shape file not found", JOptionPane.INFORMATION_MESSAGE);
            showPleaseWait(false);
            return false;
        }
        dbaseExists = false;
        testFile = new File(filenameDBF_);
        if (testFile != null) dbaseExists = testFile.exists();
        dbase = null;
        try {
            int counter = 0;
            inshape = new ShapeFile(filenameSHP_);
            if (dbaseExists) dbase = new DBaseFile(filenameDBF_);
            shapeFileType_ = inshape.getShapeType();
            rec = (ESRIRecord) inshape.getNextRecord();
            if (dbaseExists) dbase.getNextRecord();
            while (rec != null) {
                if (selected_.size() == 0 || (((Boolean) selected_.elementAt(counter)).booleanValue()) == true) {
                    recordGroup = new Group();
                    recordGroup.setPickable(true);
                    recordGroup.setName("Record");
                    nameSet = false;
                    if (dbaseExists) {
                        infoTable = recordGroup.getInfoTable();
                        for (i = 0; i < dbase.numRows(); i++) {
                            fieldName = dbase.getFieldName(i);
                            fieldData = dbase.getFieldData(i);
                            if (nameSet == false) {
                                String test = fieldName.toUpperCase();
                                int p = test.indexOf("NAME");
                                if (p >= 0) {
                                    recordGroup.setName(fieldData);
                                    nameSet = true;
                                }
                            }
                            infoTable.put(fieldName, fieldData);
                        }
                        recordGroup.setInfoTable(infoTable);
                    }
                    switch(shapeFileType_) {
                        case ShapeFile.SHAPE_TYPE_MULTIPOINT:
                            break;
                        case ShapeFile.SHAPE_TYPE_NULL:
                            break;
                        case ShapeFile.SHAPE_TYPE_POINT:
                            double xm, ym, x0, x1, y0, y1;
                            xm = ((ESRIPointRecord) rec).getBoundingBox().min.x;
                            ym = ((ESRIPointRecord) rec).getBoundingBox().min.y;
                            x0 = xm - 0.05;
                            x1 = xm + 0.05;
                            y0 = ym - 0.05;
                            y1 = ym + 0.05;
                            pol = new de.fhg.igd.earth.model.graph.Polygon();
                            pol.setStyle(style_);
                            pol.setTransparency(transparency_);
                            pol.setColor(color_);
                            pol.addPoint(x0 + offsetX_, y0 + offsetY_, offsetZ_, 0, 0, 1);
                            pol.addPoint(x1 + offsetX_, y0 + offsetY_, offsetZ_, 0, 0, 1);
                            pol.addPoint(x1 + offsetX_, y1 + offsetY_, offsetZ_, 0, 0, 1);
                            pol.addPoint(x0 + offsetX_, y1 + offsetY_, offsetZ_, 0, 0, 1);
                            pol.addPoint(x0 + offsetX_, y0 + offsetY_, offsetZ_, 0, 0, 1);
                            recordGroup.addChild(pol);
                            break;
                        case ShapeFile.SHAPE_TYPE_POLYGON:
                            polygons = ((ESRIPolygonRecord) rec).polygons;
                            nPolys = polygons.length;
                            for (i = 0; i < nPolys; i++) {
                                pts = ((ESRIPoly.ESRIFloatPoly) polygons[i]).getDecimalDegrees();
                                len = pts.length;
                                pol = new de.fhg.igd.earth.model.graph.Polygon();
                                pol.setStyle(style_);
                                pol.setTransparency(transparency_);
                                pol.setColor(color_);
                                for (int j = 0; j < len; j += 2) pol.addPoint(pts[j + 1] + offsetX_, pts[j] + offsetY_, offsetZ_, 0, 0, 1);
                                recordGroup.addChild(pol);
                            }
                            break;
                        case ShapeFile.SHAPE_TYPE_POLYLINE:
                            polygons = ((ESRIPolygonRecord) rec).polygons;
                            nPolys = polygons.length;
                            for (i = 0; i < nPolys; i++) {
                                pts = ((ESRIPoly.ESRIFloatPoly) polygons[i]).getDecimalDegrees();
                                len = pts.length;
                                pol = new de.fhg.igd.earth.model.graph.Polygon();
                                pol.setStyle(de.fhg.igd.earth.model.graph.Polygon.STYLE_WIRE_FRAME);
                                pol.setTransparency(transparency_);
                                pol.setColor(color_);
                                for (int j = 0; j < len; j += 2) pol.addPoint(pts[j + 1] + offsetX_, pts[j] + offsetY_, offsetZ_, 0, 0, 1);
                                recordGroup.addChild(pol);
                            }
                            break;
                    }
                    tg.addChild(recordGroup);
                }
                counter++;
                rec = (ESRIRecord) inshape.getNextRecord();
                if (dbaseExists) dbase.getNextRecord();
            }
            inshape.close();
            if (dbaseExists) dbase.close();
        } catch (Exception ex) {
            System.out.println(ex);
            showPleaseWait(false);
            return false;
        }
        model_.addChild(bg);
        showPleaseWait(false);
        return true;
    }
