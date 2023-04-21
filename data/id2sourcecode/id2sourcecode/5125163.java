        public void apply(mxCellState state, mxCellState source, mxCellState target, List points, List result) {
            mxGraphView view = state.getView();
            mxIGraphModel model = view.getGraph().getModel();
            int segment = (int) (mxUtils.getDouble(state.getStyle(), mxConstants.STYLE_STARTSIZE, mxConstants.ENTITY_SEGMENT) * state.view.getScale());
            boolean isSourceLeft = false;
            if (source != null) {
                mxGeometry sourceGeometry = model.getGeometry(source.cell);
                if (sourceGeometry.isRelative()) {
                    isSourceLeft = sourceGeometry.getX() <= 0.5;
                } else if (target != null) {
                    isSourceLeft = target.getX() + target.getWidth() < source.getX();
                }
            } else {
                mxPoint pt = (mxPoint) state.absolutePoints.get(0);
                if (pt == null) {
                    return;
                }
                source = new mxCellState();
                source.setX(pt.getX());
                source.setY(pt.getY());
            }
            boolean isTargetLeft = true;
            if (target != null) {
                mxGeometry targetGeometry = model.getGeometry(target.cell);
                if (targetGeometry.isRelative()) {
                    isTargetLeft = targetGeometry.getX() <= 0.5;
                } else if (source != null) {
                    isTargetLeft = source.getX() + source.getWidth() < target.getX();
                }
            } else {
                List pts = state.absolutePoints;
                mxPoint pt = (mxPoint) pts.get(pts.size() - 1);
                if (pt == null) {
                    return;
                }
                target = new mxCellState();
                target.setX(pt.getX());
                target.setY(pt.getY());
            }
            double x0 = (isSourceLeft) ? source.getX() : source.getX() + source.getWidth();
            double y0 = view.getRoutingCenterY(source);
            double xe = (isTargetLeft) ? target.getX() : target.getX() + target.getWidth();
            double ye = view.getRoutingCenterY(target);
            double seg = segment;
            double dx = (isSourceLeft) ? -seg : seg;
            mxPoint dep = new mxPoint(x0 + dx, y0);
            result.add(dep);
            dx = (isTargetLeft) ? -seg : seg;
            mxPoint arr = new mxPoint(xe + dx, ye);
            if (isSourceLeft == isTargetLeft) {
                double x = (isSourceLeft) ? Math.min(x0, xe) - segment : Math.max(x0, xe) + segment;
                result.add(new mxPoint(x, y0));
                result.add(new mxPoint(x, ye));
            } else if ((dep.getX() < arr.getX()) == isSourceLeft) {
                double midY = y0 + (ye - y0) / 2;
                result.add(new mxPoint(dep.getX(), midY));
                result.add(new mxPoint(arr.getX(), midY));
            }
            result.add(arr);
        }
