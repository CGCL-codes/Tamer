 */
public class PlotApplet extends Applet {

    /** Return a string describing this applet.
     */
    public String getAppletInfo() {
        return "PlotApplet 2.0: A data plotter.\n" + "By: Edward A. Lee, eal@eecs.berkeley.edu and\n " + "Christopher Hylands, cxh@eecs.berkeley.edu\n" + "($Id: PlotApplet.java,v 1.1.1.1 2009-06-15 16:04:31 elespuru Exp $)";
    }

    /** Return information about parameters.
     */
    public String[][] getParameterInfo() {
        String pinfo[][] = { { "background", "hexcolor value", "background color" }, { "foreground", "hexcolor value", "foreground color" }, { "dataurl", "url", "the URL of the data to plot" }, { "pxgraphargs", "args", "pxgraph style command line arguments" } };
        return pinfo;
    }

    /** Initialize the applet.  Read the applet parameters.
     */
    public void init() {
        super.init();
        setLayout(new BorderLayout());
        if (_myPlot == null) {
            _myPlot = newPlot();
        }
        add("Center", plot());
        int width, height;
        String widthspec = getParameter("width");
        if (widthspec != null) width = Integer.parseInt(widthspec); else width = 400;
        String heightspec = getParameter("height");
        if (heightspec != null) height = Integer.parseInt(heightspec); else height = 400;
        plot().setSize(width, height);
        plot().setButtons(true);
        Color background = Color.white;
        String colorspec = getParameter("background");
        if (colorspec != null) background = PlotBox.getColorByName(colorspec);
        setBackground(background);
        plot().setBackground(background);
        Color foreground = Color.black;
        colorspec = getParameter("foreground");
        if (colorspec != null) foreground = PlotBox.getColorByName(colorspec);
        setForeground(foreground);
        plot().setForeground(foreground);
        plot().setVisible(true);
        String pxgraphargs = null;
        pxgraphargs = getParameter("pxgraphargs");
        if (pxgraphargs != null) {
            try {
                showStatus("Reading arguments");
                plot()._documentBase = getDocumentBase();
                plot().parsePxgraphargs(pxgraphargs);
                showStatus("Done");
            } catch (CmdLineArgException e) {
                System.err.println("PlotApplet: failed to parse `" + pxgraphargs + "': " + e);
            } catch (FileNotFoundException e) {
                System.err.println("PlotApplet: file not found: " + e);
            } catch (IOException e) {
                System.err.println("PlotApplet: error reading input file: " + e);
