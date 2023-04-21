    public void encode(FSCoder coder) {
        boolean _containsFont = containsFont();
        boolean _containsColor = containsColor();
        boolean _containsMaxLength = containsMaxLength();
        boolean _containsText = containsText();
        super.encode(coder);
        coder.context[FSCoder.TransparentColors] = 1;
        bounds.encode(coder);
        coder.writeBits(_containsText ? 1 : 0, 1);
        coder.writeBits(wordWrapped ? 1 : 0, 1);
        coder.writeBits(multiline ? 1 : 0, 1);
        coder.writeBits(password ? 1 : 0, 1);
        coder.writeBits(readOnly ? 1 : 0, 1);
        coder.writeBits(_containsColor ? 1 : 0, 1);
        coder.writeBits(_containsMaxLength ? 1 : 0, 1);
        coder.writeBits(_containsFont ? 1 : 0, 1);
        coder.writeBits(0, 1);
        coder.writeBits(autoSize ? 1 : 0, 1);
        coder.writeBits(containsLayoutInfo() ? 1 : 0, 1);
        coder.writeBits(selectable ? 1 : 0, 1);
        coder.writeBits(bordered ? 1 : 0, 1);
        coder.writeBits(0, 1);
        coder.writeBits(html ? 1 : 0, 1);
        coder.writeBits(useFontGlyphs ? 1 : 0, 1);
        if (_containsFont) {
            coder.writeWord(fontIdentifier, 2);
            coder.writeWord(fontHeight, 2);
        }
        if (_containsColor) color.encode(coder);
        if (_containsMaxLength) coder.writeWord(maxLength, 2);
        if (containsLayoutInfo()) {
            coder.writeWord((alignment != Transform.VALUE_NOT_SET) ? alignment : 0, 1);
            coder.writeWord((leftMargin != Transform.VALUE_NOT_SET) ? leftMargin : 0, 2);
            coder.writeWord((rightMargin != Transform.VALUE_NOT_SET) ? rightMargin : 0, 2);
            coder.writeWord((indent != Transform.VALUE_NOT_SET) ? indent : 0, 2);
            coder.writeWord((leading != Transform.VALUE_NOT_SET) ? leading : 0, 2);
        }
        coder.writeString(variableName);
        coder.writeWord(0, 1);
        if (_containsText) {
            coder.writeString(initialText);
            coder.writeWord(0, 1);
        }
        coder.context[FSCoder.TransparentColors] = 0;
        coder.endObject(name());
    }
