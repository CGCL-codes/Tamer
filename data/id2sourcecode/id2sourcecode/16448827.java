    protected void writeData(OutputBitStream outStream) throws IOException {
        outStream.writeUI16(characterId);
        bounds.write(outStream);
        outStream.writeBooleanBit(hasText);
        outStream.writeBooleanBit(wordWrap);
        outStream.writeBooleanBit(multiline);
        outStream.writeBooleanBit(password);
        outStream.writeBooleanBit(readOnly);
        outStream.writeBooleanBit(hasTextColor);
        outStream.writeBooleanBit(hasMaxLength);
        outStream.writeBooleanBit(hasFont);
        outStream.writeUnsignedBits(0, 1);
        outStream.writeBooleanBit(autoSize);
        outStream.writeBooleanBit(hasLayout);
        outStream.writeBooleanBit(noSelect);
        outStream.writeBooleanBit(border);
        outStream.writeUnsignedBits(0, 1);
        outStream.writeBooleanBit(html);
        outStream.writeBooleanBit(useOutlines);
        if (hasFont) {
            outStream.writeUI16(fontId);
            outStream.writeUI16(fontHeight);
        }
        if (hasTextColor) {
            textColor.write(outStream);
        }
        if (hasMaxLength) {
            outStream.writeUI16(maxLength);
        }
        if (hasLayout) {
            outStream.writeUI8(align);
            outStream.writeUI16(leftMargin);
            outStream.writeUI16(rightMargin);
            outStream.writeUI16(indent);
            outStream.writeUI16(leading);
        }
        if (variableName != null) {
            outStream.writeString(variableName);
        } else {
            outStream.writeString("");
        }
        if (hasText) {
            outStream.writeString(initialText);
        }
    }
