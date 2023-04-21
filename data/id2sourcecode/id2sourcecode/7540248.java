        private void setLocation() {
            int px = ModelVisualiserPanel.this.getLocation().x;
            int py = ModelVisualiserPanel.this.getLocation().y;
            int pw = ModelVisualiserPanel.this.getSize().width;
            int ph = ModelVisualiserPanel.this.getSize().height;
            int w = getSize().width;
            int h = getSize().height;
            int x = px + (pw - w) / 2;
            int y = py + (ph - h) / 2;
            setLocation(500, 720);
        }
