    public BranchOfSimulativeTest() {
        worldAABB = new AABB();
        worldAABB.lowerBound.set(-100, -100);
        worldAABB.upperBound.set(1500, 1000);
        debugDraw = new AngryPigDebugDraw(WIDTH, HEIGHT, 1.0f);
        mWorld = new World(worldAABB, new Vec2(0f, 40f), true);
        pig1 = new Pig();
        pig1.getSprite().setCenterX(ORIBALLPOSTION.x);
        pig1.getSprite().setCenterY(ORIBALLPOSTION.y);
        birds = new Bird[5];
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
        CircleWood2 = new CircleBaseElement(ResourceManager.getMiddleWoodBallImages());
        CircleWood2.create(mWorld, 711, 570, 2f, 0.8f, 0.2f, 10f);
        BattenWood3 = new RectBaseElement(ResourceManager.getShortWoodBattenImages());
        BattenWood3.create(mWorld, 900, 575, MathUtils.PI / 2);
        BattenWood4 = new RectBaseElement(ResourceManager.getBombImage());
        BattenWood4.getSprite().setUpdateTime(80);
        BattenWood4.getSprite().setActive(true);
        BattenWood4.create(mWorld, 930, 575, MathUtils.PI / 2);
        BattenWood5 = new RectBaseElement(ResourceManager.getShortWoodBattenImages());
        BattenWood5.create(mWorld, 915, 550);
        RectWood6 = new RectBaseElement(ResourceManager.getMiddleWoodRectangleImages());
        RectWood6.create(mWorld, 925, 540);
        BattenWood7 = new RectBaseElement(ResourceManager.getSmallWoodSquareImages());
        BattenWood7.create(mWorld, 930, 530);
        CircleWood8 = new CircleBaseElement(ResourceManager.getLargeWoodBallImages());
        CircleWood8.create(mWorld, 932, 510, 2.0f, 0.8f, 0.2f, 18f);
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
