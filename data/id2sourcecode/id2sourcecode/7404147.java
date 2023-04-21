	private void resetCache() {
		int cs = chartSize.get();
		int itemCount = model.getRowCount();
		int startAngle = 0;
		itemCache = new CacheInfo[itemCount];
		for (int index = 0; index < itemCount; index++) {
			CacheInfo cacheInfo = new CacheInfo();
			itemCache[index] = cacheInfo;
			ChartModel.Item i = model.getRowAt(index);
			cacheInfo.text = model.formatText(textFormat.get(), i, totalNumber);
			cacheInfo.arcAngle = (int)((float)(i.number * 360) / totalNumber);
			// HACK: fix last item
			if (index == itemCount - 1)
				cacheInfo.arcAngle = 360 - startAngle;
			
			double d = Math.PI * -startAngle / 180;
			cacheInfo.cos = Math.cos(d);
			cacheInfo.sin = Math.sin(d);

			d = Math.PI * -(startAngle + ((startAngle + cacheInfo.arcAngle) - startAngle) / 2) / 180;
			cacheInfo.textCos = Math.cos(d);
			cacheInfo.textSin = Math.sin(d);
			int l = (cs + outlineWidth) / 2;
			cacheInfo.labelStart = new Point(
				(int)(midX + cacheInfo.textCos * l),
				(int)(midY + cacheInfo.textSin * l)
			);
			cacheInfo.startAngle = startAngle;
			startAngle += cacheInfo.arcAngle;
		}
	}
