package uk.ac.imperial.ma.metric.explorations.calculus.differentiation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import java.awt.Color;
import uk.ac.imperial.ma.metric.plotting.*;
import uk.ac.imperial.ma.metric.parsing.*;
import uk.ac.imperial.ma.metric.util.MathsStyleHelper;
import uk.ac.imperial.ma.metric.gui.*;
import uk.ac.imperial.ma.metric.explorations.ExplorationInterface;

/**
 *
 *
 * @author Phil Ramsden
 * @author Daniel J. R. May
 * @version 0.2.0 26 March 2004
 */
public class NewParticleAnimationExploration extends JPanel implements ExplorationInterface, ActionListener, KeyListener, CaretListener, ItemListener, MathPainterPanelListener, Runnable {

    private static final ExtendedGridBagLayout mainPanelLayout = new ExtendedGridBagLayout();

    private static final ExtendedGridBagLayout contentSettingsPanelLayout = new ExtendedGridBagLayout();

    private static final ExtendedGridBagLayout windowSettingsPanelLayout = new ExtendedGridBagLayout();

    private static final ExtendedGridBagLayout controlPanelLayout = new ExtendedGridBagLayout();

    private static final ExtendedGridBagLayout stylePanelLayout = new ExtendedGridBagLayout();

    MathPainterPanel animationPanel;

    MathPainterPanel plotPanel;

    MathPainter animationMathPainter;

    MathPainter plotMathPainter;

    GridPlotter plotGridPlotter;

    AxesPlotter plotAxesPlotter;

    AxesPlotter animationAxesPlotter;

    CoordGenerator plotCoordGenerator;

    CurvePlotter plotCurvePlotter;

    PointPlotter plotPointPlotter;

    JPanel contentSettingsPanel = new JPanel(true);

    JPanel windowSettingsPanel = new JPanel(true);

    JPanel controlPanel = new JPanel(true);

    JFrame styleFrame = new JFrame();

    JPanel stylePanel = new JPanel(true);

    Dimension mainImageDimension;

    Dimension derivativeImageDimension;

    private static final int TEXT_SIZE = 16;

    private static final int MATH_SIZE = 18;

    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, TEXT_SIZE - 2);

    private static final Font FIELD_FONT = new Font("SansSerif", Font.PLAIN, TEXT_SIZE);

    protected int currentTextSize;

    protected int currentMathSize;

    protected Font currentLabelFont;

    protected Font currentFieldFont;

    String[] parseVariables = { "t" };

    Color[] defaultColorWheel = { Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN };

    int defaultColorWheelSize = 7;

    int defaultColorIndex = 0;

    int plotIndex = 1;

    JLabel funcLbl = new JLabel("x(t) =");

    JTextField funcTFd = new JTextField("100*t-4.9*t^2");

    JEditorPane funcJEP;

    JCheckBox inheritRangesCBx = new JCheckBox("inherit ranges", true);

    JLabel tMinLbl = new JLabel("tMin");

    JTextField tMinTFd = new JTextField("0.0");

    JLabel tMaxLbl = new JLabel("tMax");

    JTextField tMaxTFd = new JTextField("20.0        ");

    JRadioButton plotDisplacementRBtn = new JRadioButton("displacement", true);

    JRadioButton plotVelocityRBtn = new JRadioButton("velocity", false);

    JRadioButton plotAccelerationRBtn = new JRadioButton("acceleration", false);

    ButtonGroup plotOptionBGrp = new ButtonGroup();

    JButton localStylesBtn = new JButton("Plot Styles...");

    JFrame plotStyleFrame;

    JPanel plotStylePanel;

    JLabel plotLabelLbl = new JLabel("Plot label");

    JTextField plotLabelTFd = new JTextField("Plot");

    JCheckBox lineCBx = new JCheckBox("show line", true);

    JCheckBox pointCBx = new JCheckBox("show points", false);

    SelectColorButton plotColorBtn = new SelectColorButton(Color.BLACK, "Plot Colour...");

    String plotLabel;

    Color plotColor = Color.BLACK;

    boolean settingsAltered;

    boolean inheritRanges = true;

    String[] functionStrings = new String[3];

    String functionString;

    String[] legends = { "displacement (m) against time (s)", "velocity (m/s) against time (s)", "acceleration (m/s^2) against time (s)" };

    double tMin = 0.0;

    double tMax = 20.0;

    double runningYMin = 0.0;

    double runningYMax = 0.0;

    public static final int PLOT_DISPLACEMENT = 0;

    public static final int PLOT_VELOCITY = 1;

    public static final int PLOT_ACCELERATION = 2;

    int plotOption = PLOT_DISPLACEMENT;

    Parser xParser;

    Parser[] yParsers = new Parser[3];

    boolean stop;

    Thread runner = null;

    JLabel windowTMinLbl = new JLabel("tMin");

    JTextField windowTMinTFd = new JTextField("0.0        ");

    JLabel windowTMaxLbl = new JLabel("tMax");

    JTextField windowTMaxTFd = new JTextField("20.0        ");

    JLabel windowXMinLbl = new JLabel("xMin");

    JTextField windowXMinTFd = new JTextField("-150.0        ");

    JLabel windowXMaxLbl = new JLabel("xMax");

    JTextField windowXMaxTFd = new JTextField("600.0        ");

    JLabel windowVMinLbl = new JLabel("vMin");

    JTextField windowVMinTFd = new JTextField("-120.0        ");

    JLabel windowVMaxLbl = new JLabel("vMax");

    JTextField windowVMaxTFd = new JTextField("120.0        ");

    JLabel windowAMinLbl = new JLabel("aMin");

    JTextField windowAMinTFd = new JTextField("-20.0        ");

    JLabel windowAMaxLbl = new JLabel("aMax");

    JTextField windowAMaxTFd = new JTextField("10.0        ");

    JButton globalStylesBtn = new JButton("Global Styles...");

    private double windowTMin;

    private double windowTMax;

    private double windowXMin;

    private double windowXMax;

    private double windowVMin;

    private double windowVMax;

    private double windowAMin;

    private double windowAMax;

    private double windowYMin;

    private double windowYMax;

    JButton eraseCurvesBtn = new JButton("Erase");

    JButton drawCurvesBtn = new JButton("Draw");

    JButton autoscaleBtn = new JButton("Scale");

    JButton textPlusBtn = new JButton("   Text+   ");

    JButton textMinusBtn = new JButton("   Text-   ");

    public NewParticleAnimationExploration() {
        super(true);
        currentTextSize = TEXT_SIZE;
        currentMathSize = MATH_SIZE;
        currentLabelFont = LABEL_FONT;
        currentFieldFont = FIELD_FONT;
        animationPanel = new MathPainterPanel();
        plotPanel = new MathPainterPanel();
        contentSettingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Content Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT));
        contentSettingsPanel.setLayout(contentSettingsPanelLayout);
        try {
            this.funcJEP = new JEditorPane("text/html", MathsStyleHelper.getStyledHTML(TreeFormatter.format2D("100*t-4.9*t^2"), TEXT_SIZE, MATH_SIZE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        plotLabelLbl.setFont(LABEL_FONT);
        plotLabelTFd.setFont(FIELD_FONT);
        lineCBx.setFont(LABEL_FONT);
        pointCBx.setFont(LABEL_FONT);
        plotColorBtn.setButtonFont(LABEL_FONT);
        funcLbl.setFont(LABEL_FONT);
        inheritRangesCBx.setFont(LABEL_FONT);
        tMinLbl.setFont(LABEL_FONT);
        tMaxLbl.setFont(LABEL_FONT);
        plotDisplacementRBtn.setFont(LABEL_FONT);
        plotVelocityRBtn.setFont(LABEL_FONT);
        plotAccelerationRBtn.setFont(LABEL_FONT);
        localStylesBtn.setFont(LABEL_FONT);
        funcTFd.setFont(FIELD_FONT);
        tMinTFd.setFont(FIELD_FONT);
        tMaxTFd.setFont(FIELD_FONT);
        funcTFd.addCaretListener(this);
        funcTFd.addKeyListener(this);
        tMinTFd.addKeyListener(this);
        tMaxTFd.addKeyListener(this);
        plotLabelTFd.addKeyListener(this);
        inheritRangesCBx.addItemListener(this);
        plotDisplacementRBtn.addActionListener(this);
        plotVelocityRBtn.addActionListener(this);
        plotAccelerationRBtn.addActionListener(this);
        localStylesBtn.addActionListener(this);
        tMinTFd.setEditable(false);
        tMaxTFd.setEditable(false);
        plotOptionBGrp.add(plotDisplacementRBtn);
        plotOptionBGrp.add(plotVelocityRBtn);
        plotOptionBGrp.add(plotAccelerationRBtn);
        contentSettingsPanelLayout.add(funcLbl, contentSettingsPanel, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        contentSettingsPanelLayout.add(funcTFd, contentSettingsPanel, 1, 0, 3, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        contentSettingsPanelLayout.add(funcJEP, contentSettingsPanel, 1, 1, 3, 1, 100, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        contentSettingsPanelLayout.add(inheritRangesCBx, contentSettingsPanel, 1, 3, 3, 1, 100, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        contentSettingsPanelLayout.add(tMinLbl, contentSettingsPanel, 0, 4, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        contentSettingsPanelLayout.add(tMinTFd, contentSettingsPanel, 1, 4, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        contentSettingsPanelLayout.add(tMaxLbl, contentSettingsPanel, 2, 4, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        contentSettingsPanelLayout.add(tMaxTFd, contentSettingsPanel, 3, 4, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        contentSettingsPanelLayout.add(plotDisplacementRBtn, contentSettingsPanel, 0, 5, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        contentSettingsPanelLayout.add(plotVelocityRBtn, contentSettingsPanel, 1, 5, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        contentSettingsPanelLayout.add(plotAccelerationRBtn, contentSettingsPanel, 2, 5, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        contentSettingsPanelLayout.add(localStylesBtn, contentSettingsPanel, 1, 6, 2, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowSettingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Visual Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT));
        windowSettingsPanel.setLayout(windowSettingsPanelLayout);
        windowTMinLbl.setFont(LABEL_FONT);
        windowTMaxLbl.setFont(LABEL_FONT);
        windowXMinLbl.setFont(LABEL_FONT);
        windowXMaxLbl.setFont(LABEL_FONT);
        windowVMinLbl.setFont(LABEL_FONT);
        windowVMaxLbl.setFont(LABEL_FONT);
        windowAMinLbl.setFont(LABEL_FONT);
        windowAMaxLbl.setFont(LABEL_FONT);
        globalStylesBtn.setFont(LABEL_FONT);
        windowTMinTFd.setFont(FIELD_FONT);
        windowTMaxTFd.setFont(FIELD_FONT);
        windowXMinTFd.setFont(FIELD_FONT);
        windowXMaxTFd.setFont(FIELD_FONT);
        windowVMinTFd.setFont(FIELD_FONT);
        windowVMaxTFd.setFont(FIELD_FONT);
        windowAMinTFd.setFont(FIELD_FONT);
        windowAMaxTFd.setFont(FIELD_FONT);
        windowSettingsPanelLayout.add(windowTMinLbl, windowSettingsPanel, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        windowSettingsPanelLayout.add(windowTMinTFd, windowSettingsPanel, 1, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowSettingsPanelLayout.add(windowTMaxLbl, windowSettingsPanel, 2, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        windowSettingsPanelLayout.add(windowTMaxTFd, windowSettingsPanel, 3, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowSettingsPanelLayout.add(windowXMinLbl, windowSettingsPanel, 0, 1, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        windowSettingsPanelLayout.add(windowXMinTFd, windowSettingsPanel, 1, 1, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowSettingsPanelLayout.add(windowXMaxLbl, windowSettingsPanel, 2, 1, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        windowSettingsPanelLayout.add(windowXMaxTFd, windowSettingsPanel, 3, 1, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowSettingsPanelLayout.add(windowVMinLbl, windowSettingsPanel, 0, 2, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        windowSettingsPanelLayout.add(windowVMinTFd, windowSettingsPanel, 1, 2, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowSettingsPanelLayout.add(windowVMaxLbl, windowSettingsPanel, 2, 2, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        windowSettingsPanelLayout.add(windowVMaxTFd, windowSettingsPanel, 3, 2, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowSettingsPanelLayout.add(windowAMinLbl, windowSettingsPanel, 0, 3, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        windowSettingsPanelLayout.add(windowAMinTFd, windowSettingsPanel, 1, 3, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowSettingsPanelLayout.add(windowAMaxLbl, windowSettingsPanel, 2, 3, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.EAST);
        windowSettingsPanelLayout.add(windowAMaxTFd, windowSettingsPanel, 3, 3, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowSettingsPanelLayout.add(globalStylesBtn, windowSettingsPanel, 1, 4, 2, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        windowTMinTFd.addKeyListener(this);
        windowTMaxTFd.addKeyListener(this);
        windowXMinTFd.addKeyListener(this);
        windowXMaxTFd.addKeyListener(this);
        windowVMinTFd.addKeyListener(this);
        windowVMaxTFd.addKeyListener(this);
        windowAMinTFd.addKeyListener(this);
        windowAMaxTFd.addKeyListener(this);
        globalStylesBtn.addActionListener(this);
        controlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Controls", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT));
        controlPanel.setLayout(controlPanelLayout);
        drawCurvesBtn.setFont(LABEL_FONT);
        eraseCurvesBtn.setFont(LABEL_FONT);
        autoscaleBtn.setFont(LABEL_FONT);
        controlPanelLayout.add(drawCurvesBtn, controlPanel, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        controlPanelLayout.add(eraseCurvesBtn, controlPanel, 1, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        controlPanelLayout.add(autoscaleBtn, controlPanel, 2, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
        drawCurvesBtn.addActionListener(this);
        eraseCurvesBtn.addActionListener(this);
        autoscaleBtn.addActionListener(this);
        animationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Animation", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT));
        plotPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Plot", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT));
        this.setLayout(mainPanelLayout);
        mainPanelLayout.add(contentSettingsPanel, this, 0, 0, 1, 2, 10, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        mainPanelLayout.add(windowSettingsPanel, this, 0, 2, 1, 1, 10, 10, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        mainPanelLayout.add(animationPanel, this, 1, 0, 1, 1, 100, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        mainPanelLayout.add(plotPanel, this, 1, 1, 1, 1, 100, 100, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        mainPanelLayout.add(controlPanel, this, 1, 2, 1, 1, 100, 10, ExtendedGridBagLayout.BOTH, ExtendedGridBagLayout.CENTER);
        textPlusBtn.setFont(LABEL_FONT);
        textMinusBtn.setFont(LABEL_FONT);
        textPlusBtn.addActionListener(this);
        textMinusBtn.addActionListener(this);
    }

    public void mathPainterPanelResized() {
        draw();
    }

    public void setupGraphics() {
        this.plotCoordGenerator = new CoordGenerator(this.plotMathPainter);
        this.plotCurvePlotter = new CurvePlotter(this.plotMathPainter, this.plotCoordGenerator);
        this.plotPointPlotter = new PointPlotter(this.plotMathPainter, this.plotCoordGenerator);
    }

    public void plot(double tMin, double tRange) {
        try {
            plotPanel.setBase();
            plotCoordGenerator.setPoints(functionString, "t", tMin, tRange, 3);
            if (lineCBx.isSelected()) plotCurvePlotter.plot();
            if (pointCBx.isSelected()) plotPointPlotter.plot();
            plotPanel.update();
            animationPanel.clear();
            animationMathPainter.fillCircle(xParser.getValue(tMin + tRange), 0.0);
            animationPanel.update();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void init() {
        animationMathPainter = animationPanel.init();
        plotMathPainter = plotPanel.init();
        setupGraphics();
        plotPanel.setBase();
        plotPanel.update();
    }

    public void initializeGraphics() {
        windowTMin = new Double(windowTMinTFd.getText()).doubleValue();
        windowTMax = new Double(windowTMaxTFd.getText()).doubleValue();
        windowTMin = 1.1 * windowTMin - 0.1 * windowTMax;
        windowXMin = new Double(windowXMinTFd.getText()).doubleValue();
        windowXMax = new Double(windowXMaxTFd.getText()).doubleValue();
        windowVMin = new Double(windowVMinTFd.getText()).doubleValue();
        windowVMax = new Double(windowVMaxTFd.getText()).doubleValue();
        windowAMin = new Double(windowAMinTFd.getText()).doubleValue();
        windowAMax = new Double(windowAMaxTFd.getText()).doubleValue();
        switch(plotOption) {
            case 0:
                {
                    windowYMin = windowXMin;
                    windowYMax = windowXMax;
                }
                break;
            case 1:
                {
                    windowYMin = windowVMin;
                    windowYMax = windowVMax;
                }
                break;
            default:
                {
                    windowYMin = windowAMin;
                    windowYMax = windowAMax;
                }
        }
        animationAxesPlotter = new AxesPlotter(this.animationMathPainter);
        animationMathPainter.setMathArea(windowXMin, -1.0, windowXMax - windowXMin, 2.0);
        animationMathPainter.setScales();
        plotGridPlotter = new GridPlotter(plotMathPainter);
        plotAxesPlotter = new AxesPlotter(plotMathPainter);
        plotMathPainter.setMathArea(windowTMin, windowYMin, windowTMax - windowTMin, windowYMax - windowYMin);
        plotMathPainter.setScales();
    }

    public void drawGraphPaper() {
        animationMathPainter.setPaint(Color.white);
        animationMathPainter.fillRect(windowXMin, -1.0, windowXMax - windowXMin, 2.0);
        animationMathPainter.setPaint(Color.blue);
        animationAxesPlotter.drawTicks(2.0 * windowXMin - windowXMax, 0.0);
        animationMathPainter.setPaint(Color.black);
        plotMathPainter.setPaint(Color.white);
        plotMathPainter.fillRect(windowTMin, windowYMin, windowTMax - windowTMin, windowYMax - windowYMin);
        plotMathPainter.setPaint(Color.lightGray);
        plotGridPlotter.drawFineGrid();
        plotMathPainter.setPaint(Color.gray);
        plotGridPlotter.drawGrid();
        plotMathPainter.setPaint(Color.blue);
        plotAxesPlotter.drawAxes();
        plotAxesPlotter.drawTicks();
        plotMathPainter.setPaint(Color.black);
        plotMathPainter.drawString(legends[plotOption], (windowTMin + windowTMax) / 2.0, 0.9 * windowYMax + 0.1 * windowYMin, 80, 0);
    }

    public void draw() {
        setFunctionStrings();
        setTMaxMin();
        animationPanel.clearCompletely();
        plotPanel.clearCompletely();
        initializeGraphics();
        drawGraphPaper();
        animationPanel.setBase();
        try {
            animationMathPainter.fillCircle(xParser.getValue(0.0), 0.0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        animationPanel.update();
        plotPanel.setBase();
        plotPanel.update();
    }

    public void setInheritRanges(boolean inheritRanges) {
        this.inheritRanges = inheritRanges;
        tMinTFd.setEditable(!inheritRanges);
        tMaxTFd.setEditable(!inheritRanges);
        if (inheritRanges) {
            tMinTFd.setText(this.windowTMinTFd.getText());
            tMaxTFd.setText(this.windowTMaxTFd.getText());
        }
    }

    private String substitute(String str, char oldChar, String newSubString) {
        int nssLength = newSubString.length();
        StringBuffer strbuf = new StringBuffer(str);
        strbuf.ensureCapacity(nssLength * str.length());
        int j = 0;
        for (int i = 0; i < str.length(); i++) {
            char thisChar = str.charAt(i);
            if (thisChar == oldChar) {
                strbuf.insert(i + j, '(');
                strbuf.setCharAt(i + j + 1, newSubString.charAt(0));
                for (int k = 1; k < nssLength; k++) {
                    strbuf.insert(i + j + k + 1, newSubString.charAt(k));
                }
                ;
                strbuf.insert(i + j + nssLength + 1, ')');
                j += nssLength + 1;
            }
        }
        return new String(strbuf);
    }

    public Component getComponent() {
        return this;
    }

    public void caretUpdate(CaretEvent ce) {
        if (ce.getSource() == funcTFd) {
            String funcString = funcTFd.getText();
            try {
                funcJEP.setText("<html>" + TreeFormatter.format2D(funcString) + "</html>");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void itemStateChanged(ItemEvent ie) {
        if (ie.getSource() == inheritRangesCBx) {
            setInheritRanges(inheritRangesCBx.isSelected());
        } else this.draw();
    }

    public void keyPressed(KeyEvent ke) {
    }

    public void keyTyped(KeyEvent ke) {
    }

    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == ke.VK_ENTER) {
            draw();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == plotDisplacementRBtn) {
            plotOption = PLOT_DISPLACEMENT;
        } else if (ae.getSource() == plotVelocityRBtn) {
            plotOption = PLOT_VELOCITY;
        } else if (ae.getSource() == plotAccelerationRBtn) {
            plotOption = PLOT_ACCELERATION;
        } else if (ae.getSource() == localStylesBtn) {
            plotStyleFrame = new JFrame("Plot Style Settings");
            plotStylePanel = new JPanel();
            plotStylePanel.setLayout(stylePanelLayout);
            stylePanelLayout.add(plotLabelLbl, plotStylePanel, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(plotLabelTFd, plotStylePanel, 1, 0, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(lineCBx, plotStylePanel, 0, 2, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(pointCBx, plotStylePanel, 1, 2, 1, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(plotColorBtn, plotStylePanel, 0, 3, 2, 1, 100, 100, ExtendedGridBagLayout.HORIZONTAL, ExtendedGridBagLayout.CENTER);
            plotStyleFrame.getContentPane().setLayout(new GridLayout(1, 0));
            plotStyleFrame.add(plotStylePanel);
            plotStyleFrame.setSize(300, 300);
            plotStyleFrame.setVisible(true);
        } else if (ae.getSource() == eraseCurvesBtn) {
            stop = true;
            draw();
        } else if (ae.getSource() == drawCurvesBtn) {
            draw();
            if (runner != null && runner.isAlive()) {
                stop = true;
            }
            runner = null;
            if (runner == null) {
                runner = new Thread(this);
                runner.start();
            }
        } else if (ae.getSource() == globalStylesBtn) {
            styleFrame = new JFrame("Global Style Settings");
            stylePanel = new JPanel();
            stylePanel.setLayout(stylePanelLayout);
            stylePanelLayout.add(textPlusBtn, stylePanel, 0, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.CENTER);
            stylePanelLayout.add(textMinusBtn, stylePanel, 1, 0, 1, 1, 100, 100, ExtendedGridBagLayout.NONE, ExtendedGridBagLayout.CENTER);
            styleFrame.getContentPane().setLayout(new GridLayout(1, 0));
            styleFrame.add(stylePanel);
            styleFrame.setSize(300, 300);
            styleFrame.setVisible(true);
        } else if (ae.getSource() == textPlusBtn) {
            currentTextSize++;
            currentMathSize++;
            updateFonts();
        } else if (ae.getSource() == textMinusBtn) {
            currentTextSize--;
            currentMathSize--;
            updateFonts();
        } else if (ae.getSource() == autoscaleBtn) {
            windowYMin = 1.2 * runningYMin - 0.2 * runningYMax;
            windowYMax = 1.1 * runningYMax - 0.1 * runningYMin;
            windowYMin = Math.round(1000.0 * windowYMin) / 1000.0;
            windowYMax = Math.round(1000.0 * windowYMax) / 1000.0;
            switch(plotOption) {
                case 0:
                    {
                        windowXMin = windowYMin;
                        windowXMax = windowYMax;
                        windowXMinTFd.setText("" + windowXMin);
                        windowXMaxTFd.setText("" + windowXMax);
                    }
                    break;
                case 1:
                    {
                        windowVMin = windowYMin;
                        windowVMax = windowYMax;
                        windowVMinTFd.setText("" + windowVMin);
                        windowVMaxTFd.setText("" + windowVMax);
                    }
                    break;
                default:
                    {
                        windowAMin = windowYMin;
                        windowAMax = windowYMax;
                        windowAMinTFd.setText("" + windowAMin);
                        windowAMaxTFd.setText("" + windowAMax);
                    }
            }
            draw();
            if (runner != null && runner.isAlive()) {
                stop = true;
            }
            runner = null;
            if (runner == null) {
                runner = new Thread(this);
                runner.start();
            }
        }
    }

    public void updateFonts() {
        currentLabelFont = new Font("SansSerif", Font.BOLD, currentTextSize - 2);
        currentFieldFont = new Font("SansSerif", Font.PLAIN, currentTextSize);
        contentSettingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Content Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.currentLabelFont));
        update2DFormattingArea();
        funcLbl.setFont(this.currentLabelFont);
        inheritRangesCBx.setFont(this.currentLabelFont);
        tMinLbl.setFont(this.currentLabelFont);
        tMaxLbl.setFont(this.currentLabelFont);
        plotDisplacementRBtn.setFont(this.currentLabelFont);
        plotVelocityRBtn.setFont(this.currentLabelFont);
        plotAccelerationRBtn.setFont(this.currentLabelFont);
        localStylesBtn.setFont(this.currentLabelFont);
        funcTFd.setFont(this.currentFieldFont);
        tMinTFd.setFont(this.currentFieldFont);
        tMaxTFd.setFont(this.currentFieldFont);
        plotLabelLbl.setFont(this.currentLabelFont);
        plotLabelTFd.setFont(this.currentFieldFont);
        lineCBx.setFont(this.currentLabelFont);
        pointCBx.setFont(this.currentLabelFont);
        plotColorBtn.setButtonFont(this.currentLabelFont);
        windowSettingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Visual Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, currentLabelFont));
        windowTMinLbl.setFont(currentLabelFont);
        windowTMaxLbl.setFont(currentLabelFont);
        windowXMinLbl.setFont(currentLabelFont);
        windowXMaxLbl.setFont(currentLabelFont);
        windowVMinLbl.setFont(currentLabelFont);
        windowVMaxLbl.setFont(currentLabelFont);
        windowAMinLbl.setFont(currentLabelFont);
        windowAMaxLbl.setFont(currentLabelFont);
        globalStylesBtn.setFont(currentLabelFont);
        windowTMinTFd.setFont(currentFieldFont);
        windowTMaxTFd.setFont(currentFieldFont);
        windowXMinTFd.setFont(currentFieldFont);
        windowXMaxTFd.setFont(currentFieldFont);
        windowVMinTFd.setFont(currentFieldFont);
        windowVMaxTFd.setFont(currentFieldFont);
        windowAMinTFd.setFont(currentFieldFont);
        windowAMaxTFd.setFont(currentFieldFont);
        controlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Controls", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, currentLabelFont));
        drawCurvesBtn.setFont(currentLabelFont);
        eraseCurvesBtn.setFont(currentLabelFont);
        autoscaleBtn.setFont(currentLabelFont);
        textPlusBtn.setFont(currentLabelFont);
        textMinusBtn.setFont(currentLabelFont);
        animationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Animation", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, currentLabelFont));
        plotPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Plot", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, currentLabelFont));
    }

    protected void update2DFormattingArea() {
        String funcString = funcTFd.getText();
        try {
            funcJEP.setText(MathsStyleHelper.getStyledHTML(TreeFormatter.format2D(funcString), this.currentTextSize, this.currentMathSize));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void mathPainterPanelAction(MathPainterPanelEvent mppe) {
    }

    public void run() {
        stop = false;
        if (runner != null) {
            double tMax = this.tMax;
            double tMin = this.tMin;
            double tJump = 0.01 * (tMax - tMin);
            try {
                runningYMin = yParsers[plotOption].getValue(tMin);
                runningYMax = yParsers[plotOption].getValue(tMin);
                for (double t = tMin + tJump; t < tMax; t += tJump) {
                    runningYMin = Math.min(runningYMin, yParsers[plotOption].getValue(t));
                    runningYMax = Math.max(runningYMax, yParsers[plotOption].getValue(t));
                    plot(t - tJump, tJump);
                    Thread.sleep(5);
                    if (stop) {
                        break;
                    }
                }
            } catch (InterruptedException iex) {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setFunctionStrings() {
        functionStrings[0] = funcTFd.getText();
        String lss = "(t+0.001)";
        String rss = "(t-0.001)";
        String oldString = functionStrings[0];
        String upperS = substitute(oldString, 't', lss);
        String lowerS = substitute(oldString, 't', rss);
        functionStrings[1] = "((" + upperS + ")-(" + lowerS + "))/0.002";
        functionStrings[2] = "((" + upperS + ")-2*(" + oldString + ")+(" + lowerS + "))/0.000001";
        functionString = functionStrings[plotOption];
        try {
            yParsers[0] = new Parser(functionStrings[0], parseVariables);
            yParsers[1] = new Parser(functionStrings[1], parseVariables);
            yParsers[2] = new Parser(functionStrings[2], parseVariables);
            xParser = yParsers[0];
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTMaxMin() {
        if (inheritRanges) {
            tMinTFd.setText(windowTMinTFd.getText());
            tMaxTFd.setText(windowTMaxTFd.getText());
        }
        tMin = (new Double(tMinTFd.getText())).doubleValue();
        tMax = (new Double(tMaxTFd.getText())).doubleValue();
    }
}
