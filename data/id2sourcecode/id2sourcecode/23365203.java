    public MainTest() {
        worldAABB = new AABB();
        worldAABB.lowerBound.set(-100, -100);
        worldAABB.upperBound.set(1500, 1000);
        debugDraw = new AngryPigDebugDraw(WIDTH, HEIGHT, 1.0f);
        mWorld = new World(worldAABB, new Vec2(0f, 40f), true);
        mWorld.setContactListener(new ConcreteContactListener());
        pigs = new Pig[PIG_TOTAL_COUNT];
        movingCloudSprite = new Sprite(ResourceManager.getMovingCloudImage());
        for (int i = 0; i < pigs.length; ++i) {
            pigs[i] = new Pig();
            pigs[i].setScore(5000);
        }
        pigs[0].getSprite().setCenterX(ORIBALLPOSTION.x);
        pigs[0].getSprite().setCenterY(ORIBALLPOSTION.y);
        pigs[1].getSprite().setCenterX(150);
        pigs[1].getSprite().setCenterY(590);
        pigs[2].getSprite().setCenterX(100);
        pigs[2].getSprite().setCenterY(590);
        birds = new Bird[BIRD_TOTAL_COUNT];
        birds[0] = new Bird();
        birds[0].createBird(mWorld, 770, GROUND_HEIGHT - birds[0].getSprite().getHeight() / 2, 2f, 0.8f, 0.2f);
        birds[1] = new Bird();
        birds[1].createBird(mWorld, 800, GROUND_HEIGHT - birds[0].getSprite().getHeight() / 2, 2f, 0.8f, 0.2f);
        birds[2] = new Bird();
        birds[2].createBird(mWorld, 830, GROUND_HEIGHT - birds[0].getSprite().getHeight() / 2, 2f, 0.8f, 0.2f);
        birds[3] = new Bird();
        birds[3].createBird(mWorld, 860, GROUND_HEIGHT - birds[0].getSprite().getHeight() / 2, 2f, 0.8f, 0.2f);
        birds[4] = new Bird();
        birds[4].createBird(mWorld, 890, GROUND_HEIGHT - birds[0].getSprite().getHeight() / 2, 2f, 0.8f, 0.2f);
        RectWood1 = new RectBaseElement(ResourceManager.getLargeWoodRectangleImages());
        RectWood1.create(mWorld, 700, 580);
        RectWood1.setScore(500);
        CircleWood2 = new CircleBaseElement(ResourceManager.getMiddleWoodBallImages());
        CircleWood2.create(mWorld, 720, 570, 2f, 0.8f, 0.2f, 10f);
        CircleWood2.setScore(300);
        BattenWood3 = new RectBaseElement(ResourceManager.getShortWoodBattenImages());
        BattenWood3.create(mWorld, 900, 575, MathUtils.PI / 2);
        BattenWood3.setScore(100);
        BattenWood4 = new RectBaseElement(ResourceManager.getShortWoodBattenImages());
        BattenWood4.create(mWorld, 930, 575, MathUtils.PI / 2);
        BattenWood4.setScore(100);
        BattenWood5 = new RectBaseElement(ResourceManager.getShortWoodBattenImages());
        BattenWood5.create(mWorld, 915, 550);
        BattenWood5.setScore(100);
        RectWood6 = new RectBaseElement(ResourceManager.getMiddleWoodRectangleImages());
        RectWood6.create(mWorld, 925, 540);
        RectWood6.setScore(300);
        CircleWood7 = new CircleBaseElement(ResourceManager.getLargeWoodBallImages());
        CircleWood7.create(mWorld, 935, 520, 2.0f, 0.8f, 0.2f, 18f);
        CircleWood7.setScore(500);
        new Ground(mWorld, 0, GROUND_HEIGHT, WIDTH, HEIGHT);
        nanos = new long[fpsAverageCount];
        long nanosPerFrameGuess = (long) (1000000000.0 / targetFPS);
        nanos[fpsAverageCount - 1] = System.nanoTime();
        for (int i = fpsAverageCount - 2; i >= 0; --i) {
            nanos[i] = nanos[i + 1] - nanosPerFrameGuess;
        }
        nanoStart = System.nanoTime();
        frameRatePeriod = (long) (1000000000.0 / targetFPS);
        add(panel);
    }
