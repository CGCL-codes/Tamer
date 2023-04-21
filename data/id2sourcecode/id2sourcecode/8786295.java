        public void draw(Canvas canvas) {
            canvas.save();
            float dx = (maxX + minX) / 2;
            float dy = (maxY + minY) / 2;
            drawable.setBounds((int) minX, (int) minY, (int) maxX, (int) maxY);
            canvas.translate(dx, dy);
            canvas.rotate(angle * 180.0f / (float) Math.PI);
            canvas.translate(-dx, -dy);
            drawable.draw(canvas);
            canvas.restore();
        }
