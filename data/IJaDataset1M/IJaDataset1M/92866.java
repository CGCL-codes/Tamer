package net.sf.genomeview.data.provider;

import java.util.ArrayList;
import net.sf.genomeview.core.Configuration;
import net.sf.genomeview.core.NoFailIterable;
import net.sf.genomeview.data.GenomeViewScheduler;
import net.sf.genomeview.data.Model;
import net.sf.genomeview.data.Task;
import net.sf.jannot.Data;
import net.sf.jannot.Entry;
import net.sf.jannot.Location;
import net.sf.jannot.bigwig.BigWigData;
import net.sf.jannot.pileup.DoublePile;
import net.sf.jannot.pileup.Pile;
import net.sf.jannot.tdf.TDFData;
import org.broad.igv.track.WindowFunction;

/**
 * 
 * @author Thomas Abeel
 * 
 */
public class BigWigProvider extends PileProvider {

    private BigWigData source;

    private Model model;

    public BigWigProvider(Entry e, BigWigData source, Model model) {
        super(model);
        this.source = source;
        this.model = model;
    }

    private ArrayList<Pile> buffer = new ArrayList<Pile>();

    private ArrayList<Status> status = new ArrayList<Status>();

    private int lastStart = -1;

    private int lastEnd = -1;

    private float maxPile;

    @Override
    public Iterable<Pile> get(final int start, final int end) {
        if (start >= lastStart && end <= lastEnd && (lastEnd - lastStart) <= 2 * (end - start)) return new NoFailIterable<Pile>(buffer);
        lastStart = start;
        lastEnd = end;
        buffer.clear();
        status.clear();
        status.add(new Status(false, true, false, start, end));
        final Status thisJob = status.get(0);
        Task t = new Task(new Location(start, end)) {

            @Override
            public void run() {
                if (!(start >= lastStart && end <= lastEnd && (lastEnd - lastStart) <= 2 * (end - start))) return;
                thisJob.setRunning();
                Iterable<Pile> fresh = source.get(start, end + 1);
                for (Pile p : fresh) {
                    float val = p.getTotal();
                    if (val > maxPile) maxPile = val;
                    buffer.add(p);
                }
                thisJob.setFinished();
                notifyListeners();
            }
        };
        GenomeViewScheduler.submit(t);
        return new NoFailIterable<Pile>(buffer);
    }

    @Override
    public double getMaxPile() {
        return maxPile;
    }

    public Iterable<Status> getStatus(int start, int end) {
        return status;
    }

    @Override
    public WindowFunction[] getWindowFunctions() {
        return new WindowFunction[] { WindowFunction.mean };
    }

    @Override
    public boolean isCurrentWindowFunction(WindowFunction wf) {
        return true;
    }

    @Override
    public void requestWindowFunction(WindowFunction wf) {
    }
}
