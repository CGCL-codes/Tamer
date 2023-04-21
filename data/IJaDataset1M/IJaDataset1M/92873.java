package pl.otros.logview.gui.actions;

import pl.otros.logview.LogData;
import pl.otros.logview.gui.LogDataTableModel;
import pl.otros.logview.gui.StatusObserver;
import pl.otros.logview.gui.markers.AutomaticMarker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AutomaticMarkUnamrkActionListener implements ActionListener {

    public static final boolean MODE_MARK = true;

    public static final boolean MODE_UNMARK = false;

    private LogDataTableModel dataTableModel;

    private AutomaticMarker automaticMarker;

    private boolean mode = true;

    private StatusObserver observer;

    public AutomaticMarkUnamrkActionListener(LogDataTableModel dataTableModel, AutomaticMarker automaticMarker, boolean mode, StatusObserver observer) {
        super();
        this.dataTableModel = dataTableModel;
        this.automaticMarker = automaticMarker;
        this.mode = mode;
        this.observer = observer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int size = dataTableModel.getRowCount();
        ArrayList<Integer> rows = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            LogData l = dataTableModel.getLogData(i);
            if (automaticMarker.toMark(l)) {
                rows.add(i);
            }
        }
        int[] r = new int[rows.size()];
        for (int i = 0; i < r.length; i++) {
            r[i] = rows.get(i);
        }
        if (mode == MODE_MARK) {
            dataTableModel.markRows(automaticMarker.getColors(), r);
            observer.updateStatus(r.length + " rows marked by marker \"" + automaticMarker.getName() + "\"");
        } else {
            dataTableModel.unmarkRows(r);
            observer.updateStatus(r.length + " rows unmarked by marker \"" + automaticMarker.getName() + "\"");
        }
    }

    public LogDataTableModel getDataTableModel() {
        return dataTableModel;
    }

    public void setDataTableModel(LogDataTableModel dataTableModel) {
        this.dataTableModel = dataTableModel;
    }

    public AutomaticMarker getAutomaticMarker() {
        return automaticMarker;
    }

    public void setAutomaticMarker(AutomaticMarker automaticMarker) {
        this.automaticMarker = automaticMarker;
    }

    public boolean isMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }
}
