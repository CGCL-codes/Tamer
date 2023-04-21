    private void init(ViewDefinition viewDefinition) {
        rotoscope = MainFrame.getInstance().getModel().getRotoscope(viewDefinition.getView());
        if (rotoscope == null) return;
        float viewTranslateX = viewDefinition.getTranslateX();
        float viewTranslateY = viewDefinition.getTranslateY();
        float viewWidth = viewDefinition.getWidth();
        float viewHeight = viewDefinition.getHeight();
        float viewScale = viewDefinition.getScale() * viewWidth * 0.5f;
        float scale = rotoscope.getScale() * viewScale;
        float xPos = rotoscope.getXPosition() - rotoscope.getScale() * 0.5f * rotoscope.getPixelWidth();
        float yPos = rotoscope.getYPosition() + rotoscope.getScale() * 0.5f * rotoscope.getPixelHeight();
        iWidth = (int) (scale * rotoscope.getPixelWidth());
        iHeight = (int) (scale * rotoscope.getPixelHeight());
        iLeftX = (int) (viewWidth * 0.5f + (viewTranslateX + xPos) * viewScale);
        iRightX = (int) (iLeftX + iWidth);
        iTopY = (int) (viewHeight * 0.5f - (viewTranslateY + yPos) * viewScale);
        iBottomY = (int) (iTopY + iHeight);
        iCenterX = (iLeftX + iRightX) / 2;
        iCenterY = (iTopY + iBottomY) / 2;
    }
