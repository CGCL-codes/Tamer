    public void buildSceneFromGraph(Graph graph, ViewConfig3D view, ObjectFilter measureFilter, boolean enableSensors, ProgressMonitor monitor, boolean sampleExplicit, float flatness) {
        this.graph = graph;
        this.view = view;
        Matrix4d m = new Matrix4d();
        m.setIdentity();
        long gatheringTime = System.currentTimeMillis();
        log("<b>Build Profile</b>");
        FluxSceneVisitor sceneVisitor = new FluxSceneVisitor();
        sceneVisitor.init(GraphState.current(graph), m, view, view != null);
        sceneVisitor.visitScene(graph, view, measureFilter, enableSensors, sampleExplicit, monitor, flatness, null);
        log(sceneVisitor.getLog());
        gatheringTime = System.currentTimeMillis() - gatheringTime;
        log("    Object gather time:    " + gatheringTime + " ms");
        primitives = sceneVisitor.getPrimitives();
        infPrimitives = sceneVisitor.getInfPrimitives();
        sensors = sceneVisitor.getSensors();
        channels = sceneVisitor.getChannels();
        shaders = sceneVisitor.getShaders();
        groupCount = sceneVisitor.getGroupCount();
        nodeToGroup = sceneVisitor.getNodeToGroup();
        long computeBoundingBoxesTime = System.currentTimeMillis();
        Variables temp = new Variables();
        for (FluxPrimitive prim : primitives) prim.computeExtent(temp);
        for (FluxPrimitive prim : infPrimitives) prim.computeExtent(temp);
        for (FluxSensor prim : sensors) prim.computeExtent(temp);
        computeBoundingBoxesTime = System.currentTimeMillis() - computeBoundingBoxesTime;
        log("    Extend compute time:   " + computeBoundingBoxesTime + " ms");
        int performance = FluxSettings.getOCLPerformance();
        BVHBuilder bvhBuilder = null;
        if (performance == FluxSettings.PERFORMANCE_TRACE) bvhBuilder = new ThreadedBVHBuilderSAH(); else bvhBuilder = new ThreadedBVHBuilderMiddle();
        monitor.setProgress("Build BVH", -1);
        long bvhConstructionTime = System.currentTimeMillis();
        bvh = bvhBuilder.construct(primitives);
        bvhConstructionTime = System.currentTimeMillis() - bvhConstructionTime;
        log("    BVH build time:        " + bvhConstructionTime + " ms");
        log(bvhBuilder.getLog());
        bounds = new BoundingBox3d();
        bounds.extent(bvh.getBounds());
        long sensorBvhConstructionTime = 0;
        if (enableSensors) {
            monitor.setProgress("Build Sensor BVH", -1);
            sensorBvhConstructionTime = System.currentTimeMillis();
            sensorBvh = bvhBuilder.construct(sensors);
            sensorBvhConstructionTime = System.currentTimeMillis() - sensorBvhConstructionTime;
            log("    Sensor BVH build time: " + sensorBvhConstructionTime + " ms");
            log(bvhBuilder.getLog());
            bounds.extent(sensorBvh.getBounds());
        }
        if (bounds.isEmpty()) {
            bounds.getMin().set(-1, -1, -1);
            bounds.getMax().set(1, 1, 1);
        }
        setLights(sceneVisitor.getLightBuilder());
    }
