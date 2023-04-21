    private float setText(String textString, int command) {
        float[][] Trm = new float[3][3];
        float[][] temp = new float[3][3];
        char rawChar = ' ';
        int fontSize = 0, rawInt = 0;
        float currentWidth = 0;
        String displayValue = "";
        float TFS = currentTextState.getTfs();
        charSpacing = currentTextState.getCharacterSpacing() / TFS;
        int type = currentFontData.getFontType();
        boolean isCID = currentFontData.isCIDFont();
        PdfJavaGlyphs glyphs = currentFontData.getGlyphData();
        currentGraphicsState.setStrokeColor(strokeColorSpace.getColor());
        currentGraphicsState.setNonstrokeColor(nonstrokeColorSpace.getColor());
        Trm = Matrix.multiply(currentTextState.Tm, currentGraphicsState.CTM);
        Trm[0][0] = Trm[0][0];
        Trm[0][1] = Trm[0][1];
        Trm[1][0] = Trm[1][0];
        Trm[1][1] = Trm[1][1];
        temp[0][0] = TFS * currentTextState.getHorizontalScaling();
        temp[1][1] = TFS;
        temp[2][1] = currentTextState.getTextRise();
        temp[2][2] = 1;
        Trm = Matrix.multiply(temp, Trm);
        if (Trm[1][1] != 0) {
            fontSize = Math.round(Trm[1][1]);
            if (fontSize == 0) fontSize = Math.round(Trm[0][1]);
        } else {
            fontSize = Math.round(Trm[1][0]);
            if (fontSize == 0) fontSize = Math.round(Trm[0][0]);
            if (fontSize < 0) {
                fontSize = -fontSize;
            }
        }
        if (fontSize == 0) fontSize = 1;
        float x = Trm[2][0];
        float y = Trm[2][1];
        int i = 0;
        int dataPointer = textString.length();
        int numOfPrefixes = 0;
        while (i < dataPointer) {
            rawChar = textString.charAt(i);
            rawInt = rawChar;
            displayValue = String.valueOf(rawChar);
            temp[0][0] = 1;
            temp[0][1] = 0;
            temp[0][2] = 0;
            temp[1][0] = 0;
            temp[1][1] = 1;
            temp[1][2] = 0;
            temp[2][0] = (currentWidth);
            temp[2][1] = 0;
            temp[2][2] = 1;
            Trm = Matrix.multiply(temp, Trm);
            if ((command == DRAW) && (Trm[2][0] > rightMargin)) {
                relativeMove(0, -currentTextState.getLeading() * TFS);
                Trm[2][0] = (float) leftMargin;
                Trm[2][1] = currentTextState.Tm[2][1];
            }
            String charGlyph = "notdef";
            currentWidth = currentFontData.getGlyphWidth(charGlyph, rawInt, displayValue);
            if (!isCID) {
                if (rawInt > 255) rawChar = ' '; else charGlyph = currentFontData.getMappedChar(rawInt, false);
            }
            if ((command != MEASURE) && (rawChar != ' ')) {
                if ((currentFontData.isFontEmbedded)) {
                    PdfGlyph glyph = glyphs.getEmbeddedGlyph(factory, charGlyph, Trm, rawInt, displayValue, currentWidth, null);
                    try {
                        if (glyph != null) {
                            AffineTransform at = new AffineTransform(Trm[0][0], Trm[0][1], Trm[1][0], Trm[1][1], Trm[2][0], Trm[2][1] - this.fontSize);
                            at.scale(currentFontData.FontMatrix[0], currentFontData.FontMatrix[3]);
                            int fontType = DynamicVectorRenderer.TRUETYPE;
                            if (renderDirectly) {
                                PdfPaint currentCol = null, textFillCol = null;
                                int text_fill_type = currentGraphicsState.getTextRenderType();
                                if ((text_fill_type & GraphicsState.FILL) == GraphicsState.FILL) textFillCol = currentGraphicsState.getNonstrokeColor();
                                if ((text_fill_type & GraphicsState.STROKE) == GraphicsState.STROKE) currentCol = currentGraphicsState.getStrokeColor();
                                current.renderEmbeddedText(currentGraphicsState, text_fill_type, glyph, fontType, g2, at, null, currentCol, textFillCol, currentGraphicsState.getStrokeAlpha(), currentGraphicsState.getNonStrokeAlpha(), null, 0);
                            } else current.drawEmbeddedText(Trm, fontSize, glyph, null, fontType, currentGraphicsState, at);
                        }
                    } catch (Exception e) {
                        addPageFailureMessage("Exception " + e + " on embedded font renderer");
                    }
                } else if ((displayValue.length() > 0) && (!displayValue.startsWith("&#"))) {
                    boolean isSTD = PdfDecoder.isRunningOnMac || (StandardFonts.isStandardFont(glyphs.getBaseFontName(), false));
                    Area transformedGlyph2 = glyphs.getStandardGlyph(Trm, rawInt, displayValue, currentWidth, isSTD);
                    if (transformedGlyph2 != null) {
                        if (renderDirectly) {
                            PdfPaint currentCol = null, fillCol = null;
                            int text_fill_type = currentGraphicsState.getTextRenderType();
                            if ((text_fill_type & GraphicsState.FILL) == GraphicsState.FILL) fillCol = currentGraphicsState.getNonstrokeColor();
                            if ((text_fill_type & GraphicsState.STROKE) == GraphicsState.STROKE) currentCol = currentGraphicsState.getStrokeColor();
                            AffineTransform def = g2.getTransform();
                            if (DynamicVectorRenderer.marksNewCode) g2.translate(Trm[2][0], Trm[2][1]);
                            if (DynamicVectorRenderer.newCode2) g2.scale(Trm[0][0], -Trm[1][1]);
                            current.renderText(Trm[2][0], Trm[2][1], text_fill_type, transformedGlyph2, g2, null, currentCol, fillCol, currentGraphicsState.getStrokeAlpha(), currentGraphicsState.getNonStrokeAlpha());
                            g2.setTransform(def);
                        } else current.drawEmbeddedText(Trm, fontSize, null, transformedGlyph2, DynamicVectorRenderer.TEXT, currentGraphicsState, null);
                    }
                }
            }
            currentWidth = currentWidth + charSpacing;
            i++;
        }
        temp[0][0] = 1;
        temp[0][1] = 0;
        temp[0][2] = 0;
        temp[1][0] = 0;
        temp[1][1] = 1;
        temp[1][2] = 0;
        temp[2][0] = (currentWidth);
        temp[2][1] = 0;
        temp[2][2] = 1;
        Trm = Matrix.multiply(temp, Trm);
        if (command != MEASURE) {
            currentTextState.Tm[2][0] = Trm[2][0];
            currentTextState.Tm[2][1] = Trm[2][1];
        }
        return Trm[2][0];
    }
