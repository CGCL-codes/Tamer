    private void writeOxDocXml() {
        try {
            File latexFile = new File(getLatex());
            File dvipngFile = new File(getDvipng());
            if (!latexFile.exists()) {
                Object[] options = { "Cancel", "Ignore" };
                int result = JOptionPane.showOptionDialog(frame, "Warning: the file " + latexFile + " does not exist.", "File does not exist", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (result != 1) return;
            }
            if (!dvipngFile.exists()) {
                Object[] options = { "Cancel", "Ignore" };
                int result = JOptionPane.showOptionDialog(frame, "Warning: the file " + dvipngFile + " does not exist.", "File does not exist", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (result != 1) return;
            }
            File oxdocXmlFile = new File(Config.userHomeConfigFile());
            File directory = oxdocXmlFile.getParentFile();
            if (oxdocXmlFile.exists()) {
                Object[] options = { "Ok", "Cancel" };
                int result = JOptionPane.showOptionDialog(frame, "The file " + oxdocXmlFile + " already exists.\n" + "Click OK to overwrite the file.", "File exists", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
                if (result != 0) return;
            }
            directory.mkdirs();
            Writer output = new BufferedWriter(new FileWriter(oxdocXmlFile));
            output.write("<oxdoc>\n");
            output.write("   <option name=\"latex\" value=\"" + getLatex() + "\" />\n");
            output.write("   <option name=\"dvipng\" value=\"" + getDvipng() + "\" />\n");
            String formulas = null;
            if (radioLatex.isSelected()) formulas = "latex"; else if (radioMathML.isSelected()) formulas = "mathml"; else if (radioPlainText.isSelected()) formulas = "plain";
            if (formulas != null) ;
            output.write("   <option name=\"formulas\" value=\"" + formulas + "\" />\n");
            output.write("</oxdoc>\n");
            output.close();
            Object[] options = { "No", "Yes" };
            int result = JOptionPane.showOptionDialog(frame, "Successfully wrote configuration into file " + oxdocXmlFile + ".\nDo you want to exit this setup program?", "Success", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
            if (result == 1) System.exit(1);
        } catch (Exception E) {
            showException(E);
        }
    }
