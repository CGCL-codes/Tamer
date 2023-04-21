    final void doDraw(Canvas canvas) {
        this.spotAnimation.animate();
        final int spotX = this.position.getVirtualScreenX() - Spot.SPOT_RADIUS;
        final int spotY = this.position.getVirtualScreenY() - Spot.SPOT_DIAMETER + 2;
        final int tale1X = this.tale1XPosition + Spot.SPOT_RADIUS;
        final int tale1Y = this.tale1YPosition + Spot.SPOT_RADIUS;
        final int tale2X = this.tale2XPosition + Spot.SPOT_RADIUS;
        final int tale2Y = this.tale2YPosition + Spot.SPOT_RADIUS;
        final int tale15X = (tale1X + tale2X) / 2;
        final int tale15Y = (tale1Y + tale2Y) / 2;
        canvas.drawCircle(tale2X, tale2Y, Spot.SPOT_RADIUS - 5, this.tale_paint);
        canvas.drawCircle(tale15X, tale15Y, Spot.SPOT_RADIUS - 4, this.tale_paint);
        canvas.drawCircle(tale1X, tale1Y, Spot.SPOT_RADIUS - 3, this.tale_paint);
        this.spotSprite.setPosition(spotX, spotY);
        this.spotSprite.doDraw(canvas);
        this.tale2XPosition = this.tale1XPosition;
        this.tale2YPosition = this.tale1YPosition;
        this.tale1XPosition = spotX;
        this.tale1YPosition = spotY;
    }
