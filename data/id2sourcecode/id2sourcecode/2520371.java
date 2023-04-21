    public void buildCurvedTexture(Object group, Set domain_set, Unit[] dataUnits, Unit[] domain_units, float[] default_values, ShadowRealType[] DomainComponents, int valueArrayLength, int[] inherited_values, int[] valueToScalar, GraphicsModeControl mode, float constant_alpha, float[] value_array, float[] constant_color, byte[][] color_bytes, DisplayImpl display, int curved_size, ShadowRealTupleType Domain, CoordinateSystem dataCoordinateSystem, DataRenderer renderer, ShadowFunctionOrSetType adaptedShadowType, int[] start, int lenX, int lenY, float[][] samples, int bigX, int bigY) throws VisADException, DisplayException {
        float[] coordinates = null;
        float[] texCoords = null;
        float[] normals = null;
        byte[] colors = null;
        int data_width = 0;
        int data_height = 0;
        int texture_width = 1;
        int texture_height = 1;
        int[] lengths = null;
        if (domain_set != null) {
            lengths = ((GriddedSet) domain_set).getLengths();
        } else {
            lengths = new int[] { lenX, lenY };
        }
        data_width = lengths[0];
        data_height = lengths[1];
        texture_width = textureWidth(data_width);
        texture_height = textureHeight(data_height);
        int size = (data_width + data_height) / 2;
        curved_size = Math.max(2, Math.min(curved_size, size / 32));
        int nwidth = 2 + (data_width - 1) / curved_size;
        int nheight = 2 + (data_height - 1) / curved_size;
        int nn = nwidth * nheight;
        int[] is = new int[nwidth];
        int[] js = new int[nheight];
        for (int i = 0; i < nwidth; i++) {
            is[i] = Math.min(i * curved_size, data_width - 1);
        }
        for (int j = 0; j < nheight; j++) {
            js[j] = Math.min(j * curved_size, data_height - 1);
        }
        int[] indices = new int[nn];
        int k = 0;
        for (int j = 0; j < nheight; j++) {
            for (int i = 0; i < nwidth; i++) {
                indices[k] = is[i] + data_width * js[j];
                k++;
            }
        }
        float[][] spline_domain = null;
        if (domain_set == null) {
            for (int kk = 0; kk < indices.length; kk++) {
                int x = indices[kk] % lenX;
                int y = indices[kk] / lenX;
                indices[kk] = (start[0] + x) + (start[1] + y) * bigX;
            }
            spline_domain = new float[2][indices.length];
            for (int kk = 0; kk < indices.length; kk++) {
                spline_domain[0][kk] = samples[0][indices[kk]];
                spline_domain[1][kk] = samples[1][indices[kk]];
            }
        } else {
            spline_domain = domain_set.indexToValue(indices);
        }
        spline_domain = Unit.convertTuple(spline_domain, dataUnits, domain_units, false);
        ShadowRealTupleType domain_reference = Domain.getReference();
        ShadowRealType[] DC = DomainComponents;
        if (domain_reference != null && domain_reference.getMappedDisplayScalar()) {
            RealTupleType ref = (RealTupleType) domain_reference.getType();
            renderer.setEarthSpatialData(Domain, domain_reference, ref, ref.getDefaultUnits(), (RealTupleType) Domain.getType(), new CoordinateSystem[] { dataCoordinateSystem }, domain_units);
            spline_domain = CoordinateSystem.transformCoordinates(ref, null, ref.getDefaultUnits(), null, (RealTupleType) Domain.getType(), dataCoordinateSystem, domain_units, null, spline_domain);
            DC = adaptedShadowType.getDomainReferenceComponents();
        } else {
            RealTupleType ref = (domain_reference == null) ? null : (RealTupleType) domain_reference.getType();
            Unit[] ref_units = (ref == null) ? null : ref.getDefaultUnits();
            renderer.setEarthSpatialData(Domain, domain_reference, ref, ref_units, (RealTupleType) Domain.getType(), new CoordinateSystem[] { dataCoordinateSystem }, domain_units);
        }
        int[] tuple_index = new int[3];
        int[] spatial_value_indices = { -1, -1, -1 };
        ScalarMap[] spatial_maps = new ScalarMap[3];
        DisplayTupleType spatial_tuple = null;
        for (int i = 0; i < DC.length; i++) {
            Enumeration maps = DC[i].getSelectedMapVector().elements();
            ScalarMap map = (ScalarMap) maps.nextElement();
            DisplayRealType real = map.getDisplayScalar();
            spatial_tuple = real.getTuple();
            if (spatial_tuple == null) {
                throw new DisplayException("texture with bad tuple: " + "ShadowImageFunctionTypeJ3D.doTransform");
            }
            tuple_index[i] = real.getTupleIndex();
            spatial_value_indices[tuple_index[i]] = map.getValueIndex();
            spatial_maps[tuple_index[i]] = map;
            if (maps.hasMoreElements()) {
                throw new DisplayException("texture with multiple spatial: " + "ShadowImageFunctionTypeJ3D.doTransform");
            }
        }
        tuple_index[2] = 3 - (tuple_index[0] + tuple_index[1]);
        DisplayRealType real = (DisplayRealType) spatial_tuple.getComponent(tuple_index[2]);
        int value2_index = display.getDisplayScalarIndex(real);
        float value2 = default_values[value2_index];
        for (int i = 0; i < valueArrayLength; i++) {
            if (inherited_values[i] > 0 && real.equals(display.getDisplayScalar(valueToScalar[i]))) {
                value2 = value_array[i];
                break;
            }
        }
        float[][] spatial_values = new float[3][];
        spatial_values[tuple_index[0]] = spline_domain[0];
        spatial_values[tuple_index[1]] = spline_domain[1];
        spatial_values[tuple_index[2]] = new float[nn];
        for (int i = 0; i < nn; i++) spatial_values[tuple_index[2]][i] = value2;
        for (int i = 0; i < 3; i++) {
            if (spatial_maps[i] != null) {
                spatial_values[i] = spatial_maps[i].scaleValues(spatial_values[i]);
            }
        }
        if (spatial_tuple.equals(Display.DisplaySpatialCartesianTuple)) {
            renderer.setEarthSpatialDisplay(null, spatial_tuple, display, spatial_value_indices, default_values, null);
        } else {
            CoordinateSystem coord = spatial_tuple.getCoordinateSystem();
            spatial_values = coord.toReference(spatial_values);
            renderer.setEarthSpatialDisplay(coord, spatial_tuple, display, spatial_value_indices, default_values, null);
        }
        coordinates = new float[3 * nn];
        k = 0;
        for (int i = 0; i < nn; i++) {
            coordinates[k++] = spatial_values[0][i];
            coordinates[k++] = spatial_values[1][i];
            coordinates[k++] = spatial_values[2][i];
        }
        boolean spatial_all_select = true;
        for (int i = 0; i < 3 * nn; i++) {
            if (coordinates[i] != coordinates[i]) spatial_all_select = false;
        }
        normals = Gridded3DSet.makeNormals(coordinates, nwidth, nheight);
        colors = new byte[3 * nn];
        for (int i = 0; i < 3 * nn; i++) colors[i] = (byte) 127;
        float ratiow = ((float) data_width) / ((float) texture_width);
        float ratioh = ((float) data_height) / ((float) texture_height);
        float half_width = 0.5f / ((float) texture_width);
        float half_height = 0.5f / ((float) texture_height);
        float width = 1.0f / ((float) texture_width);
        float height = 1.0f / ((float) texture_height);
        int mt = 0;
        texCoords = new float[2 * nn];
        for (int j = 0; j < nheight; j++) {
            for (int i = 0; i < nwidth; i++) {
                float isfactor = is[i] / (data_width - 1.0f);
                float jsfactor = js[j] / (data_height - 1.0f);
                texCoords[mt++] = (ratiow - width) * isfactor + half_width;
                texCoords[mt++] = 1.0f - (ratioh - height) * jsfactor - half_height;
            }
        }
        VisADTriangleStripArray tarray = new VisADTriangleStripArray();
        tarray.stripVertexCounts = new int[nheight - 1];
        for (int i = 0; i < nheight - 1; i++) {
            tarray.stripVertexCounts[i] = 2 * nwidth;
        }
        int len = (nheight - 1) * (2 * nwidth);
        tarray.vertexCount = len;
        tarray.normals = new float[3 * len];
        tarray.coordinates = new float[3 * len];
        tarray.colors = new byte[3 * len];
        tarray.texCoords = new float[2 * len];
        k = 0;
        int kt = 0;
        int nwidth3 = 3 * nwidth;
        int nwidth2 = 2 * nwidth;
        for (int i = 0; i < nheight - 1; i++) {
            int m = i * nwidth3;
            mt = i * nwidth2;
            for (int j = 0; j < nwidth; j++) {
                tarray.coordinates[k] = coordinates[m];
                tarray.coordinates[k + 1] = coordinates[m + 1];
                tarray.coordinates[k + 2] = coordinates[m + 2];
                tarray.coordinates[k + 3] = coordinates[m + nwidth3];
                tarray.coordinates[k + 4] = coordinates[m + nwidth3 + 1];
                tarray.coordinates[k + 5] = coordinates[m + nwidth3 + 2];
                tarray.normals[k] = normals[m];
                tarray.normals[k + 1] = normals[m + 1];
                tarray.normals[k + 2] = normals[m + 2];
                tarray.normals[k + 3] = normals[m + nwidth3];
                tarray.normals[k + 4] = normals[m + nwidth3 + 1];
                tarray.normals[k + 5] = normals[m + nwidth3 + 2];
                tarray.colors[k] = colors[m];
                tarray.colors[k + 1] = colors[m + 1];
                tarray.colors[k + 2] = colors[m + 2];
                tarray.colors[k + 3] = colors[m + nwidth3];
                tarray.colors[k + 4] = colors[m + nwidth3 + 1];
                tarray.colors[k + 5] = colors[m + nwidth3 + 2];
                tarray.texCoords[kt] = texCoords[mt];
                tarray.texCoords[kt + 1] = texCoords[mt + 1];
                tarray.texCoords[kt + 2] = texCoords[mt + nwidth2];
                tarray.texCoords[kt + 3] = texCoords[mt + nwidth2 + 1];
                k += 6;
                m += 3;
                kt += 4;
                mt += 2;
            }
        }
        if (!spatial_all_select) {
            tarray = (VisADTriangleStripArray) tarray.removeMissing();
        }
        if (adaptedShadowType.getAdjustProjectionSeam()) {
            tarray = (VisADTriangleStripArray) tarray.adjustLongitude(renderer);
            tarray = (VisADTriangleStripArray) tarray.adjustSeam(renderer);
        }
        BufferedImage image = createImage(data_width, data_height, texture_width, texture_height, color_bytes);
        textureToGroup(group, tarray, image, mode, constant_alpha, constant_color, texture_width, texture_height);
    }
