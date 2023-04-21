    private ClassShape createClassShape(String className) {
        if (!classShapesMap.containsKey(className)) {
            ClassShape classShape = new ClassShape();
            int x = getNewClassShapeX();
            classShape.setX(x);
            classShape.setY(TOP_PADDING);
            int templateStringHeight = intCeil(templateStringBounds.getHeight());
            classShape.setCaptionHeight(templateStringHeight + 2 * CLASS_VERTICAL_PADDING + 2 * CLASS_BORDER_WIDTH);
            Rectangle classNameStringBounds = getTextBounds(className);
            int captionWidth = intCeil(classNameStringBounds.getWidth());
            int captionHeight = intCeil(classNameStringBounds.getHeight());
            int classVerticalPadding;
            classVerticalPadding = CLASS_VERTICAL_PADDING + (templateStringHeight - captionHeight) / 2;
            classShape.setCaptionVerticalPadding(classVerticalPadding);
            classShape.setCaptionHorizontalPadding(CLASS_HORIZONTAL_PADDING);
            classShape.setWidth(captionWidth + 2 * CLASS_HORIZONTAL_PADDING + 2 * CLASS_BORDER_WIDTH);
            classShape.setHeight(classShape.getCaptionHeight() + CLASS_VERTICAL_MARGIN);
            classShape.setClassName(className);
            classShapes.add(classShape);
            classShapesMap.put(className, classShape);
        }
        return classShapesMap.get(className);
    }
