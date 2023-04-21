package org.opensih.actions;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public interface IReporteAction {

    public void inicio();

    public void crerReportePDF();

    public void paint(OutputStream stream, Object object) throws IOException;

    public abstract void destroy();

    public long getTimeStamp();

    public File getChartFilePie();

    public void setChartFilePie(File chartFil);

    public File getChartFileXY();

    public void setChartFileXY(File chartFile);

    public File getChartFileBar();

    public void setChartFileBar(File chartFile);

    public File getChartFileLayeredBar();

    public void setChartFileLayeredBar(File chartFileLayeredBar);
}
