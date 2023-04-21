    public Engine3D(int width, int height, int FPS, int shadowsType, int frameBufferMode) {
        super("hebraMotor3D");
        fpsValue = FPS;
        shadowMode = shadowsType;
        if ((shadowMode == Engine3D.DYNAMIC_SHADOWS_LOW) || (shadowMode == Engine3D.DYNAMIC_SHADOWS_MEDIUM) || (shadowMode == Engine3D.DYNAMIC_SHADOWS_HIGH)) {
            usingDinamicShadows = true;
        } else if (shadowMode == Engine3D.STATIC_SHADOWS) {
            usingStaticShadows = true;
        }
        refreshTime = 0;
        poligonIDAimedFromMouse = -1;
        object3DIDAimendFromMouse = null;
        initEngine3D();
        eLog = new EngineLog(LOGFILENAME);
        eLog.writeEngineLogWithEmisor(objetoEmisor, "Hebra del engine3D creada.");
        world = new World();
        eLog.writeEngineLogWithEmisor(objetoEmisor, "Instancia WORLD del mundo virtual creada.");
        texMan = TextureManager.getInstance();
        eLog.writeEngineLogWithEmisor(objetoEmisor, "Gestor de texturas instanciado.");
        world.setAmbientLight(255, 255, 255);
        eLog.writeEngineLogWithEmisor(objetoEmisor, "Luz ambiental definida, valores m�ximos.");
        buffer = new FrameBuffer(width, height, frameBufferMode);
        String modeFrameBuffer = getStringFromRenderMethod(frameBufferMode);
        eLog.writeEngineLogWithEmisor(objetoEmisor, "FrameBuffer creado y configurado para OPENGL en modo: " + modeFrameBuffer + ".");
        eLog.writeEngineLogWithEmisor(objetoEmisor, "Desactivada renderizaci�n por software.");
        buffer.disableRenderer(IRenderer.RENDERER_SOFTWARE);
        if (usingDinamicShadows) {
            projector = new Projector();
            Config.glShadowZBias = 0.0f;
            switch(shadowMode) {
                case (Engine3D.DYNAMIC_SHADOWS_LOW):
                    shadowHelper = new ShadowHelper(world, buffer, projector, Engine3D.DYNAMIC_SHADOWS_LOW);
                    break;
                case (Engine3D.DYNAMIC_SHADOWS_MEDIUM):
                    shadowHelper = new ShadowHelper(world, buffer, projector, Engine3D.DYNAMIC_SHADOWS_MEDIUM);
                    break;
                case (Engine3D.DYNAMIC_SHADOWS_HIGH):
                    shadowHelper = new ShadowHelper(world, buffer, projector, Engine3D.DYNAMIC_SHADOWS_HIGH);
                    break;
            }
            shadowHelper.setFiltering(true);
            shadowHelper.setAmbientLight(new Color(150, 150, 150));
            eLog.writeEngineLogWithEmisor(objetoEmisor, "Sombras din�micas iniciadas.");
        }
        eLog.writeEngineLogWithEmisor(objetoEmisor, "Canvas asociado al FrameBuffer creado.");
        canvas = buffer.enableGLCanvasRenderer();
        paintListener = new PaintListener();
        buffer.setPaintListener(paintListener);
        buffer.setPaintListenerState(true);
        eLog.writeEngineLogWithEmisor(objetoEmisor, "PaintListener creado, asociado a buffer y activado.");
        oManager = new ObjectManager(eLog);
        eLog.writeEngineLogWithEmisor(objetoEmisor, "Manejador de almacenamiento de texturas y mallas creado.");
        handlObj = new HandlObj(this, eLog);
        eLog.writeEngineLogWithEmisor(objetoEmisor, "Manejador de objetos del mundo virtual creado.");
        handlEngine3D = new HandlEngine(this);
        eLog.writeEngineLogWithEmisor(objetoEmisor, "Manjeador del engine3D creado.");
        animStack = new AnimationStack();
        eLog.writeEngineLogWithEmisor(objetoEmisor, "Cola de animaciones y acciones del engine3D creado.");
        mouseListener = new MouseListenerEngine3D(this);
        eLog.writeEngineLogWithEmisor(objetoEmisor, "Listener de rat�n creado para el engine3D.");
        diskLoader = new DiskLoader(this);
        eLog.writeEngineLogWithEmisor(objetoEmisor, "Cargador de archivos del engine3D creado.");
        eLog.writeEngineLogWithEmisor(objetoEmisor, "Listos para arrancar hebra Engine3D.");
    }
