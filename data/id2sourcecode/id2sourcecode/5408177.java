    private void MouseActions(Point p) {
        if (Editor.drawMode == Editor.DRAW && Editor.selectedObject != null) {
            if (mouseClickListener.getButton() == 1) {
                Point p16 = new Point(p.x / 16, p.y / 16);
                if (!Editor.selectedObject.name.equals("WorldTile")) {
                    object[getObjectNumberAt(p16)] = new EditorObject(Editor.selectedObject, p16);
                } else {
                    object[getObjectNumberAt(p16)] = new EditorObject(Editor.selectedObject, p16, Integer.parseInt("" + Editor.selectedObject.objectChar));
                }
            }
        }
        if (Toolbox.ToolboxTab.getSelectedIndex() == 0 && Editor.drawMode == Editor.SELECT) {
            if (mouseClickListener.getButtonState(1) == true) {
                if (object[getObjectNumberAt(new Point(p.x / 16, p.y / 16))] != null) {
                    if (object[getObjectNumberAt(new Point(p.x / 16, p.y / 16))].selected == true) {
                        clickingSelection = true;
                    }
                }
                if (clickingSelection == false) {
                    if (mouseClickListener.initialClick.x >= p.x && mouseClickListener.initialClick.y >= p.y) selectionRect = new Rectangle(p.x, p.y, mouseClickListener.initialClick.x - p.x, Math.abs(mouseClickListener.initialClick.y - p.y));
                    if (mouseClickListener.initialClick.x >= p.x && mouseClickListener.initialClick.y < p.y) selectionRect = new Rectangle(p.x, mouseClickListener.initialClick.y, mouseClickListener.initialClick.x - p.x, Math.abs(p.y - mouseClickListener.initialClick.y));
                    if (mouseClickListener.initialClick.x < p.x && mouseClickListener.initialClick.y >= p.y) selectionRect = new Rectangle(mouseClickListener.initialClick.x, p.y, p.x - mouseClickListener.initialClick.x, Math.abs(mouseClickListener.initialClick.y - p.y));
                    if (mouseClickListener.initialClick.x < p.x && mouseClickListener.initialClick.y < p.y) selectionRect = new Rectangle(mouseClickListener.initialClick.x, mouseClickListener.initialClick.y, p.x - mouseClickListener.initialClick.x, Math.abs(p.y - mouseClickListener.initialClick.y));
                } else {
                    moving = true;
                    int a = 0;
                    while (object[a] != null) {
                        if (object[a].selected) {
                            object[a].position = new Point(objectTemp[a].position.x + p.x / 16 - mouseClickListener.initialClick.x / 16, objectTemp[a].position.y - mouseClickListener.initialClick.y / 16 + p.y / 16);
                        }
                        a++;
                    }
                }
            } else {
                if (selectionRect != null) {
                    selectedPoint = new Point(selectionRect.x / 16, selectionRect.y / 16);
                    selectionObjects = new EditorObject[99999];
                    int a = 0;
                    while (object[a] != null) {
                        object[a].selected = false;
                        a++;
                    }
                    a = 0;
                    for (int x = selectionRect.x / 16; x < selectionRect.x / 16 + selectionRect.width / 16 + 1; x++) {
                        for (int y = selectionRect.y / 16; y < selectionRect.y / 16 + selectionRect.height / 16 + 1; y++) {
                            int n = getObjectNumberAt(new Point(x, y));
                            if (object[n] != null) {
                                selectionObjects[a] = object[n];
                                object[n].selected = true;
                                a++;
                            }
                        }
                    }
                }
                objectTemp = new EditorObject[99999];
                int a = 0;
                while (object[a] != null) {
                    objectTemp[a] = new EditorObject(new GameObject(object[a].name, object[a].objectChar), object[a].position);
                    a++;
                }
                selectionRect = null;
                clickingSelection = false;
            }
        }
        if (Toolbox.ToolboxTab.getSelectedIndex() == 1 && Editor.drawMode == Editor.SELECT) {
            if (mouseClickListener.getButtonState(1) == true) {
                if (new Rectangle(0, Editor.cameraPrefHeight - Editor.cameraTolerance / 2, maxWidth, Editor.cameraTolerance).contains(p)) {
                    Editor.cameraPrefHeight = Integer.parseInt(Toolbox.camPrefHeightSpinner.getValue().toString()) + p.y - mouseClickListener.initialClick.y;
                    update = true;
                }
            } else {
                if (update == true) {
                    Toolbox.camPrefHeightSpinner.setValue(Editor.cameraPrefHeight);
                    update = false;
                }
            }
        }
        if (Editor.drawMode == Editor.ERASE) {
            if (mouseClickListener.getButton() == 1) {
                Point p16 = new Point(p.x / 16, p.y / 16);
                int objectNumber = getNumberOfObjects();
                for (int i = getObjectNumberAt(p16); i < objectNumber; i++) {
                    object[i] = object[i + 1];
                }
                object[objectNumber] = null;
            }
        }
    }
