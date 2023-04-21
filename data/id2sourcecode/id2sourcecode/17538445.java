        @Override
        protected List<Point> getNewPositionFor(final List<Widget> _widgetToMove) {
            int minx = _widgetToMove.get(0).convertLocalToScene(_widgetToMove.get(0).getBounds()).y;
            for (final Widget widget : _widgetToMove) {
                minx = Math.min(minx, widget.convertLocalToScene(widget.getBounds()).y);
            }
            int maxx = _widgetToMove.get(0).convertLocalToScene(_widgetToMove.get(0).getBounds()).y + _widgetToMove.get(0).convertLocalToScene(_widgetToMove.get(0).getBounds()).height;
            for (final Widget widget : _widgetToMove) {
                maxx = Math.max(maxx, widget.convertLocalToScene(widget.getBounds()).y + widget.convertLocalToScene(widget.getBounds()).height);
            }
            final List<Point> newPoints = new ArrayList<Point>(_widgetToMove.size());
            for (final Widget widget : _widgetToMove) {
                final Point p = widget.getPreferredLocation();
                final int oldY = p.x;
                p.y = (maxx + minx) / 2;
                widget.getParentWidget().convertSceneToLocal(p);
                p.y = p.y - widget.getBounds().height / 2 - widget.getBounds().y;
                p.x = oldY;
                newPoints.add(p);
            }
            return newPoints;
        }
