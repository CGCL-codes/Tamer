    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        double questionSizeFactor = Math.min(1, questionAnimationCounter / questionPaintTime);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (scaledBackdrop != null) {
            g.drawImage(scaledBackdrop, (getWidth() - scaledBackdrop.getWidth(this)) / 2, (getHeight() - scaledBackdrop.getHeight(this)) / 2, this);
        }
        if (scaledLogo != null && boardAnimationCounter == 0) {
            g.drawImage(scaledLogo, (getWidth() - scaledLogo.getWidth(this)) / 2, (getHeight() - scaledLogo.getHeight(this)) / 2, this);
        }
        if (categories == null) {
            return;
        }
        if (categoryFont == null) {
            int maxWidth = 0;
            g.setFont(new Font(textFont, Font.BOLD, 100));
            for (InGameCategory category : categories) {
                maxWidth = Math.max(maxWidth, g.getFontMetrics().stringWidth(category.name));
            }
            categoryFont = new Font(textFont, Font.BOLD, Math.min(80 * (categoryWidth - categoryPadding) / maxWidth, 80 * (categoryHeight - categoryPadding) / g.getFontMetrics().getHeight()));
        }
        g.setFont(categoryFont);
        int i = 0;
        for (InGameCategory category : categories) {
            boolean isCurrentCategory = category == categories[currentCategoryIndex];
            int x = categoryPadding + i * (categoryPadding + categoryWidth);
            int y = categoryPadding;
            double categoryAnimationCounter = boardAnimationCounter - categoryTime * i - categoryStartTime;
            if (categoryAnimationCounter > 0) {
                double sizeFactor = Math.min(1, categoryAnimationCounter / categoryGrowTime);
                g.setPaint(categoryGradient);
                if (isCurrentCategory) {
                    int sR = (int) (categoryGradientStartColor.getRed() * (1 - questionSizeFactor) + categoryHighlightGradientStartColor.getRed() * questionSizeFactor);
                    int sG = (int) (categoryGradientStartColor.getGreen() * (1 - questionSizeFactor) + categoryHighlightGradientStartColor.getGreen() * questionSizeFactor);
                    int sB = (int) (categoryGradientStartColor.getBlue() * (1 - questionSizeFactor) + categoryHighlightGradientStartColor.getBlue() * questionSizeFactor);
                    int eR = (int) (categoryGradientEndColor.getRed() * (1 - questionSizeFactor) + categoryHighlightGradientEndColor.getRed() * questionSizeFactor);
                    int eG = (int) (categoryGradientEndColor.getGreen() * (1 - questionSizeFactor) + categoryHighlightGradientEndColor.getGreen() * questionSizeFactor);
                    int eB = (int) (categoryGradientEndColor.getBlue() * (1 - questionSizeFactor) + categoryHighlightGradientEndColor.getBlue() * questionSizeFactor);
                    g.setPaint(new GradientPaint(0, categoryPadding, new Color(sR, sG, sB), 0, categoryPadding + categoryHeight, new Color(eR, eG, eB)));
                }
                g.fillRoundRect(x, y, (int) (categoryWidth * sizeFactor), (int) (categoryHeight * sizeFactor), categoryCornerRadius, categoryCornerRadius);
                g.setColor(Color.BLACK);
                g.drawRoundRect(x, y, (int) (categoryWidth * sizeFactor), (int) (categoryHeight * sizeFactor), categoryCornerRadius, categoryCornerRadius);
                if (sizeFactor >= 1) {
                    int fontWidth = g.getFontMetrics().stringWidth(category.name);
                    int fontHeight = g.getFontMetrics().getHeight();
                    int xOffset = (categoryWidth - fontWidth) / 2;
                    int yOffset = (categoryHeight - fontHeight) / 2;
                    g.setColor(Color.BLACK);
                    g.drawString(category.name, x + xOffset + 1, y + categoryHeight - yOffset + 1);
                    g.setColor(Color.WHITE);
                    g.drawString(category.name, x + xOffset, y + categoryHeight - yOffset);
                }
            }
            int j = 0;
            for (InGameQuestion question : category.questions) {
                double animationCounter = boardAnimationCounter - ((i * numberOfStepsBetweenQuestions + j) * questionTime + questionStartTime);
                if (animationCounter > 0 && !question.usedUp) {
                    int qx = x + (categoryWidth - questionWidth) / 2;
                    int qy = questionStartX + j * (questionPadding + questionHeight);
                    double sizeFactor = Math.min(1, animationCounter / questionGrowTime);
                    GradientPaint tmpPaint = questionGradient[j];
                    g.setPaint(tmpPaint);
                    g.fillOval(qx, qy, (int) (questionWidth * sizeFactor), (int) (questionHeight * sizeFactor));
                    g.setColor(Color.BLACK);
                    g.drawOval(qx, qy, (int) (questionWidth * sizeFactor), (int) (questionHeight * sizeFactor));
                    if (sizeFactor >= 1) {
                        int fontWidth = g.getFontMetrics().stringWidth("" + question.score);
                        int fontHeight = g.getFontMetrics().getHeight();
                        int xOffset = (questionWidth - fontWidth) / 2;
                        int yOffset = (int) (((questionHeight - .65 * fontHeight) / 2));
                        g.setColor(Color.BLACK);
                        g.drawString("" + question.score, qx + xOffset + 1, qy + questionHeight - yOffset + 1);
                        g.setColor(Color.WHITE);
                        g.drawString("" + question.score, qx + xOffset, qy + questionHeight - yOffset);
                    }
                }
                j++;
            }
            i++;
        }
        if (questionAnimationCounter > 0) {
            g.setPaint(showQuestionGradient);
            g.fillRoundRect(showQuestionStartX, showQuestionStartY, (int) (showQuestionWidth * questionSizeFactor), (int) (showQuestionHeight * questionSizeFactor), showQuestionCornerRadius, showQuestionCornerRadius);
            g.setColor(Color.BLACK);
            g.drawRoundRect(showQuestionStartX, showQuestionStartY, (int) (showQuestionWidth * questionSizeFactor), (int) (showQuestionHeight * questionSizeFactor), showQuestionCornerRadius, showQuestionCornerRadius);
            if (questionSizeFactor >= 1 && currentQuestion != null) {
                g.setFont(showQuestionFont);
                LinkedList<String> tmpList = fold(currentQuestion, (int) (currentQuestion.length() * (showQuestionSizeRatio * getWidth() - showQuestionPadding) / g.getFontMetrics().stringWidth(currentQuestion)));
                int xOffset = (int) (getWidth() * showQuestionSizeRatio - showQuestionPadding) / 2;
                int fontHeight = g.getFontMetrics().getHeight();
                for (int j = 0; j < tmpList.size(); j++) {
                    g.setColor(Color.BLACK);
                    g.drawString(tmpList.get(j), getWidth() / 2 - xOffset, getHeight() / 2 + j * fontHeight);
                    g.setColor(Color.WHITE);
                    g.drawString(tmpList.get(j), getWidth() / 2 - xOffset - 1, getHeight() / 2 + j * fontHeight - 1);
                }
            }
        }
    }
