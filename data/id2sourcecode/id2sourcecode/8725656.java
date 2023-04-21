    public void captureImage() {
        Rectangle posRect = new Rectangle(main.getSettings().getPosRectangle());
        Rectangle heathRect = main.getSettings().getHealthManaRectangle();
        Rectangle targetRect = new Rectangle(main.getSettings().getTargetNameRectangle());
        posRect.y -= TempSettings.getPosTextCalibrationOffeset();
        posRect.height = 10 + 2 * TempSettings.getPosTextCalibrationOffeset();
        targetRect.y -= TempSettings.getTargetTextCalibrationOffeset();
        targetRect.height = 10 + 2 * TempSettings.getTargetTextCalibrationOffeset();
        robot.delay(100);
        posImage = robot.createScreenCapture(posRect);
        informPictureChangedListener(PictureChangedListener.IMAGE_POSITION);
        if (main.getSettings().isCaptureManaHealthEnabled()) {
            robot.delay(100);
            healthManaImage = robot.createScreenCapture(heathRect);
            informPictureChangedListener(PictureChangedListener.IMAGE_MANA_HEALTH);
        }
        if (main.getSettings().isCaptureTargetEnabled()) {
            robot.delay(100);
            targetNameImage = robot.createScreenCapture(targetRect);
            informPictureChangedListener(PictureChangedListener.IMAGE_TARGET_NAME);
        }
        informPictureChangedListener(PictureChangedListener.IMAGE_ALL_UPDATED);
    }
