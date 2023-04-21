    protected TGImage getBuffer() {
        if (this.clientArea != null) {
            this.bufferDisposer.update(this.clientArea.width, this.clientArea.height);
            if (this.buffer == null || this.buffer.isDisposed()) {
                String[] names = null;
                TGMeasure measure = getMeasure();
                this.maxNote = 0;
                this.minNote = 127;
                if (TuxGuitar.instance().getSongManager().isPercussionChannel(measure.getTrack().getChannelId())) {
                    names = new String[PERCUSSIONS.length];
                    for (int i = 0; i < names.length; i++) {
                        this.minNote = Math.min(this.minNote, PERCUSSIONS[i].getValue());
                        this.maxNote = Math.max(this.maxNote, PERCUSSIONS[i].getValue());
                        names[i] = PERCUSSIONS[names.length - i - 1].getName();
                    }
                } else {
                    for (int sNumber = 1; sNumber <= measure.getTrack().stringCount(); sNumber++) {
                        TGString string = measure.getTrack().getString(sNumber);
                        this.minNote = Math.min(this.minNote, string.getValue());
                        this.maxNote = Math.max(this.maxNote, (string.getValue() + 20));
                    }
                    names = new String[this.maxNote - this.minNote + 1];
                    for (int i = 0; i < names.length; i++) {
                        names[i] = (NOTE_NAMES[(this.maxNote - i) % 12] + ((this.maxNote - i) / 12));
                    }
                }
                int minimumNameWidth = 110;
                int minimumNameHeight = 0;
                TGPainter painter = new TGPainterImpl(new GC(this.dialog.getDisplay()));
                painter.setFont(new TGFontImpl(this.config.getFont()));
                for (int i = 0; i < names.length; i++) {
                    int fmWidth = painter.getFMWidth(names[i]);
                    if (fmWidth > minimumNameWidth) {
                        minimumNameWidth = fmWidth;
                    }
                    int fmHeight = painter.getFMHeight();
                    if (fmHeight > minimumNameHeight) {
                        minimumNameHeight = fmHeight;
                    }
                }
                painter.dispose();
                int cols = measure.getTimeSignature().getNumerator();
                int rows = (this.maxNote - this.minNote);
                this.leftSpacing = minimumNameWidth + 10;
                this.lineHeight = Math.max(minimumNameHeight, ((this.clientArea.height - (BORDER_HEIGHT * 2.0f)) / (rows + 1.0f)));
                this.timeWidth = Math.max((10 * (TGDuration.SIXTY_FOURTH / measure.getTimeSignature().getDenominator().getValue())), ((this.clientArea.width - this.leftSpacing) / cols));
                this.bufferWidth = this.leftSpacing + (this.timeWidth * cols);
                this.bufferHeight = (this.lineHeight * (rows + 1));
                this.buffer = new TGImageImpl(this.editor.getDisplay(), Math.round(this.bufferWidth), Math.round(this.bufferHeight));
                painter = this.buffer.createPainter();
                painter.setFont(new TGFontImpl(this.config.getFont()));
                painter.setForeground(new TGColorImpl(this.config.getColorForeground()));
                for (int i = 0; i <= rows; i++) {
                    painter.setBackground(new TGColorImpl(this.config.getColorLine(i % 2)));
                    painter.initPath(TGPainter.PATH_FILL);
                    painter.setAntialias(false);
                    painter.addRectangle(0, (i * this.lineHeight), this.bufferWidth, this.lineHeight);
                    painter.closePath();
                    painter.drawString(names[i], 5, (Math.round((i * this.lineHeight)) + Math.round((this.lineHeight - minimumNameHeight) / 2)));
                }
                for (int i = 0; i < cols; i++) {
                    float colX = this.leftSpacing + (i * this.timeWidth);
                    float divisionWidth = (this.timeWidth / this.grids);
                    for (int j = 0; j < this.grids; j++) {
                        if (j == 0) {
                            painter.setLineStyleSolid();
                        } else {
                            painter.setLineStyleDot();
                        }
                        painter.initPath();
                        painter.setAntialias(false);
                        painter.moveTo(Math.round(colX + (j * divisionWidth)), 0);
                        painter.lineTo(Math.round(colX + (j * divisionWidth)), this.bufferHeight);
                        painter.closePath();
                    }
                }
                painter.dispose();
            }
        }
        return this.buffer;
    }
