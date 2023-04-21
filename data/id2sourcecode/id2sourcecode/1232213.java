        public Paragraph(String text, Font font) {
            URL url = resolve(font.getName());
            if (url == null) {
                this.font = font;
            } else {
                this.font = FontCache.getFont(url).deriveFont(font.getStyle(), font.getSize());
            }
            int maxWidth = getWidth();
            int maxHeight = getHeight();
            FontMetrics metrics = view.getContainer().getHost().getFontMetrics(this.font);
            this.ascent = metrics.getAscent();
            this.descent = metrics.getDescent();
            this.leading = metrics.getLeading();
            char[] chars = text.toCharArray();
            int start = 0;
            int end = chars.length;
            while (start != end) {
                int length = end - start;
                int lineBreak = text.indexOf("  ", start);
                if (lineBreak != -1) {
                    length = lineBreak - start;
                }
                if (maxWidth > 0) {
                    while (metrics.charsWidth(chars, start, length) > maxWidth) {
                        int wordBreak = text.lastIndexOf(' ', start + length - 1);
                        if (length > 1) {
                            wordBreak = Math.max(wordBreak, text.lastIndexOf('-', start + length - 2) + 1);
                        }
                        if (wordBreak <= start) {
                            while (metrics.charsWidth(chars, start, length) > maxWidth) {
                                length--;
                                if (length == 0) {
                                    return;
                                }
                            }
                        } else {
                            length = wordBreak - start;
                        }
                    }
                }
                int newHeight = height + ascent + descent;
                if (lines.size() > 0) {
                    newHeight += leading;
                }
                if (maxHeight > 0) {
                    if (newHeight > maxHeight) {
                        return;
                    }
                }
                Line line = new Line(chars, start, length, metrics);
                lines.add(line);
                width = Math.max(width, line.width);
                height = newHeight;
                start = start + length;
                while (start < end && chars[start] == ' ') {
                    start++;
                }
            }
        }
