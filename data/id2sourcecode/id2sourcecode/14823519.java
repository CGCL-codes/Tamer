		private void getColorFromScreen() {
			if (!enabled.isSelected())
				return;

			createRobot();

			if (bender == null)
				return;

			PointerInfo pointerInfo = MouseInfo.getPointerInfo();
			Point location = pointerInfo.getLocation();

			if (location.equals(lastMouseLocation))
				return;

			lastMouseLocation.setLocation(location);

			color = bender.getPixelColor(location.x, location.y);

			int size = 30;
			Dimension screenSize = UI.getScreenSize();
			int centerX = location.x - (size / 2);
			int centerY = location.y - (size / 2);
			Rectangle r = new Rectangle(centerX, centerY, size, size);

			// do not capture "offscreen" image
			if (r.x < 0)
				r.x = 0;
			else if (r.x + size > screenSize.width)
				r.x = screenSize.width - size;
			if (r.y < 0)
				r.y = 0;
			else if (r.y + size > screenSize.height)
				r.y = screenSize.height - size;

			BufferedImage screenshot = bender.createScreenCapture(r);

			int zoom = 5;
			int zoomSize = size * zoom;
			BufferedImage icon = UI.createCompatibleImage(zoomSize, zoomSize, false);
			Graphics2D g = icon.createGraphics();
			g.setColor(Color.PINK);
			g.fillRect(0, 0, zoomSize, zoomSize);

			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g.drawImage(
				screenshot,
				(r.x - centerX) * zoom,
				(r.y - centerY) * zoom,
				zoomSize,
				zoomSize,
				null
			);

			g.setColor(Color.RED);
			g.drawRect(zoomSize / 2, zoomSize / 2, zoom, zoom);

			g.dispose();
			imageLabel.setImage(icon);

			updateChooser();
		}
