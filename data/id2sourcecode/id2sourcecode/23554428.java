    protected void placeFrame(EmptyFrame frame, int index) {
        int width = (int) framesPanel.getPreferredSize().getWidth();
        int height = (int) framesPanel.getPreferredSize().getHeight();
        float frameWidthMultiplier;
        float frameHeightMultiplier;
        int sizeReport = (nbDisplayedFrames + 1) / 2;
        switch(nbDisplayedFrames) {
            case 0:
                System.out.println("Empty frames set");
                break;
            case 1:
                frame.setBounds((int) (width * HORIZONTAL_BORDER), (int) (height * VERTICAL_BORDER), (int) (width * (1 - 2 * HORIZONTAL_BORDER)), (int) (height * (1 - 2 * VERTICAL_BORDER)));
                break;
            case 2:
                frame.setBounds((int) (width * (HORIZONTAL_BORDER * (1 - (index % sizeReport)) + (1 + HORIZONTAL_GAP) / sizeReport * (index % sizeReport))), (int) (height * (VERTICAL_BORDER * (1 - (index / sizeReport)) + (1 + VERTICAL_GAP) / 2 * (index / sizeReport))), (int) (width * (1 - 2 * HORIZONTAL_BORDER)), (int) (height * (1 - 2 * VERTICAL_BORDER - VERTICAL_GAP) / 2));
                break;
            case 3:
            case 4:
                frame.setBounds((int) (width * (HORIZONTAL_BORDER * (1 - (index % sizeReport)) + (1 + HORIZONTAL_GAP) / sizeReport * (index % sizeReport))), (int) (height * (VERTICAL_BORDER * (1 - (index / sizeReport)) + (1 + VERTICAL_GAP) / sizeReport * (index / sizeReport))), (int) (width * (1 - 2 * HORIZONTAL_BORDER - HORIZONTAL_GAP) / 2), (int) (height * (1 - 2 * VERTICAL_BORDER - VERTICAL_GAP) / 2));
                break;
            case 5:
            case 6:
                frameWidthMultiplier = (1 - 2 * HORIZONTAL_BORDER - HORIZONTAL_GAP) / 2;
                frameHeightMultiplier = (1 - 2 * VERTICAL_BORDER - 2 * VERTICAL_GAP) / 3;
                frame.setBounds((int) (width * (HORIZONTAL_BORDER + (index % 2) * (HORIZONTAL_GAP + frameWidthMultiplier))), (int) (height * (VERTICAL_BORDER + (index / 2) * (VERTICAL_GAP + frameHeightMultiplier))), (int) (width * frameWidthMultiplier), (int) (height * frameHeightMultiplier));
                break;
            default:
                break;
        }
    }
