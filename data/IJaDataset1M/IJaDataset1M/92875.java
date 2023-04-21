package jmetric.util;

import jmetric.ui.ProgressInterface;

public class JMetricCommandException extends JMetricException {

    public JMetricCommandException() {
        super();
    }

    public JMetricCommandException(String s) {
        super(s);
    }

    public void showErrorMessage(ProgressInterface tc) {
        tc.displayInStatus("Invalid Command:");
        tc.displayInStatus("Commands include:");
        tc.displayInStatus("    To add a file use 'a'.");
        tc.displayInStatus("    To clear the current project use 'clr'");
        tc.displayInStatus("    to quit JMetric use 'exit' or 'quit' or 'q'");
        tc.displayInStatus("    to change the working directory use 'cwd' or 'cd'");
        tc.displayInStatus("    to export use 'export'");
        tc.displayInStatus("    to view the charts use 'chart' (NOTE: only in GUI mode)");
        tc.displayInStatus("    to load a file list use 'load' or 'l'");
    }
}
