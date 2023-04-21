    public void mouseClickedDown(Point2d worldCoord) {
        logger.debug("rotate mouseClickedDown, initializing rotation");
        rotationCenter = null;
        selection = super.chemModelRelay.getRenderer().getRenderer2DModel().getSelection();
        if (selection == null || !selection.isFilled() || selection.getConnectedAtomContainer() == null || selection.getConnectedAtomContainer().getAtomCount() == 0) {
            logger.debug("Nothing selected for rotation");
            selectionMade = false;
            return;
        } else {
            rotationPerformed = false;
            Rectangle2D bounds = BoundsCalculator.calculateBounds(this.chemModelRelay.getRenderer().getRenderer2DModel().getSelection().getConnectedAtomContainer());
            rotationAngle = 0.0;
            selectionMade = true;
            atomCoordsMap = new HashMap<IAtom, Point2d[]>();
            for (IAtom atom : selection.getConnectedAtomContainer().atoms()) {
                Point2d[] coordsforatom = new Point2d[2];
                coordsforatom[1] = atom.getPoint2d();
                atomCoordsMap.put(atom, coordsforatom);
            }
            IAtomContainer selectedAtoms = selection.getConnectedAtomContainer();
            Double upperX = null, lowerX = null, upperY = null, lowerY = null;
            for (int i = 0; i < selectedAtoms.getAtomCount(); i++) {
                if (upperX == null) {
                    upperX = selectedAtoms.getAtom(i).getPoint2d().x;
                    lowerX = upperX;
                    upperY = selectedAtoms.getAtom(i).getPoint2d().y;
                    lowerY = selectedAtoms.getAtom(i).getPoint2d().y;
                } else {
                    double currX = selectedAtoms.getAtom(i).getPoint2d().x;
                    if (currX > upperX) upperX = currX;
                    if (currX < lowerX) lowerX = currX;
                    double currY = selectedAtoms.getAtom(i).getPoint2d().y;
                    if (currY > upperY) upperY = currY;
                    if (currY < lowerY) lowerY = currY;
                }
            }
            rotationCenter = new Point2d();
            rotationCenter.x = (upperX + lowerX) / 2;
            rotationCenter.y = (upperY + lowerY) / 2;
            logger.debug("rotationCenter " + rotationCenter.x + " " + rotationCenter.y);
            startCoordsRelativeToRotationCenter = new Point2d[selectedAtoms.getAtomCount()];
            for (int i = 0; i < selectedAtoms.getAtomCount(); i++) {
                Point2d relativeAtomPosition = new Point2d();
                relativeAtomPosition.x = selectedAtoms.getAtom(i).getPoint2d().x - rotationCenter.x;
                relativeAtomPosition.y = selectedAtoms.getAtom(i).getPoint2d().y - rotationCenter.y;
                startCoordsRelativeToRotationCenter[i] = relativeAtomPosition;
            }
        }
    }
