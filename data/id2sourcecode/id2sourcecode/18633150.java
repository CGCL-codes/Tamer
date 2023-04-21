    public void init() {
        System.out.println(WELCOME_MSG);
        try {
            if (IS_APPLET) {
                HELPFILE = new URL(getCodeBase().toString() + "./" + JSphereDir + "readme.txt");
                factorySettings();
            } else {
                HELPFILE = new URL(getDocumentBase().toString() + JSphereDir + "readme.txt");
            }
        } catch (MalformedURLException augh) {
        }
        if (IS_APPLET) {
            String ballDir = getParameter("BallTexDir");
            String wallDir = getParameter("WallTexDir");
            String numTex = getParameter("Textures");
            if (ballDir != null) BallTexDir = ballDir;
            if (wallDir != null) WallTexDir = wallDir;
            if (numTex != null) {
                MAXTEXTS = Integer.parseInt(numTex);
                if (MAXTEXTS < 1) MAXTEXTS = 1;
            }
        }
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);
        add("Center", canvas);
        StatusTxt = new TextArea();
        add("East", StatusTxt);
        blurb(WELCOME_MSG);
        setVisible(true);
        BranchGroup scene = createSceneGraph();
        SU = new SimpleUniverse(canvas);
        resetView();
        class KeyCmdListener extends KeyAdapter {

            public void keyPressed(KeyEvent e) {
                Matrix3d m = new Matrix3d();
                int c = e.getKeyCode();
                char C = e.getKeyChar();
                if (c == 27) {
                    JMIDI.silence();
                    unTrack();
                    resetView();
                } else if (c == 39) {
                    slewView(new Vector3d(1, 0, 0));
                } else if (c == 37) {
                    slewView(new Vector3d(-1, 0, 0));
                } else if (c == 38) {
                    slewView(new Vector3d(0, 1, 0));
                } else if (c == 40) {
                    slewView(new Vector3d(0, -1, 0));
                } else if (c == 155) {
                    slewView(new Vector3d(0, 0, 1));
                } else if (c == 12) {
                    slewView(new Vector3d(0, 0, -1));
                } else if (c == 33) {
                    m.rotX(.1);
                    rotateView(m);
                } else if (c == 36) {
                    m.rotX(-.1);
                    rotateView(m);
                } else if (c == 35) {
                    m.rotY(.1);
                    rotateView(m);
                } else if (c == 34) {
                    m.rotY(-.1);
                    rotateView(m);
                } else if (C == 'z') {
                    m.rotZ(.1);
                    rotateView(m);
                } else if (C == 'x') {
                    m.rotZ(-.1);
                    rotateView(m);
                } else if (c == 32) {
                    if (!isTracking()) track();
                    if (TRACK_STYLE == TRACK_BALL) cycleTrackBall(); else cycleViewBall();
                } else if (C == 'q') {
                    Ball.setSpeed(Ball.getSpeed() + SPEEDVAR);
                    blurb("Speed: " + Ball.getSpeed());
                } else if (C == 'a') {
                    Ball.setSpeed(Ball.getSpeed() - SPEEDVAR);
                    blurb("Speed: " + Ball.getSpeed());
                } else if (C == 'g') {
                    GRAVITY = !GRAVITY;
                    blurb("Gravity: " + GRAVITY);
                } else if (C == 'r') {
                    clearViewRotation();
                } else if (C == 't') {
                    if (!isTracking()) track();
                    if (++TRACK_STYLE >= MAX_TRACK) TRACK_STYLE = 0;
                    blurb("Track style: " + TRACK_STYLES[TRACK_STYLE]);
                    if (TRACK_STYLE == TRACK_BALL && BALLVIEW == BALLVIEW2) cycleTrackBall();
                } else if (C == ']') {
                    PAUSE += PAUSE_VAR;
                    if (PAUSE > 100000) PAUSE = 100000;
                    blurb("Pause: " + PAUSE);
                } else if (C == '[') {
                    PAUSE -= PAUSE_VAR;
                    if (PAUSE < 0) PAUSE = 0;
                    blurb("Pause: " + PAUSE);
                } else if (C == 'e' || C == 'E') {
                    BLK_BALL = !BLK_BALL;
                    blurb("Eightballs: " + BLK_BALL);
                    if (C == 'E') reset((Canvas3D) e.getComponent());
                } else if (C == 'w' || C == 'W') {
                    WALLS = !WALLS;
                    blurb("Walls: " + WALLS);
                    if (C == 'W') reset((Canvas3D) e.getComponent());
                } else if (C == 's' || C == 'S') {
                    SHARDS = !SHARDS;
                    blurb("Shards: " + SHARDS);
                    if (C == 'S') reset((Canvas3D) e.getComponent());
                } else if (C == 'd' || C == 'D') {
                    DUST = !DUST;
                    blurb("Dust: " + DUST);
                    if (C == 'D') reset((Canvas3D) e.getComponent());
                } else if (C == 'b' || C == 'B') {
                    BLK_WALL = !BLK_WALL;
                    blurb("Dark dust/shards/walls: " + BLK_WALL);
                    if (C == 'B') reset((Canvas3D) e.getComponent());
                } else if (C == 'c') {
                    MULTICOL = !MULTICOL;
                    blurb("Multiple Reflecting Colors: " + MULTICOL);
                } else if (C == 'C') {
                    BALLSHAPE++;
                    if (BALLSHAPE == BALLSHAPES) BALLSHAPE = SPHERE;
                    reset((Canvas3D) e.getComponent());
                } else if (C == 'v') {
                    VERTICIES += VERT_VAR;
                    if (VERTICIES > MAX_VERT) VERTICIES = VERT_VAR;
                    blurb("Verticies: " + VERTICIES);
                } else if (C == 'V') {
                    Ball.VISIBLE = !Ball.VISIBLE;
                    blurb("Visible Balls: " + Ball.VISIBLE);
                    reset((Canvas3D) e.getComponent());
                } else if (C == 'n' || C == 'N') {
                    BALLSIZE += SIZE_VAR;
                    if (BALLSIZE > MAX_SIZE) BALLSIZE = SIZE_VAR;
                    blurb("Ball Size: " + BALLSIZE);
                    if (C == 'N') reset((Canvas3D) e.getComponent());
                } else if (C == 'l' || C == 'L') {
                    SPOTLIGHT = !SPOTLIGHT;
                    blurb("Spotlight: " + SPOTLIGHT);
                    if (C == 'L') reset((Canvas3D) e.getComponent());
                } else if (C == 'i' || C == 'I') {
                    if (C == 'i') SPOT_CON += SPOT_CON_VAR; else SPOT_CON -= SPOT_CON_VAR;
                    if (SPOT_CON > SPOT_MAX_CON) SPOT_CON = 0; else if (SPOT_CON < 0) SPOT_CON = SPOT_MAX_CON;
                    blurb("Spotlight intensity: " + SPOT_CON);
                    setBallLights();
                } else if (C == 'k' || C == 'K') {
                    if (C == 'k') SPOT_SPREAD++; else SPOT_SPREAD--;
                    if (SPOT_SPREAD > SPOT_MAX_SPREAD) SPOT_SPREAD = 1; else if (SPOT_SPREAD < 1) SPOT_SPREAD = SPOT_MAX_SPREAD;
                    blurb("Spotlight spread angle factor: " + SPOT_SPREAD);
                    setBallLights();
                } else if (C == 'o') {
                    LIGHT_COL++;
                    if (LIGHT_COL >= LIGHT_STYLES.length) LIGHT_COL = 0;
                    blurb("Color style: " + LIGHT_STYLES[LIGHT_COL]);
                } else if (C == '*') {
                    SPOT_STYLE++;
                    if (SPOT_STYLE >= SPOT_STYLES.length) SPOT_STYLE = 0;
                    blurb("Spotlight style: " + SPOT_STYLES[SPOT_STYLE]);
                } else if (C == '>') {
                    if (SPOTLIGHT) {
                        SPOT_ATT += SPOT_ATT_VAR;
                        if (SPOT_ATT > MAX_SPOT_ATT) SPOT_ATT = 0;
                        blurb("Spotlight attenuation: " + SPOT_ATT);
                    } else {
                        POINT_ATT += POINT_ATT_VAR;
                        if (POINT_ATT > MAX_POINT_ATT) POINT_ATT = 0;
                        blurb("Pointlight attenuation: " + POINT_ATT);
                    }
                    setBallLights();
                } else if (C == '<') {
                    if (SPOTLIGHT) {
                        SPOT_ATT -= SPOT_ATT_VAR;
                        if (SPOT_ATT < 0) SPOT_ATT = MAX_SPOT_ATT;
                        blurb("Spotlight attenuation: " + SPOT_ATT);
                    } else {
                        POINT_ATT -= POINT_ATT_VAR;
                        if (POINT_ATT < 0) POINT_ATT = MAX_POINT_ATT;
                        blurb("Pointlight attenuation: " + POINT_ATT);
                    }
                    setBallLights();
                } else if (C == ',' && SONIFY) {
                    int b = BALLVIEW;
                    if (b < 0) b = 0;
                    JMIDI.getChannel(b).allNotesOff();
                    BALL_SOUNDS[b]--;
                    if (BALL_SOUNDS[b] < 0) BALL_SOUNDS[b] = MAX_INST;
                    JMIDI.setChannel(b, BALL_SOUNDS[b]);
                } else if (C == '.' && SONIFY) {
                    int b = BALLVIEW;
                    if (b < 0) b = 0;
                    JMIDI.getChannel(b).allNotesOff();
                    BALL_SOUNDS[b]++;
                    if (BALL_SOUNDS[b] > MAX_INST) BALL_SOUNDS[b] = 0;
                    JMIDI.setChannel(b, BALL_SOUNDS[b]);
                } else if (C == '/') {
                    FIXED_PITCH = !FIXED_PITCH;
                    if (FIXED_PITCH) {
                        for (int i = 0; i < NUMBALLS; i++) balls[i].setPitch();
                    }
                    blurb("Fixed ball pitch: " + FIXED_PITCH);
                } else if (C == '-') {
                    COMPLEX_BOUNCE = !COMPLEX_BOUNCE;
                    blurb("Complex bounce sounds: " + COMPLEX_BOUNCE);
                } else if (C == '=') {
                    POST_MOVE = !POST_MOVE;
                    blurb("Post-move: " + POST_MOVE);
                } else if (C == '\\') {
                    WALL_SOUND = !WALL_SOUND;
                    blurb("Wall bounce sounds: " + WALL_SOUND);
                } else if (C == '^') {
                    PITCH_DEBUG = !PITCH_DEBUG;
                    blurb("Pitch debug: " + PITCH_DEBUG);
                } else if (C == 'm') {
                    if (!SONIFY) SONIFY = initMidi(); else {
                        SONIFY = false;
                        JMIDI.unload();
                    }
                    blurb("Sonify: " + SONIFY);
                } else if (C == 'p') {
                    PAUSED = !PAUSED;
                } else if (C == 'Q' || C == 'X') {
                    SU.cleanup();
                    System.exit(0);
                } else if (C == 'f') {
                    FLYING = !FLYING;
                    blurb("Flying: " + FLYING);
                } else if (C == 'F') {
                    factorySettings();
                    reset((Canvas3D) e.getComponent());
                } else if (C == '#') {
                    CHORDS = !CHORDS;
                    blurb("Chords: " + CHORDS);
                } else if (C == '!') {
                    JMIDI.silence();
                } else if (C == '%') {
                    HOLD_CHORD = !HOLD_CHORD;
                    blurb("Sustained chords: " + HOLD_CHORD);
                } else if (C == '*') {
                    PITCH_DEBUG = !PITCH_DEBUG;
                    blurb("Pitch Debug: " + PITCH_DEBUG);
                } else if (C == '?') {
                    blurb(FileHelper.EOL + FileHelper.listFile(HELPFILE, FileHelper.EOL));
                } else if (c == 10) {
                    reset((Canvas3D) e.getComponent());
                } else if (C == '`') {
                    FULLSCREEN = !FULLSCREEN;
                    if (FULLSCREEN) remove(StatusTxt); else add(StatusTxt, "East");
                    updateView();
                } else if (c > 49 && c < 58) {
                    NUMBALLS = c - 48;
                    blurb("Balls: " + NUMBALLS);
                    reset((Canvas3D) e.getComponent());
                }
            }
        }
        KeyCmdListener KListener = new KeyCmdListener();
        canvas.addKeyListener(KListener);
        scene.compile();
        SU.addBranchGraph(scene);
    }
