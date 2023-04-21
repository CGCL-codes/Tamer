    private int processWord(final GC gc, final String sLine, String word, final Rectangle printArea, final LineInfo lineInfo, StringBuffer outputLine, final StringBuffer space) {
        if (word.length() == 0) {
            space.append(' ');
            return -1;
        }
        if (images != null && word.length() >= 2 && word.charAt(0) == '%') {
            int imgIdx = word.charAt(1) - '0';
            if (images.length > imgIdx && imgIdx >= 0 && images[imgIdx] != null) {
                Image img = images[imgIdx];
                Rectangle bounds = img.getBounds();
                if (imageScales != null && imageScales.length > imgIdx) {
                    bounds.width = (int) (bounds.width * imageScales[imgIdx]);
                    bounds.height = (int) (bounds.height * imageScales[imgIdx]);
                }
                Point spaceExtent = gc.stringExtent(space.toString());
                int newWidth = lineInfo.width + bounds.width + spaceExtent.x;
                if (newWidth > printArea.width) {
                    if (bounds.width + spaceExtent.x < printArea.width || lineInfo.width > 0) {
                        return 0;
                    }
                }
                if (lineInfo.imageIndexes == null) {
                    lineInfo.imageIndexes = new int[] { imgIdx };
                }
                int targetWidth = lineInfo.width + newWidth;
                lineInfo.width = newWidth;
                lineInfo.height = Math.max(bounds.height, lineInfo.height);
                Point ptWordSize = gc.stringExtent(word.substring(2) + " ");
                if (lineInfo.width + ptWordSize.x > printArea.width) {
                    outputLine.append(space);
                    outputLine.append(word.substring(0, 2));
                    return 2;
                }
                outputLine.append(space);
                space.setLength(0);
                outputLine.append(word.substring(0, 2));
                word = word.substring(2);
            }
        }
        Point ptLineAndWordSize = gc.stringExtent(outputLine + word + " ");
        if (ptLineAndWordSize.x > printArea.width) {
            Point ptWordSize2 = gc.stringExtent(word + " ");
            boolean bWordLargerThanWidth = ptWordSize2.x > printArea.width;
            if (bWordLargerThanWidth && lineInfo.width > 0) {
                return 0;
            }
            int endIndex = word.length();
            long diff = endIndex;
            while (ptLineAndWordSize.x != printArea.width) {
                diff = (diff >> 1) + (diff % 2);
                if (diff <= 0) {
                    diff = 1;
                }
                if (ptLineAndWordSize.x > printArea.width) {
                    endIndex -= diff;
                    if (endIndex < 1) {
                        endIndex = 1;
                    }
                } else {
                    endIndex += diff;
                    if (endIndex > word.length()) {
                        endIndex = word.length();
                    }
                }
                ptLineAndWordSize = gc.stringExtent(outputLine + word.substring(0, endIndex) + " ");
                if (diff <= 1) {
                    break;
                }
            }
            boolean nothingFit = endIndex == 0;
            if (nothingFit) {
                endIndex = 1;
            }
            if (ptLineAndWordSize.x > printArea.width && endIndex > 1) {
                endIndex--;
                ptLineAndWordSize = gc.stringExtent(outputLine + word.substring(0, endIndex) + " ");
            }
            if (DEBUG) {
                System.out.println("excess starts at " + endIndex + " of " + word.length() + ". " + "wrap?" + wrap);
            }
            if (wrap && (printFlags & FLAG_FULLLINESONLY) > 0) {
                int nextLineHeight = gc.stringExtent(GOOD_STRING).y;
                if (iCurrentHeight + ptLineAndWordSize.y + nextLineHeight > printArea.height) {
                    if (DEBUG) {
                        System.out.println("turn off wrap");
                    }
                    wrap = false;
                }
            }
            if (endIndex > 0 && outputLine.length() > 0 && !nothingFit) {
                outputLine.append(space);
            }
            int w = ptLineAndWordSize.x - lineInfo.width;
            if (wrap && !nothingFit && !bWordLargerThanWidth) {
                return 0;
            }
            outputLine.append(word.substring(0, endIndex));
            if (!wrap) {
                int len = outputLine.length();
                if (len == 0) {
                    if (word.length() > 0) {
                        outputLine.append(word.charAt(0));
                    } else if (sLine.length() > 0) {
                        outputLine.append(sLine.charAt(0));
                    }
                } else {
                    if (len > 2) {
                        len -= 2;
                    }
                    outputLine.setLength(len);
                    outputLine.append("…");
                    cutoff = true;
                }
            }
            if (DEBUG) {
                System.out.println("excess " + word.substring(endIndex));
            }
            return endIndex;
        }
        lineInfo.width = ptLineAndWordSize.x;
        if (lineInfo.width > printArea.width) {
            if (space.length() > 0) {
                space.delete(0, space.length());
            }
            if (!wrap) {
                int len = outputLine.length();
                if (len == 0) {
                    if (word.length() > 0) {
                        outputLine.append(word.charAt(0));
                    } else if (sLine.length() > 0) {
                        outputLine.append(sLine.charAt(0));
                    }
                } else {
                    if (len > 2) {
                        len -= 2;
                    }
                    outputLine.setLength(len);
                    outputLine.append("…");
                    cutoff = true;
                }
                return -1;
            } else {
                return 0;
            }
        }
        if (outputLine.length() > 0) {
            outputLine.append(space);
        }
        outputLine.append(word);
        if (space.length() > 0) {
            space.delete(0, space.length());
        }
        space.append(' ');
        return -1;
    }
