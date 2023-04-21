        @Override
        protected List<Point> getNewPositionFor(final List<Widget> _widgetToMove) {
            int minx = _widgetToMove.get(0).convertLocalToScene(_widgetToMove.get(0).getBounds()).x;
            for (final Widget widget : _widgetToMove) {
                minx = Math.min(minx, widget.convertLocalToScene(widget.getBounds()).x);
            }
            int maxx = _widgetToMove.get(0).convertLocalToScene(_widgetToMove.get(0).getBounds()).x + _widgetToMove.get(0).convertLocalToScene(_widgetToMove.get(0).getBounds()).width;
            for (final Widget widget : _widgetToMove) {
                maxx = Math.max(maxx, widget.convertLocalToScene(widget.getBounds()).x + widget.convertLocalToScene(widget.getBounds()).width);
            }
            final List<Point> newPoints = new ArrayList<Point>(_widgetToMove.size());
            for (final Widget widget : _widgetToMove) {
                final Point p = widget.getPreferredLocation();
                final int oldY = p.y;
                p.x = (maxx + minx) / 2;
                widget.getParentWidget().convertSceneToLocal(p);
                p.x = p.x - widget.getBounds().width / 2 - widget.getBounds().x;
                p.y = oldY;
                newPoints.add(p);
            }
            return newPoints;
        }
