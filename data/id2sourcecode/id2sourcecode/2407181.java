    public void load() throws IOException {
        int i;
        File file = new File(path);
        URL url = file.toURI().toURL();
        if (file.exists()) {
            Properties temp = new Properties();
            temp.load(url.openStream());
            if (temp.getProperty("OutputFileName") != null) OutputFileName.setText(temp.getProperty("OutputFileName"));
            if (temp.getProperty("OutputObjectName") != null) OutputObjectName.setText(temp.getProperty("OutputObjectName"));
            if (temp.getProperty("DebugSaveOutputImagesFolder") != null) {
                DebugSaveOutputImagesFolder = temp.getProperty("DebugSaveOutputImagesFolder");
                Debug = true;
            }
            if (temp.getProperty("SaveOnProgramWindowClose") != null) SaveOnProgramWindowClose = temp.getProperty("SaveOnProgramWindowClose").equals("true");
            if (temp.getProperty("SaveRestrictedSearchImageProperties") != null) SaveRestrictedSearchImageProperties = temp.getProperty("SaveRestrictedSearchImageProperties").equals("true");
            if (temp.getProperty("SaveOnProgramCancel") != null) SaveOnProgramCancel = temp.getProperty("SaveOnProgramCancel").equals("true");
            if (temp.getProperty("SaveOnProgramFinish") != null) SaveOnProgramFinish = temp.getProperty("SaveOnProgramFinish").equals("true");
            if (temp.getProperty("SaveCalibrationSheetProperties") != null) SaveCalibrationSheetProperties = temp.getProperty("SaveCalibrationSheetProperties").equals("true");
            if (temp.getProperty("SaveProcessedImageProperties") != null) SaveProcessedImageProperties = temp.getProperty("SaveProcessedImageProperties").equals("true");
            if (temp.getProperty("BlankOutputFilenameOnLoad") != null) BlankOutputFilenameOnLoad = temp.getProperty("BlankOutputFilenameOnLoad").equals("true");
            if (temp.getProperty("DebugImageOverlay") != null) DebugImageOverlay = temp.getProperty("DebugImageOverlay").equals("true");
            if (temp.getProperty("DebugImageSegmentation") != null) DebugImageSegmentation = temp.getProperty("DebugImageSegmentation").equals("true");
            if (temp.getProperty("DebugRestrictedSearch") != null) DebugRestrictedSearch = temp.getProperty("DebugRestrictedSearch").equals("true");
            if (temp.getProperty("DebugCalibrationSheetBarycentricEstimate") != null) DebugCalibrationSheetBarycentricEstimate = temp.getProperty("DebugCalibrationSheetBarycentricEstimate").equals("true");
            if (temp.getProperty("DebugEllipseFinding") != null) DebugEllipseFinding = temp.getProperty("DebugEllipseFinding").equals("true");
            if (temp.getProperty("DebugManualEllipseSelection") != null) DebugManualEllipseSelection = temp.getProperty("DebugManualEllipseSelection").equals("true");
            if (temp.getProperty("DebugPointPairMatching") != null) DebugPointPairMatching = temp.getProperty("DebugPointPairMatching").equals("true");
            if (temp.getProperty("DebugPointPairMatchingSubsets") != null) DebugPointPairMatchingSubsets = temp.getProperty("DebugPointPairMatchingSubsets").equals("true");
            if (temp.getProperty("DebugEdgeFindingForEllipseDetection") != null) DebugEdgeFindingForEllipseDetection = temp.getProperty("DebugEdgeFindingForEllipseDetection").equals("true");
            if (temp.getProperty("DebugCalibrationSheetPlanarHomographyEstimate") != null) DebugCalibrationSheetPlanarHomographyEstimate = temp.getProperty("DebugCalibrationSheetPlanarHomographyEstimate").equals("true");
            if (temp.getProperty("DebugVoxelisation") != null) DebugVoxelisation = temp.getProperty("DebugVoxelisation").equals("true");
            if (temp.getProperty("DebugShow3DFlythough") != null) DebugShow3DFlythough = temp.getProperty("DebugShow3DFlythough").equals("true");
            if (temp.getProperty("DebugIndividualTextureMatch") != null) DebugIndividualTextureMatch = temp.getProperty("DebugIndividualTextureMatch").equals("true");
            if (temp.getProperty("DebugMarchingTextureMatch") != null) DebugMarchingTextureMatch = temp.getProperty("DebugMarchingTextureMatch").equals("true");
            if (temp.getProperty("AlgorithmSettingTextureMatchingManualSelectionOfInitialLineSegment") != null) AlgorithmSettingTextureMatchingManualSelectionOfInitialLineSegment = temp.getProperty("AlgorithmSettingTextureMatchingManualSelectionOfInitialLineSegment").equals("true");
            for (i = 0; i < SkipStep.length; i++) if (temp.getProperty("SkipStep" + Integer.toString(i + 1)) != null) SkipStep[i] = temp.getProperty("SkipStep" + Integer.toString(i + 1)).equals("true");
            if (temp.getProperty("CalibrationSheetKeepAspectRatioWhenPrinted") != null) CalibrationSheetKeepAspectRatioWhenPrinted.setSelected(temp.getProperty("CalibrationSheetKeepAspectRatioWhenPrinted").equals("true"));
            if (temp.getProperty("PaperSizeIsCustom") != null) PaperSizeIsCustom.setSelected(temp.getProperty("PaperSizeIsCustom").equals("true"));
            if (temp.getProperty("PaperOrientationIsPortrait") != null) {
                boolean set = temp.getProperty("PaperOrientationIsPortrait").equals("true");
                PaperOrientationIsPortrait = new JRadioButton(PaperOrientationIsPortrait.getText(), set);
                PaperOrientationIsLandscape = new JRadioButton(PaperOrientationIsLandscape.getText(), !set);
            }
            if (temp.getProperty("AlgorithmSettingMaximumCameraAngleFromVerticalInDegrees") != null) {
                try {
                    AlgorithmSettingMaximumCameraAngleFromVerticalInDegrees = Integer.valueOf(temp.getProperty("AlgorithmSettingMaximumCameraAngleFromVerticalInDegrees"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingMaximumCameraAngleFromVerticalInDegrees - leaving as default: " + e);
                }
                if (AlgorithmSettingMaximumCameraAngleFromVerticalInDegrees > 89) AlgorithmSettingMaximumCameraAngleFromVerticalInDegrees = 89;
                if (AlgorithmSettingMaximumCameraAngleFromVerticalInDegrees < 0) AlgorithmSettingMaximumCameraAngleFromVerticalInDegrees = 0;
            }
            if (temp.getProperty("AlgorithmSettingMinimumFoundValidEllipses") != null) {
                try {
                    AlgorithmSettingMinimumFoundValidEllipses = Integer.valueOf(temp.getProperty("AlgorithmSettingMinimumFoundValidEllipses"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingMinimumFoundValidEllipses - leaving as default: " + e);
                }
                if (AlgorithmSettingMinimumFoundValidEllipses < 4) AlgorithmSettingMinimumFoundValidEllipses = 4;
            }
            if (temp.getProperty("AlgorithmSettingResampledImageWidthForEllipseDetection") != null) {
                try {
                    AlgorithmSettingResampledImageWidthForEllipseDetection = Integer.valueOf(temp.getProperty("AlgorithmSettingResampledImageWidthForEllipseDetection"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingResampledImageWidthForEllipseDetection - leaving as default: " + e);
                }
                if (AlgorithmSettingResampledImageWidthForEllipseDetection < 320) AlgorithmSettingResampledImageWidthForEllipseDetection = 320;
            }
            if (temp.getProperty("DebugShow3DFlythoughImagePixelStep") != null) {
                try {
                    DebugShow3DFlythoughImagePixelStep = Integer.valueOf(temp.getProperty("DebugShow3DFlythoughImagePixelStep"));
                } catch (Exception e) {
                    System.out.println("Error loading DebugShow3DFlythoughImagePixelStep - leaving as default: " + e);
                }
                if (DebugShow3DFlythoughImagePixelStep < 1) DebugShow3DFlythoughImagePixelStep = 1;
            }
            if (temp.getProperty("AlgorithmSettingPointPairMatchingDistanceThreshold") != null) {
                try {
                    AlgorithmSettingPointPairMatchingDistanceThreshold = Double.valueOf(temp.getProperty("AlgorithmSettingPointPairMatchingDistanceThreshold"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingPointPairMatchingDistanceThreshold - leaving as default: " + e);
                }
                if (AlgorithmSettingPointPairMatchingDistanceThreshold < 0) AlgorithmSettingPointPairMatchingDistanceThreshold = 1;
            }
            if (temp.getProperty("AlgorithmSettingEllipseValidityThresholdPercentage") != null) {
                try {
                    AlgorithmSettingEllipseValidityThresholdPercentage = Integer.valueOf(temp.getProperty("AlgorithmSettingEllipseValidityThresholdPercentage"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingEllipseValidityThresholdPercentage - leaving as default: " + e);
                }
                if (AlgorithmSettingEllipseValidityThresholdPercentage > 100) AlgorithmSettingEllipseValidityThresholdPercentage = 100;
                if (AlgorithmSettingEllipseValidityThresholdPercentage < 0) AlgorithmSettingEllipseValidityThresholdPercentage = 0;
            }
            if (temp.getProperty("AlgorithmSettingEdgeStrengthThreshold") != null) {
                try {
                    AlgorithmSettingEdgeStrengthThreshold = Integer.valueOf(temp.getProperty("AlgorithmSettingEdgeStrengthThreshold"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingEdgeStrengthThreshold - leaving as default: " + e);
                }
                if (AlgorithmSettingEdgeStrengthThreshold > 255) AlgorithmSettingEdgeStrengthThreshold = 255;
                if (AlgorithmSettingEdgeStrengthThreshold < 0) AlgorithmSettingEdgeStrengthThreshold = 0;
            }
            if (temp.getProperty("AlgorithmSettingCalibrationSheetEdgeStrengthThreshold") != null) {
                try {
                    AlgorithmSettingCalibrationSheetEdgeStrengthThreshold = Integer.valueOf(temp.getProperty("AlgorithmSettingCalibrationSheetEdgeStrengthThreshold"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingEdgeStrengthThreshold - leaving as default: " + e);
                }
                if (AlgorithmSettingCalibrationSheetEdgeStrengthThreshold > 255) AlgorithmSettingCalibrationSheetEdgeStrengthThreshold = 255;
                if (AlgorithmSettingCalibrationSheetEdgeStrengthThreshold < 0) AlgorithmSettingCalibrationSheetEdgeStrengthThreshold = 0;
            }
            if (temp.getProperty("AlgorithmSettingMaxBundleAdjustmentNumberOfIterations") != null) {
                try {
                    AlgorithmSettingMaxBundleAdjustmentNumberOfIterations = Integer.valueOf(temp.getProperty("AlgorithmSettingMaxBundleAdjustmentNumberOfIterations"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingMaxBundleAdjustmentNumberOfIterations - leaving as default: " + e);
                }
                if (AlgorithmSettingMaxBundleAdjustmentNumberOfIterations < 1) AlgorithmSettingMaxBundleAdjustmentNumberOfIterations = 1;
            }
            if (temp.getProperty("AlgorithmSettingStepsAroundCircleCircumferenceForEllipseEstimationInBundleAdjustment") != null) {
                try {
                    AlgorithmSettingStepsAroundCircleCircumferenceForEllipseEstimationInBundleAdjustment = Integer.valueOf(temp.getProperty("AlgorithmSettingStepsAroundCircleCircumferenceForEllipseEstimationInBundleAdjustment"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingStepsAroundCircleCircumferenceForEllipseEstimationInBundleAdjustment - leaving as default: " + e);
                }
                if (AlgorithmSettingStepsAroundCircleCircumferenceForEllipseEstimationInBundleAdjustment < 5) AlgorithmSettingStepsAroundCircleCircumferenceForEllipseEstimationInBundleAdjustment = 5;
            }
            if (temp.getProperty("AlgorithmSettingVolumeSubDivision") != null) {
                try {
                    AlgorithmSettingVolumeSubDivision = Integer.valueOf(temp.getProperty("AlgorithmSettingVolumeSubDivision"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingVolumeSubDivision - leaving as default: " + e);
                }
                if (AlgorithmSettingVolumeSubDivision < 1) AlgorithmSettingVolumeSubDivision = 1;
            }
            if (temp.getProperty("AlgorithmSettingTextureMatchingNthTriangularNumberOfSamples") != null) {
                try {
                    AlgorithmSettingTextureMatchingNthTriangularNumberOfSamples = Integer.valueOf(temp.getProperty("AlgorithmSettingTextureMatchingNthTriangularNumberOfSamples"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingTextureMatchingNthTriangularNumberOfSamples - leaving as default: " + e);
                }
                if (AlgorithmSettingTextureMatchingNthTriangularNumberOfSamples < 2) AlgorithmSettingTextureMatchingNthTriangularNumberOfSamples = 2;
            }
            if (temp.getProperty("AlgorithmSettingTextureMatchingMinimumSimilarityRange") != null) {
                try {
                    AlgorithmSettingTextureMatchingMinimumSimilarityRange = Double.valueOf(temp.getProperty("AlgorithmSettingTextureMatchingMinimumSimilarityRange"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingTextureMatchingMinimumSimilarityRange - leaving as default: " + e);
                }
                if (AlgorithmSettingTextureMatchingMinimumSimilarityRange < 0) AlgorithmSettingTextureMatchingMinimumSimilarityRange = 0;
                if (AlgorithmSettingTextureMatchingMinimumSimilarityRange > 1.0) AlgorithmSettingTextureMatchingMinimumSimilarityRange = 1.0;
            }
            if (temp.getProperty("AlgorithmSettingTextureMatchingAngleAccuracyInDegrees") != null) {
                try {
                    AlgorithmSettingTextureMatchingAngleAccuracyInDegrees = Double.valueOf(temp.getProperty("AlgorithmSettingTextureMatchingAngleAccuracyInDegrees"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingTextureMatchingAngleAccuracyInDegrees - leaving as default: " + e);
                }
                if (AlgorithmSettingTextureMatchingAngleAccuracyInDegrees <= 0) AlgorithmSettingTextureMatchingAngleAccuracyInDegrees = 1;
            }
            if (temp.getProperty("AlgorithmSettingTextureMatchingMaxDistanceToSnapToTriangleVertex") != null) {
                try {
                    AlgorithmSettingTextureMatchingMaxDistanceToSnapToTriangleVertex = Double.valueOf(temp.getProperty("AlgorithmSettingTextureMatchingMaxDistanceToSnapToTriangleVertex"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingTextureMatchingMaxDistanceToSnapToTriangleVertex - leaving as default: " + e);
                }
                if (AlgorithmSettingTextureMatchingMaxDistanceToSnapToTriangleVertex < 0) AlgorithmSettingTextureMatchingMaxDistanceToSnapToTriangleVertex = 0;
            }
            if (temp.getProperty("AlgorithmSettingTextureMatchingMinimumAverageAngleBetweenCameraAndPlaneInDegrees") != null) {
                try {
                    AlgorithmSettingTextureMatchingMinimumAverageAngleBetweenCameraAndPlaneInDegrees = Double.valueOf(temp.getProperty("AlgorithmSettingTextureMatchingMinimumAverageAngleBetweenCameraAndPlaneInDegrees"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingTextureMatchingMinimumAverageAngleBetweenCameraAndPlaneInDegrees - leaving as default: " + e);
                }
                if (AlgorithmSettingTextureMatchingMinimumAverageAngleBetweenCameraAndPlaneInDegrees < 0) AlgorithmSettingTextureMatchingMinimumAverageAngleBetweenCameraAndPlaneInDegrees = 0;
                if (AlgorithmSettingTextureMatchingMinimumAverageAngleBetweenCameraAndPlaneInDegrees > 90) AlgorithmSettingTextureMatchingMinimumAverageAngleBetweenCameraAndPlaneInDegrees = 90;
            }
            if (temp.getProperty("AlgorithmSettingTextureMatchingMinimumAbsoluteSecondDerivative") != null) {
                try {
                    AlgorithmSettingTextureMatchingMinimumAbsoluteSecondDerivative = Double.valueOf(temp.getProperty("AlgorithmSettingTextureMatchingMinimumAbsoluteSecondDerivative"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingTextureMatchingMinimumAbsoluteSecondDerivative - leaving as default: " + e);
                }
                if (AlgorithmSettingTextureMatchingMinimumAbsoluteSecondDerivative < 0) AlgorithmSettingTextureMatchingMinimumAbsoluteSecondDerivative = 0;
            }
            if (temp.getProperty("AlgorithmSettingTextureMatchingMinimumValidMaxSimilarity") != null) {
                try {
                    AlgorithmSettingTextureMatchingMinimumValidMaxSimilarity = Double.valueOf(temp.getProperty("AlgorithmSettingTextureMatchingMinimumValidMaxSimilarity"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingTextureMatchingMinimumValidMaxSimilarity - leaving as default: " + e);
                }
                if (AlgorithmSettingTextureMatchingMinimumValidMaxSimilarity < 0) AlgorithmSettingTextureMatchingMinimumValidMaxSimilarity = 0;
                if (AlgorithmSettingTextureMatchingMinimumValidMaxSimilarity > 1) AlgorithmSettingTextureMatchingMinimumValidMaxSimilarity = 1;
            }
            if (temp.getProperty("AlgorithmSettingTextureMatchingMaxOverlapForSnapToFit") != null) {
                try {
                    AlgorithmSettingTextureMatchingMaxOverlapForSnapToFit = Double.valueOf(temp.getProperty("AlgorithmSettingTextureMatchingMaxOverlapForSnapToFit"));
                } catch (Exception e) {
                    System.out.println("Error loading AlgorithmSettingTextureMatchingMaxOverlapForSnapToFit - leaving as default: " + e);
                }
                if (AlgorithmSettingTextureMatchingMaxOverlapForSnapToFit < 0) AlgorithmSettingTextureMatchingMaxOverlapForSnapToFit = 0;
                if (AlgorithmSettingTextureMatchingMaxOverlapForSnapToFit > 1) AlgorithmSettingTextureMatchingMaxOverlapForSnapToFit = 1;
            }
            if (temp.getProperty("CurrentCalibrationPatternIndexNumber") != null) try {
                CurrentCalibrationPatternIndexNumber = Integer.valueOf(temp.getProperty("CurrentCalibrationPatternIndexNumber"));
            } catch (Exception e) {
                System.out.println("Error loading CurrentCalibrationPatternIndexNumber - leaving as default: " + e);
            }
            if (temp.getProperty("CurrentPaperSizeIndexNumber") != null) try {
                CurrentPaperSizeIndexNumber = Integer.valueOf(temp.getProperty("CurrentPaperSizeIndexNumber"));
            } catch (Exception e) {
                System.out.println("Error loading CurrentPaperSizeIndexNumber - leaving as default: " + e);
            }
            i = 0;
            while (temp.getProperty("ImageFileList" + Integer.toString(i)) != null) {
                String element = temp.getProperty("ImageFileList" + Integer.toString(i));
                boolean add = false;
                if (new File(element).exists()) {
                    add = true;
                    if (add) for (int j = 0; j < imagefiles.getSize(); j++) if (imagefiles.getElementAt(j).toString().equals(element)) add = false;
                }
                if (add) imagefiles.addElement(element);
                i++;
            }
            i = 0;
            while (temp.getProperty("CalibrationPatternFileList" + Integer.toString(i)) != null) {
                String element = temp.getProperty("CalibrationPatternFileList" + Integer.toString(i));
                boolean add = false;
                if (new File(element).exists()) {
                    add = !ImageFile.IsInvalid(element);
                    if (add) for (int j = 0; j < calibrationpatterns.getSize(); j++) if (calibrationpatterns.getElementAt(j).toString().equals(element)) add = false;
                }
                if (add) calibrationpatterns.addElement(element); else if (CurrentCalibrationPatternIndexNumber > i) CurrentCalibrationPatternIndexNumber--;
                i++;
            }
            i = 0;
            while ((temp.getProperty("PaperSizeNameList" + Integer.toString(i)) != null) && (temp.getProperty("PaperSizeWidthmmList" + Integer.toString(i)) != null) && (temp.getProperty("PaperSizeHeightmmList" + Integer.toString(i)) != null)) {
                Papersize element = new Papersize(temp.getProperty("PaperSizeNameList" + Integer.toString(i)), Double.valueOf(temp.getProperty("PaperSizeWidthmmList" + Integer.toString(i))), Double.valueOf(temp.getProperty("PaperSizeHeightmmList" + Integer.toString(i))));
                boolean add = true;
                for (int j = 0; j < PaperSizeList.getSize(); j++) if (((Papersize) PaperSizeList.getElementAt(j)).equals(element)) add = false;
                if (add) PaperSizeList.addElement(element);
                i++;
            }
            if ((CurrentCalibrationPatternIndexNumber < 0) || (CurrentCalibrationPatternIndexNumber >= calibrationpatterns.getSize())) CurrentCalibrationPatternIndexNumber = 0;
            if ((CurrentPaperSizeIndexNumber < 0) || (CurrentPaperSizeIndexNumber >= PaperSizeList.getSize())) CurrentPaperSizeIndexNumber = 0;
            if (temp.getProperty("PaperCustomSizeWidthmm") != null) {
                try {
                    PaperCustomSizeWidthmm.setText(temp.getProperty("PaperCustomSizeWidthmm"));
                } catch (Exception e) {
                    System.out.println("Error loading PaperCustomSizeWidthmm - leaving as default: " + e);
                }
                if (Double.valueOf(PaperCustomSizeWidthmm.getText()) < 1) PaperCustomSizeWidthmm.setText("1");
            }
            if (temp.getProperty("PaperCustomSizeHeightmm") != null) {
                try {
                    PaperCustomSizeHeightmm.setText(temp.getProperty("PaperCustomSizeHeightmm"));
                } catch (Exception e) {
                    System.out.println("Error loading PaperCustomSizeHeightmm- leaving as default: " + e);
                }
                if (Double.valueOf(PaperCustomSizeHeightmm.getText()) < 1) PaperCustomSizeHeightmm.setText("1");
            }
            if (temp.getProperty("PaperMarginHorizontalmm") != null) try {
                PaperMarginHorizontalmm.setText(temp.getProperty("PaperMarginHorizontalmm"));
            } catch (Exception e) {
                System.out.println("Error loading PaperMarginHorizontalmm - leaving as default: " + e);
            }
            if (temp.getProperty("PaperMarginVerticalmm") != null) try {
                PaperMarginVerticalmm.setText(temp.getProperty("PaperMarginVerticalmm"));
            } catch (Exception e) {
                System.out.println("Error loading PaperMarginVerticalmm - leaving as default: " + e);
            }
            SanityCheckMargins();
        }
        if (BlankOutputFilenameOnLoad) OutputFileName.setText("");
    }
