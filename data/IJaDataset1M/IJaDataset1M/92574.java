package fr.ana.anaballistics.gui.popup;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import fr.ana.anaballistics.component.io.ooxml.OOXMLWriter;
import fr.ana.anaballistics.core.graph2D.Energy2D;
import fr.ana.anaballistics.core.graph2D.Trajectory2D;
import fr.ana.anaballistics.core.graph2D.Velocity2D;
import fr.ana.anaballistics.gui.window.MainWindow;
import org.apache.poi.ss.usermodel.Workbook;

public class ExportToOOXML extends AbstractAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private MainWindow mw;

    private OOXMLWriter ex;

    private ExcelFormat v;

    public enum ExcelFormat {

        excel2003, excel2007
    }

    ;

    public ExportToOOXML(MainWindow mw, OOXMLWriter ex, ExcelFormat v) {
        this.mw = mw;
        this.ex = ex;
        this.v = v;
    }

    private void transferData() {
        ex.setExcelFormat(v);
        ArrayList<String> paramsName = new ArrayList<String>();
        paramsName.add(mw.getHeightRangeLabel().getText());
        paramsName.add(mw.getInitialSpeedLabel().getText());
        paramsName.add(mw.getMassBBLabel().getText());
        paramsName.add(mw.getDiameterBBLabel().getText());
        paramsName.add(mw.getHopUpLabel().getText());
        paramsName.add(mw.getLangMap().get("zmin+"));
        paramsName.add(mw.getLangMap().get("tair+"));
        paramsName.add(mw.getLangMap().get("deltaT+"));
        paramsName.add(mw.getLangMap().get("alt+"));
        paramsName.add(mw.getLangMap().get("lat+"));
        paramsName.add(mw.getLangMap().get("phi+"));
        ArrayList<Double> paramsValue = new ArrayList<Double>();
        paramsValue.add((Double) mw.getHeigtRangField().getModel().getValue());
        paramsValue.add((Double) mw.getIntialSPeedField().getModel().getValue());
        paramsValue.add((Double) mw.getMassBBField().getModel().getValue());
        paramsValue.add((Double) mw.getDiameterBBField().getModel().getValue());
        paramsValue.add((Double) mw.getHopUpField().getModel().getValue());
        paramsValue.add(mw.getCurrentUsedTDC().getZmin());
        paramsValue.add(mw.getCurrentUsedTDC().getTair());
        paramsValue.add(mw.getCurrentUsedTDC().getDeltaT());
        paramsValue.add(mw.getCurrentUsedTDC().getAlt());
        paramsValue.add(mw.getCurrentUsedTDC().getLat());
        paramsValue.add(mw.getCurrentUsedTDC().getPhi());
        ex.setParams(paramsName, paramsValue);
        Velocity2D v2d = new Velocity2D(mw.getCurrentDisplayedTrajectory());
        ex.setTrajectoryValues(mw.getCurrentDisplayedTrajectory().toString());
        ex.setVelocityValues(v2d.toString());
        ex.setAccelerationValues((new Trajectory2D(mw.getCurrentDisplayedTrajectory())).toString());
        ex.setEnergyValues((new Energy2D(v2d, (Double) mw.getMassBBField().getModel().getValue())).toString());
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);
        file.setMultiSelectionEnabled(false);
        file.setDialogTitle("AnaBallistics " + mw.getLangMap().get("Export"));
        if (file.showSaveDialog(mw) == JFileChooser.APPROVE_OPTION) {
            this.transferData();
            Workbook wb = ex.exportData();
            FileOutputStream out = null;
            try {
                if (v == ExcelFormat.excel2007) out = new FileOutputStream(file.getSelectedFile() + ".xlsx"); else if (v == ExcelFormat.excel2003) out = new FileOutputStream(file.getSelectedFile() + ".xls");
            } catch (FileNotFoundException e1) {
                JOptionPane.showMessageDialog(mw, mw.getLangMap().get("ExportFailed2"), mw.getLangMap().get("Export"), JOptionPane.ERROR_MESSAGE);
            }
            try {
                wb.write(out);
                out.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mw, mw.getLangMap().get("ExportFailed1"), mw.getLangMap().get("Export"), JOptionPane.ERROR_MESSAGE);
            }
            if (v == ExcelFormat.excel2007) JOptionPane.showMessageDialog(mw, "<html><center>" + mw.getLangMap().get("ExportFinished") + "</center></br><center>" + file.getSelectedFile().getName() + ".xlsx</center></html>", mw.getLangMap().get("Export"), JOptionPane.INFORMATION_MESSAGE); else if (v == ExcelFormat.excel2003) JOptionPane.showMessageDialog(mw, "<html><center>" + mw.getLangMap().get("ExportFinished") + "</center></br><center>" + file.getSelectedFile().getName() + ".xls</center></html>", mw.getLangMap().get("Export"), JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
