    private void setResult() {
        PredictionModel model = selectedModel();
        maxScore = result.getMaxPredictionScore(model);
        minScore = result.getMinPredictionScore(model);
        SpecificityEstimator est = model.getSpecEstimator();
        if (est != null) {
            double spec1 = est.threshold(1.0);
            double spec0 = est.threshold(0.0);
            if (maxScore < spec1) {
                maxScore = spec1;
            }
            if (minScore > spec0) {
                minScore = spec0;
            }
            threshold = result.getThreshold(model);
            if (Double.isInfinite(threshold)) {
                threshold = est.threshold(0.95);
            } else {
                specificity = est.specificity(threshold);
            }
        } else {
            this.threshold = (maxScore + minScore) / 2;
        }
        result.setThreshold(model, threshold);
        double eps = (maxScore - minScore) / maxTick;
        maxScore += eps;
        minScore -= eps;
        ignoreSlideChangeEvent = true;
        setSliderTitleModel();
        String textSpec;
        if (est == null) {
            textSpec = "N/A";
            specificityTextField.setEditable(false);
        } else {
            textSpec = String.format("%.2f", specificity * 100);
        }
        specificityTextField.setText(textSpec);
        scoreCutoffTextField.setText(String.format("%.2f", threshold));
        setSliderLabels();
        setMapScoreColor();
        specificitySlider.setValue((int) Math.round(maxTick * (threshold - minScore) / (maxScore - minScore)));
        ignoreSlideChangeEvent = false;
    }
