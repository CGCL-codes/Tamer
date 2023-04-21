package edu.utah.seq.analysis;

import java.io.*;
import java.util.regex.*;
import java.util.*;
import util.gen.*;
import edu.utah.seq.data.*;
import edu.utah.seq.parsers.*;
import edu.utah.seq.useq.apps.Bar2USeq;
import trans.tpmap.*;

/**Takes PointData and generates sliding window scan statistics. Estimates FDRs using two different methods.
 * @author Nix
 * */
public class RNAEditingScanSeqs {

    private File[] pointDataDirs;

    private File saveDirectory;

    private int windowSize = 100;

    private int minimumNumberObservationsInWindow = 7;

    private boolean strandedAnalysis = false;

    private boolean plusStrand = true;

    private boolean minusStrand = true;

    private HashMap<String, PointData[]> plusPointData;

    private HashMap<String, PointData[]> minusPointData;

    private ArrayList<SmoothingWindowInfo> smiAL = new ArrayList<SmoothingWindowInfo>();

    private WindowMaker windowMaker;

    private int[][] windows;

    private SmoothingWindow[] smoothingWindow;

    private SmoothingWindowInfo[] smoothingWindowInfo;

    private File swiFile;

    private ArrayList<SmoothingWindow> allSmoothingWindowAL = new ArrayList<SmoothingWindow>();

    private String[] scoreNames;

    private String[] scoreDescriptions;

    private String[] scoreUnits;

    private String adapterName = "chrAdapter";

    private String chromosome;

    private PointData chromPointData = null;

    public RNAEditingScanSeqs(String[] args) {
        long startTime = System.currentTimeMillis();
        processArgs(args);
        if (strandedAnalysis) {
            plusStrand = true;
            minusStrand = false;
            scan();
            smiAL.clear();
            allSmoothingWindowAL.clear();
            plusStrand = false;
            minusStrand = true;
            scan();
        } else scan();
        double diffTime = ((double) (System.currentTimeMillis() - startTime)) / 1000;
        System.out.println("\nDone! " + Math.round(diffTime) + " seconds\n");
    }

    public void scan() {
        loadPointDataArrays();
        System.out.print("Scanning Chromosomes");
        String[] chromosomes = fetchAllChromosomes();
        for (int i = 0; i < chromosomes.length; i++) {
            chromosome = chromosomes[i];
            windowScanChromosome();
        }
        if (smiAL.size() == 0) Misc.printExit("\nNo windows found. Thresholds too strict?\n");
        smoothingWindowInfo = new SmoothingWindowInfo[smiAL.size()];
        smiAL.toArray(smoothingWindowInfo);
        System.out.println("Saving serialized window data...");
        String ext = "";
        if (plusStrand == true && minusStrand == true) ext = ""; else if (plusStrand) ext = "Plus"; else if (minusStrand) ext = "Minus";
        swiFile = new File(saveDirectory, "windowData" + windowSize + "bp" + ext + ".swi");
        IO.saveObject(swiFile, smoothingWindowInfo);
        writeBarFileGraphs(ext);
    }

    /**Fetches the names of all the chromosomes in the data.*/
    public String[] fetchAllChromosomes() {
        HashSet<String> c = new HashSet<String>();
        Iterator<String> it = plusPointData.keySet().iterator();
        while (it.hasNext()) c.add(it.next());
        it = minusPointData.keySet().iterator();
        while (it.hasNext()) c.add(it.next());
        return Misc.hashSetToStringArray(c);
    }

    /**Fetchs the data for a particular chromosome.*/
    public boolean fetchData() {
        PointData chromPlus = null;
        if (plusPointData.containsKey(chromosome) && plusStrand) chromPlus = PointData.combinePointData(plusPointData.get(chromosome), true);
        PointData chromMinus = null;
        if (minusPointData.containsKey(chromosome) && minusStrand) chromMinus = PointData.combinePointData(minusPointData.get(chromosome), true);
        chromPointData = null;
        if (plusStrand && minusStrand == false) chromPointData = chromPlus; else if (minusStrand && plusStrand == false) chromPointData = chromMinus; else {
            ArrayList<PointData> pdAL = new ArrayList<PointData>();
            if (chromPlus != null) pdAL.add(chromPlus);
            if (chromMinus != null) pdAL.add(chromMinus);
            if (pdAL.size() == 2) chromPointData = PointData.combinePointData(pdAL, true); else if (pdAL.size() == 1) chromPointData = pdAL.get(0);
        }
        if (chromPointData != null) return true;
        return false;
    }

    /**Window scans a chromosome collecting read count data and calculating binomial p-values.*/
    public void windowScanChromosome() {
        if (fetchData() == false) {
            System.out.println("\n\tSkipping " + chromosome + ". Failed to find paired PointData datasets.");
            return;
        }
        int[] positions = chromPointData.getPositions();
        makeWindows(positions);
        if (windows.length == 0) {
            System.out.println("\n\tSkipping " + chromosome + ". No windows found with minimum observations of " + minimumNumberObservationsInWindow + " within a window size of " + windowSize);
            return;
        }
        System.out.print(".");
        smoothingWindow = new SmoothingWindow[windows.length];
        scanWindows();
        saveWindowDataToSMIArrayList();
    }

    /**Saves window data to SMI Array.*/
    public void saveWindowDataToSMIArrayList() {
        Info info = chromPointData.getInfo();
        HashMap<String, String> notes = new HashMap<String, String>();
        notes.put(BarParser.WINDOW_SIZE, windowSize + "");
        notes.put(BarParser.DESCRIPTION_TAG, Misc.stringArrayToString(scoreNames, ","));
        notes.put(BarParser.UNIT_TAG, Misc.stringArrayToString(scoreUnits, ","));
        info.setNotes(notes);
        info.setStrand(".");
        smiAL.add(new SmoothingWindowInfo(smoothingWindow, info));
    }

    private void scanWindows() {
        smoothingWindow = new SmoothingWindow[windows.length];
        for (int i = 0; i < windows.length; i++) {
            Point[] p = chromPointData.fetchPoints(windows[i][0], windows[i][1]);
            float[] fractions = Point.extractScores(p);
            float pse = (float) Num.pseudoMedian(fractions);
            float[] scores = new float[] { pse, p.length };
            smoothingWindow[i] = new SmoothingWindow(windows[i][0], windows[i][1], scores);
            allSmoothingWindowAL.add(smoothingWindow[i]);
        }
    }

    /**Collects and calculates a bunch of stats re the PointData.*/
    private void loadPointDataArrays() {
        HashMap<String, ArrayList<PointData>>[] combo = PointData.fetchStrandedPointDataNoMerge(pointDataDirs);
        combo[0].remove(adapterName);
        combo[1].remove(adapterName);
        plusPointData = PointData.convertArrayList2Array(combo[0]);
        minusPointData = PointData.convertArrayList2Array(combo[1]);
    }

    /**Adds the toAdd to each int.*/
    public static void addShift(int[] positions, int toAdd) {
        for (int i = 0; i < positions.length; i++) {
            positions[i] += toAdd;
            if (positions[i] < 0) positions[i] = 0;
        }
    }

    /**Makes a common set of windows using merged positions.*/
    public void makeWindows(int[] positions) {
        windows = windowMaker.makeWindows(positions);
        for (int i = 0; i < windows.length; i++) {
            windows[i][0] = positions[windows[i][0]];
            windows[i][1] = positions[windows[i][1]] + 1;
        }
    }

    /**Writes stair step window bar graph files*/
    public void writeBarFileGraphs(String extension) {
        File sum = new File(saveDirectory, "Pse" + extension);
        sum.mkdir();
        for (int i = 0; i < smoothingWindowInfo.length; i++) {
            Info info = smoothingWindowInfo[i].getInfo();
            SmoothingWindow[] sm = smoothingWindowInfo[i].getSm();
            if (plusStrand && minusStrand == false) info.setStrand("+"); else if (minusStrand && plusStrand == false) info.setStrand("-"); else info.setStrand(".");
            saveSmoothedHeatMapData(0, sm, info, sum, "#FF00FF", false);
        }
        new Bar2USeq(sum, true);
    }

    /**Saves bar heatmap/ stairstep graph files*/
    public void saveSmoothedHeatMapData(int scoreIndex, SmoothingWindow[] sm, Info info, File dir, String color, boolean posNeg) {
        HashMap<String, String> map = info.getNotes();
        map.put(BarParser.GRAPH_TYPE_TAG, BarParser.GRAPH_TYPE_STAIRSTEP);
        map.put(BarParser.GRAPH_TYPE_COLOR_TAG, color);
        String fileNames = Misc.stringArrayToString(IO.fetchFileNames(pointDataDirs), ",");
        map.put(BarParser.SOURCE_TAG, fileNames);
        map.put(BarParser.WINDOW_SIZE, windowSize + "");
        map.put(BarParser.UNIT_TAG, scoreUnits[scoreIndex]);
        map.put(BarParser.DESCRIPTION_TAG, scoreDescriptions[scoreIndex]);
        info.setNotes(map);
        PointData pd;
        if (posNeg) {
            HeatMapMakerPosNeg hm = new HeatMapMakerPosNeg(scoreIndex, 0, 0);
            pd = hm.makeHeatMapPositionValues(sm);
        } else {
            HeatMapMaker hm = new HeatMapMaker(scoreIndex, 0);
            pd = hm.makeHeatMapPositionValues(sm, false);
        }
        pd.setInfo(info);
        pd.writePointData(dir);
        pd.nullPositionScoreArrays();
    }

    /**Sets score names/ descriptions/ units base on whether control data is present.*/
    public void setScoreStrings() {
        scoreNames = new String[] { "Pse", "Obs" };
        scoreDescriptions = new String[] { "Pseudo median of fraction edits", "# edits" };
        scoreUnits = new String[] { "Fraction", "Count" };
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            printDocs();
            System.exit(0);
        }
        new RNAEditingScanSeqs(args);
    }

    /**This method will process each argument and assign new variables*/
    public void processArgs(String[] args) {
        Pattern pat = Pattern.compile("-[a-z]");
        System.out.println("\nArguments: " + Misc.stringArrayToString(args, " ") + "\n");
        String strand = null;
        for (int i = 0; i < args.length; i++) {
            String lcArg = args[i].toLowerCase();
            Matcher mat = pat.matcher(lcArg);
            if (mat.matches()) {
                char test = args[i].charAt(1);
                try {
                    switch(test) {
                        case 'p':
                            pointDataDirs = IO.extractFiles(args[++i]);
                            break;
                        case 'r':
                            saveDirectory = new File(args[++i]);
                            break;
                        case 's':
                            strandedAnalysis = true;
                            break;
                        case 'w':
                            windowSize = Integer.parseInt(args[++i]);
                            break;
                        case 'm':
                            minimumNumberObservationsInWindow = Integer.parseInt(args[++i]);
                            break;
                        case 'h':
                            printDocs();
                            System.exit(0);
                        default:
                            Misc.printExit("\nProblem, unknown option! " + mat.group());
                    }
                } catch (Exception e) {
                    Misc.printExit("\nSorry, something doesn't look right with this parameter: -" + test + "\n");
                }
            }
        }
        if (pointDataDirs == null || pointDataDirs[0].isDirectory() == false) Misc.printExit("\nError: cannot find your treatment PointData directories(s)!\n");
        if (pointDataDirs.length == 1) {
            File[] otherDirs = IO.extractOnlyDirectories(pointDataDirs[0]);
            if (otherDirs != null && otherDirs.length > 0) pointDataDirs = otherDirs;
        }
        if (windowSize == 0) Misc.printExit("\nPlease enter a positive length for the window size if you are going to set the peak shift to 0\n");
        if (strand != null && (strand.equals("+") == false && strand.equals("-") == false)) {
            Misc.printExit("\nError: Enter either + or - for a stranded scan.\n");
        }
        plusStrand = (strand == null || strand.equals("+"));
        minusStrand = (strand == null || strand.equals("-"));
        windowMaker = new WindowMaker(windowSize, minimumNumberObservationsInWindow);
        if (saveDirectory == null) Misc.printExit("\nError: enter a directory text to save results.\n");
        if (saveDirectory.exists() == false) saveDirectory.mkdir();
        setScoreStrings();
    }

    public static void printDocs() {
        System.out.println("\n" + "**************************************************************************************\n" + "**                            RNA Editing Scan Seqs: Jan 2012                       **\n" + "**************************************************************************************\n" + "Beta.\n\n" + "Options:\n" + "-r Results directory, full path.\n" + "-p PointData directories, full path, comma delimited from the RNAEditingPileUpParser.\n" + "       These should contain stranded chromosome specific xxx_-/+_.bar.zip files. One\n" + "       can also provide a single directory that contains multiple PointData\n" + "       directories. These will be merged when scanning.\n" + "-s Perform stranded analysis, defaults to unstranded.\n" + "-w Window size, defaults to 100.\n" + "-m Minimum number observations in window, defaults to 7. \n" + "\n" + "Example: java -Xmx4G -jar pathTo/USeq/Apps/ \n\n" + "**************************************************************************************\n");
    }

    public SmoothingWindowInfo[] getSmoothingWindowInfo() {
        return smoothingWindowInfo;
    }

    public File getSwiFile() {
        return swiFile;
    }
}
