    public void doubleClickPaletteItem(IUIContext ui, FigureCanvas rootCanvas, IWidgetLocator locator, String categoryLabel, String item) {
        TestCase.assertNotNull(ui);
        TestCase.assertNotNull(rootCanvas);
        TestCase.assertNotNull(categoryLabel);
        TestCase.assertNotNull(item);
        Draw2DActivator.logDebug("Searching for palette item: " + item + " in category: " + categoryLabel);
        IFigure paletteItem = null;
        try {
            paletteItem = findPaletteItemForCategory(ui, rootCanvas, categoryLabel, item);
        } catch (FigureNotFoundException fnfe) {
            ScreenCapture.createScreenCapture("doubleClickPaletteItem_" + categoryLabel);
            Draw2DActivator.logException(fnfe);
            TestCase.fail(fnfe.getLocalizedMessage());
        } catch (MultipleFiguresFoundException mffe) {
            ScreenCapture.createScreenCapture("doubleClickPaletteItem_" + categoryLabel);
            Draw2DActivator.logException(mffe);
            TestCase.fail(mffe.getLocalizedMessage());
        }
        Draw2DActivator.logDebug("Found palette item: " + item);
        FigureTester figureTester = new FigureTester();
        figureTester.doubleClickFigure(ui, rootCanvas, locator, paletteItem);
    }
