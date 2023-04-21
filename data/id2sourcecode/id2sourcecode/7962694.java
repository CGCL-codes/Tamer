    @Override
    protected void visitImpl(Object object, boolean asNode, Shader s, Path path) {
        Object shape = state.getObjectDefault(object, asNode, Attributes.SHAPE, null);
        float ior = 1f;
        if (shape != null) {
            infinite = state.getBooleanDefault(object, asNode, Attributes.TREATED_AS_INFINITE, false);
            if (shape instanceof Renderable) {
                shader = getCurrentShader();
                if (shape instanceof Sky) {
                    sky = (Sky) shape;
                    hasSunSky = true;
                    if (shader instanceof SunSkyLight) {
                        SunshineSunSky sunshinesky = new SunshineSunSky();
                        sunshinesky.setSun(((SunSkyLight) shader).getSun());
                        sunshinesky.setDisableSun(((SunSkyLight) shader).isDisableSun());
                        sunshinesky.setDisableLight(((SunSkyLight) shader).isDisableLight());
                        sunshinesky.setRadianceFactor(((SunSkyLight) shader).getRadianceFactor());
                        sunshinesky.setTurbidity(((SunSkyLight) shader).getTurbidity());
                        skyParas = sunshinesky.getParams();
                    }
                    sObject = null;
                    shader = null;
                    return;
                }
                if (shape instanceof Sphere) {
                    volumeID = oh.getSphereID();
                    builder.drawSphere(((Sphere) shape).getRadius(), null, 0, null);
                    sObject = new SunshineSphere(((Sphere) shape).getRadius());
                }
                if (shape instanceof Cylinder) {
                    volumeID = oh.getCfcID();
                    float r = state.getFloat(object, asNode, Attributes.RADIUS);
                    float len = (float) state.getDouble(object, asNode, Attributes.LENGTH);
                    builder.drawFrustum(len, r, r, !((Cylinder) shape).isBaseOpen(), !((Cylinder) shape).isTopOpen(), ((Cylinder) shape).isScaleV() ? len : 1, null, 0, null);
                    sObject = new SunshineCFC(r, len, ((Cylinder) shape).isTopOpen(), ((Cylinder) shape).isBaseOpen());
                }
                if (shape instanceof Box) {
                    volumeID = oh.getBoxID();
                    builder.drawBox(((Box) shape).getWidth() * 0.5f, ((Box) shape).getLength() * 0.5f, ((Box) shape).getHeight(), null, 0, null);
                    sObject = new SunshineBox(((Box) shape).getWidth(), ((Box) shape).getHeight(), ((Box) shape).getLength());
                }
                if (shape instanceof Cone) {
                    volumeID = oh.getCfcID();
                    builder.drawFrustum(((Cone) shape).getLength(), ((Cone) shape).getRadius(), 0, !((Cone) shape).isOpen(), false, ((Cone) shape).isScaleV() ? ((Cone) shape).getLength() : 1, null, 0, null);
                    sObject = new SunshineCone(((Cone) shape).isOpen(), ((Cone) shape).getRadius(), ((Cone) shape).getLength());
                }
                if (shape instanceof Frustum) {
                    volumeID = oh.getCfcID();
                    if (((Frustum) object).getBaseRadius() == ((Frustum) shape).getTopRadius()) {
                        builder.drawFrustum(((Frustum) shape).getLength(), ((Frustum) shape).getBaseRadius(), ((Frustum) shape).getBaseRadius(), !((Frustum) shape).isBaseOpen(), !((Frustum) shape).isTopOpen(), ((Frustum) shape).isScaleV() ? ((Frustum) shape).getLength() : 1, null, 0, null);
                        sObject = new SunshineCFC(((Frustum) shape).getBaseRadius(), ((Frustum) shape).getLength(), ((Frustum) shape).isTopOpen(), ((Frustum) shape).isBaseOpen());
                    } else {
                        builder.drawFrustum(((Frustum) shape).getLength(), ((Frustum) shape).getBaseRadius(), ((Frustum) shape).getTopRadius(), !((Frustum) shape).isBaseOpen(), !((Frustum) shape).isTopOpen(), ((Frustum) shape).isScaleV() ? ((Frustum) shape).getLength() : 1, null, 0, null);
                        sObject = new SunshineFrustum(((Frustum) shape).getBaseRadius(), ((Frustum) shape).getTopRadius(), ((Frustum) shape).getLength(), ((Frustum) shape).isTopOpen(), ((Frustum) shape).isBaseOpen());
                    }
                }
                if (shape instanceof Plane) {
                    volumeID = oh.getPlaneID();
                    builder.drawPlane(null, 0, null);
                    sObject = new SunshinePlane();
                }
                if (shape instanceof Parallelogram) {
                    volumeID = oh.getParaID();
                    AreaLight light = ((Parallelogram) shape).getLight();
                    boolean isLight = light != null;
                    builder.drawParallelogram(((Parallelogram) shape).getLength(), ((Parallelogram) shape).getAxis(), 1, ((Parallelogram) shape).isScaleV() ? ((Parallelogram) shape).getLength() : 1, null, 0, null);
                    sObject = new SunshineParalellogram(((Parallelogram) shape).getLength(), ((Parallelogram) shape).getAxis(), isLight);
                    if (isLight) {
                        SunshineObject areaLight = new SunshineAreaLight(light.getPower(), light.getExponent(), light.isShadowless(), ((Parallelogram) shape).getLength(), ((Parallelogram) shape).getAxis(), count);
                        Shader sh = ((Parallelogram) shape).getShader();
                        if (light instanceof SpectralAreaLight) {
                            LambertShader lam = new LambertShader();
                            lam.setChannel(((SpectralAreaLight) light).getChannel());
                            areaLight.setShader(getShader(lam, 1.0f));
                        } else if (sh instanceof SunshineSpectralShader) {
                            areaLight.setShader(getShader(sh, 1.0f));
                        } else if (sh instanceof RGBAShader) {
                            areaLight.setShader((RGBAShader) sh);
                        }
                        areaLight.setTransformMatrix(getCurrentTransformation());
                        oh.storePrimitive(areaLight);
                    }
                    count++;
                }
                if (shape instanceof MeshNode) {
                    volumeID = oh.getTriangleID();
                    builder.drawPolygons((MeshNode) shape, object, asNode, null, 0, getCurrentTransformation());
                    PolygonMesh p = (PolygonMesh) ((MeshNode) shape).getPolygons();
                    int[] index = p.getIndexData();
                    if (index.length > 0) {
                        sObject = new SunshineTriangles(true, p.getVertexData(), index, p.getNormalData(), p.getTextureData());
                        if (shader != null) sObject.setShader(getShader(shader, getIOR(sObject, (ShadedNull) shape)));
                        sObject.setTransformMatrix(getCurrentTransformation());
                        oh.storePrimitive(sObject);
                        sObject = null;
                        shader = null;
                    }
                }
            }
        }
        Light light = (Light) state.getObjectDefault(object, asNode, Attributes.LIGHT, null);
        if (light != null && light instanceof PointLight) {
            sObject = new SunshineLight(((PointLight) light).getPower(), ((PointLight) light).getAttenuationDistance(), ((PointLight) light).getAttenuationExponent(), light.isShadowless());
            Color3f tmpColor = new Color3f();
            tmpColor = ((PointLight) light).getColor();
            sObject.setShader(tmpColor.x, tmpColor.y, tmpColor.z, 1);
            shader = null;
            if (light instanceof SpotLight) {
                ((SunshineLight) sObject).setInnerAngle(((SpotLight) light).getInnerAngle());
                ((SunshineLight) sObject).setOuterAngle(((SpotLight) light).getOuterAngle());
            }
        }
        if (sObject != null) {
            if (!(shape instanceof LightNode)) {
                ior = getIOR(sObject, (ShadedNull) shape);
                sObject.setIOR(ior);
            }
            if (shader != null) sObject.setShader(getShader(shader, ior));
            sObject.setTransformMatrix(getCurrentTransformation());
            oh.storePrimitive(sObject);
            sObject = null;
            shader = null;
        }
    }
