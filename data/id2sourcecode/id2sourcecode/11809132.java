    public Paint getPaint(double value) {
        if (value < this.lowerBound) {
            return this.defaultPaint;
        }
        if (value > this.upperBound) {
            return this.defaultPaint;
        }
        int count = this.lookupTable.size();
        if (count == 0) {
            return this.defaultPaint;
        }
        PaintItem item = (PaintItem) this.lookupTable.get(0);
        if (value < item.value) {
            return this.defaultPaint;
        }
        int low = 0;
        int high = this.lookupTable.size() - 1;
        while (high - low > 1) {
            int current = (low + high) / 2;
            item = (PaintItem) this.lookupTable.get(current);
            if (value >= item.value) {
                low = current;
            } else {
                high = current;
            }
        }
        if (high > low) {
            item = (PaintItem) this.lookupTable.get(high);
            if (value < item.value) {
                item = (PaintItem) this.lookupTable.get(low);
            }
        }
        return (item != null ? item.paint : this.defaultPaint);
    }
