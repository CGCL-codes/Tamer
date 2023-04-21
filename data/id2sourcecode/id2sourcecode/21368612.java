    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < lGroupBalloons.size(); i++) {
            GroupBalloons balloons = lGroupBalloons.get(i);
            balloons.onDraw(canvas, paint);
            if (balloons.isDie()) {
                balloons.update();
            }
        }
        for (int i = 0; i < lBullets.size(); i++) {
            Bullet bullet = lBullets.get(i);
            bullet.onDraw(canvas, paint);
        }
        for (int i = 0; i < lBullets.size(); i++) {
            Bullet bullet = lBullets.get(i);
            if (bullet.isDie()) {
                lBullets.remove(bullet);
            }
        }
        for (int i = 0; i < number_bullet; i++) {
            int x = (i) * bmBullet.getWidth() + 5;
            int y = getHeight() - 10 - bmBullet.getHeight();
            canvas.drawBitmap(bmBullet, x, y, paint);
        }
        for (int i = 0; i < lScore.size(); i++) {
            Score score = lScore.get(i);
            Point point = score.getPoint();
            canvas.drawText(score.getScore() + "", point.x, point.y, paint);
        }
        for (int i = 0; i < lScore.size(); i++) {
            Score score = lScore.get(i);
            if (score.isDie()) {
                lScore.remove(score);
            }
        }
        if (lGroupBalloons.size() == 0) {
            int number = 5;
            if (CommonDeviceId.isTablet((Activity) getContext())) {
                number = 8;
            }
            GroupBalloons groupBalloons = new GroupBalloons(getContext(), 1, 100, 200);
            int y = getHeight() - 10 - bmBullet.getHeight() - groupBalloons.getMinHeight() / 2;
            int x = groupBalloons.getMaxWidth() / 2 + 10;
            int space = groupBalloons.getMaxWidth() + 10;
            int spaceRight = getWidth() - (x + space * (number - 1));
            x = x + (spaceRight - x) / 2;
            for (int i = 0; i < number; i++) {
                lGroupBalloons.add(createGroupBalloons(x + space * (i), y));
            }
        }
        invalidate();
    }
