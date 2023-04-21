    private void transform() {
        if (xs.getSettings() != null) {
            settings = xs.getSettings();
        }
        entOutput = os.getSelectedOption();
        redirectFileName = os.getFileName();
        if (entOutput != Globals.PLAIN_HTML) {
            jSplitPane1.setDividerLocation(1.0);
        } else if (jSplitPane1.getDividerLocation() > jSplitPane1.getMaximumDividerLocation()) {
            jSplitPane1.setDividerLocation(.6);
        }
        try {
            if (baos == null) baos = new java.io.ByteArrayOutputStream();
            long time = 0;
            switch(entOutput) {
                case Globals.RUN_EXTERNAL:
                case Globals.PLAIN_SAVE:
                case Globals.PLAIN_HTML:
                case Globals.TO_WINDOW:
                    if (txtXML.isReferenced()) {
                        java.io.FileInputStream fXML = new java.io.FileInputStream(txtXML.getCurrentFile());
                        java.io.ByteArrayInputStream bXSL = new java.io.ByteArrayInputStream(this.txtXSLT.getText().toString().getBytes());
                        time = tt.transform(fXML, bXSL, baos, txtXSLT.getCurrentFile(), txtXML.getCurrentFile());
                        fXML.close();
                        bXSL.close();
                        fXML = null;
                        bXSL = null;
                    } else {
                        java.io.ByteArrayInputStream bXML = new java.io.ByteArrayInputStream(this.txtXML.getText().toString().getBytes());
                        java.io.ByteArrayInputStream bXSL = new java.io.ByteArrayInputStream(this.txtXSLT.getText().toString().getBytes());
                        time = tt.transform(bXML, bXSL, baos, txtXSLT.getCurrentFile(), txtXML.getCurrentFile());
                        bXML.close();
                        bXSL.close();
                        bXML = bXSL = null;
                    }
                    break;
                case Globals.FOP_PREVIEW:
                case Globals.PDF_SAVE:
                    if (txtXML.isReferenced()) {
                        java.io.FileInputStream fXML = new java.io.FileInputStream(txtXML.getCurrentFile());
                        java.io.ByteArrayInputStream bXSL = new java.io.ByteArrayInputStream(this.txtXSLT.getText().toString().getBytes());
                        time = tt.FOPtransform(fXML, bXSL, baos, entOutput, txtXSLT.getCurrentFile(), txtXML.getCurrentFile());
                        fXML.close();
                        bXSL.close();
                        fXML = null;
                        bXSL = null;
                    } else {
                        java.io.ByteArrayInputStream bXML = new java.io.ByteArrayInputStream(this.txtXML.getText().toString().getBytes());
                        java.io.ByteArrayInputStream bXSL = new java.io.ByteArrayInputStream(this.txtXSLT.getText().toString().getBytes());
                        time = tt.FOPtransform(bXML, bXSL, baos, entOutput, txtXSLT.getCurrentFile(), txtXML.getCurrentFile());
                        bXML.close();
                        bXSL.close();
                        bXML = bXSL = null;
                    }
                    break;
            }
            java.io.FileOutputStream newFileStream = null;
            switch(entOutput) {
                case Globals.PLAIN_HTML:
                    bais = new ByteArrayInputStream(baos.toByteArray());
                    txtOutput.setText(bais);
                    setHTMLPaneWithPlainPaneData();
                    bais = null;
                    break;
                case Globals.PLAIN_SAVE:
                case Globals.PDF_SAVE:
                    newFileStream = new FileOutputStream(this.redirectFileName);
                    newFileStream.write(baos.toByteArray());
                    newFileStream.flush();
                    newFileStream.close();
                    newFileStream = null;
                    break;
                case Globals.TO_WINDOW:
                    Ent nEnt = new Ent(this.getTitle() + "- Results");
                    nEnt.buildXMLTools();
                    nEnt.getXMLPane().setText(new ByteArrayInputStream(baos.toByteArray()));
                    nEnt.show();
                    break;
                case Globals.RUN_EXTERNAL:
                    File tempOutFile = new File("tempOUTPUT");
                    newFileStream = new FileOutputStream(tempOutFile);
                    newFileStream.write(baos.toByteArray());
                    newFileStream.flush();
                    newFileStream.close();
                    String tempCommand = "";
                    if (this.redirectFileName.indexOf("$1") > 0) {
                        RE r = new RE("\\$1");
                        tempCommand = r.subst(redirectFileName, tempOutFile.getAbsolutePath());
                    }
                    Process p = Runtime.getRuntime().exec(tempCommand);
                    break;
                case Globals.FOP_PREVIEW:
                    baos.reset();
                    break;
            }
            ((javax.swing.border.TitledBorder) ((javax.swing.JPanel) txtOutput.getParent()).getBorder()).setTitle(Globals.getLabelLabel("took", Globals.WINDOW_BUNDLE) + " " + time + " ms " + "(" + settings.XSLTFactory + ")");
            ((javax.swing.JPanel) txtOutput.getParent()).repaint();
            baos.flush();
            baos.close();
            baos = null;
        } catch (Exception e) {
            System.err.println("Window::transform: " + e);
            e.printStackTrace(System.err);
        }
    }
