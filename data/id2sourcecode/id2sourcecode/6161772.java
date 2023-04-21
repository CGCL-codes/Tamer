            public void run() {
                int boundsCenterX = bounds.x + (bounds.width / 2);
                int boundsCenterY = bounds.y + (bounds.height / 2);
                Viewport viewport = parent.getViewport();
                Dimension viewportSize = viewport.getSize().getCopy();
                org.eclipse.draw2d.geometry.Point viewportLoc = viewport.getViewLocation().getCopy();
                int viewPortPoint1X = viewportLoc.x;
                int viewPortPoint1Y = viewportLoc.y;
                int viewPortPoint2X = viewportLoc.x + viewportSize.width;
                int viewPortPoint3Y = viewportLoc.y + viewportSize.height;
                int adjustedPortPoint1Y = viewPortPoint1Y + 25;
                int adjustedPortPoint3Y = viewPortPoint3Y - 25;
                if ((boundsCenterX < viewPortPoint1X) || (boundsCenterX > viewPortPoint2X) || (boundsCenterY < adjustedPortPoint1Y) || (boundsCenterY > adjustedPortPoint3Y)) {
                    Dimension contentsSize = viewport.getContents().getSize().getCopy();
                    int dx = 0;
                    int diffX = boundsCenterX - (viewPortPoint1X + (viewportSize.width / 2));
                    if (boundsCenterX < viewPortPoint1X) {
                        dx = -1 * Math.min(viewPortPoint1X, Math.abs(diffX));
                    } else if (boundsCenterX > viewPortPoint2X) {
                        dx = Math.min(contentsSize.width - viewPortPoint2X, Math.abs(diffX));
                    }
                    int dy = 0;
                    int diffY = boundsCenterY - (adjustedPortPoint1Y + (viewportSize.height / 2));
                    if (boundsCenterY < adjustedPortPoint1Y) {
                        dy = -1 * Math.min(viewPortPoint1Y, Math.abs(diffY));
                    } else if (boundsCenterY > adjustedPortPoint3Y) {
                        dy = Math.min(contentsSize.height - viewPortPoint3Y, Math.abs(diffY));
                    }
                    if ((dx == 0) && (dy == 0) && ((contentsSize.height - viewPortPoint3Y) != 0)) {
                        ScreenCapture.createScreenCapture("FigureTester.scrollForClick");
                        String message = MessageFormat.format("Click point ({0}, {1}) was not in viewport, should have to scroll but deltas were ({2}, {3})", new Object[] { boundsCenterX, boundsCenterY, dx, dy });
                        TestCase.assertTrue(message, (dx != 0) || (dy != 0));
                    }
                    viewportLoc.translate(dx, dy);
                    viewport.setViewLocation(viewportLoc);
                    TestCase.assertEquals("viewport was not scrolled the expected amount", viewportLoc, viewport.getViewLocation());
                }
                p[0] = new Point(clickPoint.x - viewportLoc.x, clickPoint.y - viewportLoc.y);
            }
