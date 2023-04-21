    void createArtificialROIs(int howMany) {
        int nCandidates = 2 * howMany;
        Vector<Rectangle> candidates = new Vector<Rectangle>();
        int maxX = imageDim.width - displayDim.width;
        int maxY = imageDim.height - displayDim.height;
        {
            int way = 0;
            if (way == 0) {
                for (int i = 0; i < nCandidates; i++) {
                    int x = rand.nextInt(maxX + 1);
                    int y = rand.nextInt(maxY + 1);
                    candidates.add(new Rectangle(new Point(x, y), displayDim));
                }
            } else if (way == 1) {
            }
        }
        {
            int way = 0;
            if (way == 0) {
                for (int i = 0; i < howMany; i++) {
                    Rectangle rect = candidates.remove(rand.nextInt(candidates.size()));
                    ROI roi = new ROI(rect.getLocation(), rect.getSize(), null);
                    roi.setStaticRelevance(ARTIFICIAL_RELEVANCE);
                    rois.add(roi);
                }
            } else if (way == 1) {
            }
        }
    }
