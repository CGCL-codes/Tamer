        void layout(int l, int t, int r, int b, int alignment) {
            this.alignment = alignment;
            final Drawable tabBackground = tab.getBackground();
            final int handleWidth = tabBackground.getIntrinsicWidth();
            final int handleHeight = tabBackground.getIntrinsicHeight();
            final Drawable targetDrawable = target.getDrawable();
            final int targetWidth = targetDrawable.getIntrinsicWidth();
            final int targetHeight = targetDrawable.getIntrinsicHeight();
            final int parentWidth = r - l;
            final int parentHeight = b - t;
            final int leftTarget = (int) (THRESHOLD * parentWidth) - targetWidth + handleWidth / 2;
            final int rightTarget = (int) ((1.0f - THRESHOLD) * parentWidth) - handleWidth / 2;
            final int left = (parentWidth - handleWidth) / 2;
            final int right = left + handleWidth;
            if (alignment == ALIGN_LEFT || alignment == ALIGN_RIGHT) {
                final int targetTop = (parentHeight - targetHeight) / 2;
                final int targetBottom = targetTop + targetHeight;
                final int top = (parentHeight - handleHeight) / 2;
                final int bottom = (parentHeight + handleHeight) / 2;
                if (alignment == ALIGN_LEFT) {
                    tab.layout(0, top, handleWidth, bottom);
                    text.layout(0 - parentWidth, top, 0, bottom);
                    text.setGravity(Gravity.RIGHT);
                    target.layout(leftTarget, targetTop, leftTarget + targetWidth, targetBottom);
                    alignment_value = l;
                } else {
                    tab.layout(parentWidth - handleWidth, top, parentWidth, bottom);
                    text.layout(parentWidth, top, parentWidth + parentWidth, bottom);
                    target.layout(rightTarget, targetTop, rightTarget + targetWidth, targetBottom);
                    text.setGravity(Gravity.TOP);
                    alignment_value = r;
                }
            } else {
                final int targetLeft = (parentWidth - targetWidth) / 2;
                final int targetRight = (parentWidth + targetWidth) / 2;
                final int top = (int) (THRESHOLD * parentHeight) + handleHeight / 2 - targetHeight;
                final int bottom = (int) ((1.0f - THRESHOLD) * parentHeight) - handleHeight / 2;
                if (alignment == ALIGN_TOP) {
                    tab.layout(left, 0, right, handleHeight);
                    text.layout(left, 0 - parentHeight, right, 0);
                    target.layout(targetLeft, top, targetRight, top + targetHeight);
                    alignment_value = t;
                } else {
                    tab.layout(left, parentHeight - handleHeight, right, parentHeight);
                    text.layout(left, parentHeight, right, parentHeight + parentHeight);
                    target.layout(targetLeft, bottom, targetRight, bottom + targetHeight);
                    alignment_value = b;
                }
            }
        }
