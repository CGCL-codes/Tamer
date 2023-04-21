        private void binarySearchRangeForSmallestValidHeight() {
            while (minHeight < maxHeight) {
                int testHeight = minHeight + (maxHeight - minHeight) / 2;
                PrintIterator testIter = iterator.copy();
                PrintPiece testPiece = PaperClips.next(testIter, canvasWidth, testHeight);
                if (testPiece == null) {
                    minHeight = testHeight + 1;
                } else if (testIter.hasNext()) {
                    testPiece.dispose();
                    minHeight = testHeight + 1;
                } else {
                    maxHeight = Math.min(testHeight, testPiece.getSize().y);
                    piece.dispose();
                    piece = testPiece;
                }
            }
        }
