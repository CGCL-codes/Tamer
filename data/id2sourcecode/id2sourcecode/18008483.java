    private void btnCalcRespActionPerformed(java.awt.event.ActionEvent evt) {
        if (chcStack.getSelectedIndex() == 0) {
            IJ.error("Please select a stack to work with!");
            return;
        }
        if (txtExpTimes.getText().equals("")) {
            IJ.error("Please enter exposure times!");
            return;
        }
        try {
            ImagePlus imp = WindowManager.getImage(chcStack.getSelectedItem());
            int noOfImagesP = Integer.parseInt(txtNoOfImages.getText());
            int noOfPixelsN = Integer.parseInt(txtNoOfPixels.getText());
            int Zmin = Integer.parseInt(txtZmin.getText());
            int Zmax = Integer.parseInt(txtZmax.getText());
            int levels = Integer.parseInt(txtLevels.getText());
            String[] values = txtExpTimes.getText().split(",");
            double[] expTimes = new double[values.length];
            for (int i = 0; i < expTimes.length; i++) {
                expTimes[i] = new Double(values[i].trim().replaceAll(" ", ""));
            }
            if (noOfImagesP == 0) {
                IJ.error("Please select an appropriate number of images for the calculation of the camera response function");
                return;
            }
            if (noOfPixelsN == 0) {
                return;
            }
            if (noOfImagesP > imp.getStackSize()) {
                IJ.error("The number of images P is higher than the number of available images in the selected image stack!");
                return;
            }
            if (imp.getStackSize() < 2) {
                IJ.error("The size of the selected image stack is too small. You need at least two images for the calculation of the camera response function!");
                return;
            }
            if (!(noOfImagesP == expTimes.length)) {
                IJ.error("Exposure Times Missing!", "The number of images P is higher than the number of given exposure times!");
                return;
            }
            if (Zmin > Zmax) {
                IJ.error("Zmin is greater than Zmax!");
                return;
            }
            int arrayWidth = imp.getStack().getWidth();
            int arrayHeight = imp.getStack().getHeight();
            ResponseFunctionCalculatorSettings settings = new ResponseFunctionCalculatorSettings();
            settings.setExpTimes(expTimes);
            settings.setNoOfChannels(imp.getChannelProcessor().getNChannels());
            settings.setNoOfImages(noOfImagesP);
            settings.setNoOfPixelsN(noOfPixelsN);
            settings.setZmax(Zmax);
            settings.setZmin(Zmin);
            settings.setHeight(imp.getStack().getHeight());
            settings.setWidth(imp.getStack().getWidth());
            settings.setFileName((imp.getTitle()));
            settings.setType(imp.getType());
            settings.setLevels(levels);
            ResponseFunctionCalculator responseFunc = new RobertsonCalculator(imp, settings);
            HDRResponseFunctionCalculatorFrame gui = new HDRResponseFunctionCalculatorFrame(responseFunc);
            gui.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            new TextWindow("Error!", e.getMessage() + "An error occured while processing your input. Please make sure that you entered all information in the correct (numerical) format.", 400, 400).setVisible(true);
            return;
        }
    }
