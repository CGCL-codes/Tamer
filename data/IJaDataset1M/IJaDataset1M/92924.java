package ejp.presenter.api.filters.parameters;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ejp.presenter.api.util.CustomLogger;
import ejp.presenter.gui.ColorChooserDialog;
import ejp.presenter.gui.Utils;

/**
 * A color distribution parameter, which associates a color to every value in
 * the <code>[0, 1]</code> interval.
 * 
 * The interval is actually split into by a user-provided divider and a color
 * can be associated to every sub-range. A convenience system allows to
 * automatically build up a gradient of colors between the first and the last
 * ones.
 * 
 * <p>
 * <b>Known issue </b>: due to Swing's error #4497301, the combo box might not
 * be selectable the second time the dialog is showed. There is currently no
 * acceptable workaround to this. The solution used here consists on limiting
 * the count of rows to a maximum.
 * 
 * @author Sebastien Vauclair
 * @version 1.0
 */
public class ColorDistributionParameter extends AbstractParameter implements ActionListener, ChangeListener {

    /**
   * Decimal numbers formatter used to render percents.
   */
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##0.0%");

    /**
   * Separator character between colors in text descriptions.
   */
    public static final String COLORS_SEPARATOR = ";";

    /**
   * Current list of colors.
   * 
   * <p>
   * Additional colors (after index <code>divisions</code> are kept in memory
   * for possible later usage.
   */
    protected final ArrayList colors = new ArrayList();

    /**
   * Number of divisions.
   * 
   * <p>
   * <code>number of divisions = number of colors - 1</code>.
   */
    protected int divisions = 0;

    /**
   * Spinner model.
   */
    protected final SpinnerNumberModel spinnerModel = new SpinnerNumberModel(divisions, 0, 1000, 1);

    /**
   * Avoids reentrancy into <code>divisionsUpdated()</code>.
   */
    protected boolean divisionsUpdatedIsWorking = false;

    /**
   * Current read-only state of the parameter.
   */
    protected boolean readOnly = false;

    /**
   * Divisions spinner.
   */
    protected final JSpinner jsDivisions;

    /**
   * Intervals combo box.
   */
    protected final JComboBox jcbIntervals;

    /**
   * Panel that displays current color distribution.
   */
    protected final ColorsDisplay cdSamples;

    /**
   * Creates a new color distribution parameter instance.
   * 
   * @param aName
   *          the parameter's name.
   * @param aTitle
   *          parameter's caption.
   * @param aToolTipText
   *          a tool-tip text.
   */
    public ColorDistributionParameter(String aName, String aTitle, String aToolTipText) {
        super(aName, aTitle, aToolTipText);
        colors.add(Color.black);
        jsDivisions = new JSpinner(spinnerModel);
        Utils.setCommonProperties(jsDivisions);
        jsDivisions.addChangeListener(this);
        addLine("Number of divisions", "Number of separations of the interval.", jsDivisions, null);
        jcbIntervals = new JComboBox();
        Utils.setCommonProperties(jcbIntervals);
        jcbIntervals.setRenderer(new ComboCellRenderer());
        jcbIntervals.setPrototypeDisplayValue(new ComboItem(1, 1, null));
        jcbIntervals.addActionListener(this);
        jcbIntervals.setMaximumRowCount(3);
        cdSamples = new ColorsDisplay(ColorsDisplay.DEFAULT_WIDTH, (int) jcbIntervals.getPreferredSize().getHeight());
        cdSamples.setToolTipText("Double-click to automatically build a gradient");
        cdSamples.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent aEvent) {
                if (!readOnly && aEvent.getButton() == MouseEvent.BUTTON1 && aEvent.getClickCount() == 2) buildGradient();
            }
        });
        addLine("Colors", "Colors of each sub-range.", jcbIntervals, cdSamples);
        divisionsUpdated();
    }

    /**
   * Sets the paramter as read-only.
   * 
   * <p>
   * Locks divisions spinner and no longer popups a color selection dialog when
   * an interval is selected.
   */
    public void setReadOnly() {
        readOnly = true;
        jsDivisions.setEnabled(false);
        cdSamples.setToolTipText(null);
        jcbIntervals.setRenderer(new ComboCellRenderer());
    }

    /**
   * Sets value from object.
   * 
   * Wrapper to <code>setValue(Color[])</code>.
   */
    public void setValue(Object aObject) throws ClassCastException {
        setValue((Color[]) aObject);
    }

    /**
   * Sets value from a color array.
   * 
   * Updates divisions and intervals values.
   * 
   * @param aColors
   *          a non-empty <code>Color[]</code> value.
   */
    public void setValue(Color[] aColors) {
        int nb = aColors.length;
        if (nb < 1) throw new IllegalArgumentException("Set of colors is empty");
        divisions = nb - 1;
        spinnerModel.setValue(new Integer(divisions));
        for (int i = 0; i < nb; i++) if (i < colors.size()) colors.set(i, aColors[i]); else colors.add(aColors[i]);
        divisionsUpdated();
    }

    /**
   * Sets value from text.
   * 
   * <p>
   * Input text is first tokenized using <code>COLORS_SEPARATOR</code>, then
   * each token is processed using <code>Color.decode(String)</code>.
   * 
   * @exception NumberFormatException
   *              if a number cannot be parsed to a color.
   * @exception IllegalStateException
   *              if an error occurs in the tokenizer.
   */
    public void setValueAsText(String aTextValue) throws NumberFormatException, IllegalStateException {
        StringTokenizer st = new StringTokenizer(aTextValue, COLORS_SEPARATOR, false);
        int nb = st.countTokens();
        Color[] tmpColors = new Color[nb];
        int i = 0;
        while (st.hasMoreTokens()) tmpColors[i++] = Color.decode(st.nextToken());
        if (i != nb) throw new IllegalStateException("Actual count of colors is smaller than expected");
        setValue(tmpColors);
    }

    /**
   * Gets value as an object.
   */
    public Object getValue() {
        return getColorsValue();
    }

    /**
   * Gets value as text.
   * 
   * <p>
   * Colors are exported as integers using <code>Color.getRGB()</code>. They
   * are separated by <code>COLORS_SEPARATOR</code> character.
   */
    public String getValueAsText() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i <= divisions; i++) {
            if (i > 0) buffer.append(COLORS_SEPARATOR);
            buffer.append(((Color) colors.get(i)).getRGB());
        }
        return buffer.toString();
    }

    /**
   * Get current value of the parameter.
   * 
   * @return a <code>Color[]</code> value.
   */
    public Color[] getColorsValue() {
        Color[] result = new Color[divisions + 1];
        for (int i = 0; i <= divisions; i++) result[i] = (Color) colors.get(i);
        return result;
    }

    /**
   * Gets the color currently associated to a real number.
   * 
   * @param aRatio
   *          a <code>double</code> value in the <code>[0, 1]</code>
   *          interval.
   * @return the associated <code>Color</code> value.
   */
    public Color colorForRatio(double aRatio) {
        int index = (aRatio == 1d ? divisions : (int) (aRatio * (divisions + 1)));
        return (Color) colors.get(index);
    }

    /**
   * Builds up a gradient between the first and last colors.
   */
    protected void buildGradient() {
        if (divisions < 2) return;
        Color first = (Color) colors.get(0);
        Color last = (Color) colors.get(divisions);
        int firstR = first.getRed();
        int firstG = first.getGreen();
        int firstB = first.getBlue();
        double deltaR = ((double) (last.getRed() - firstR)) / ((double) divisions);
        double deltaG = ((double) (last.getGreen() - firstG)) / ((double) divisions);
        double deltaB = ((double) (last.getBlue() - firstB)) / ((double) divisions);
        for (int i = 1; i <= divisions - 1; i++) colors.set(i, new Color(firstR + (int) (deltaR * i), firstG + (int) (deltaG * i), firstB + (int) (deltaB * i)));
        divisionsUpdated();
    }

    /**
   * Updates all fields to match a new divisions number.
   * 
   * <p>
   * When the number of divisions grows, previously selected colors are used or
   * if there are none, last color is repeated.
   * 
   * <p>
   * This method updates fields and calls <code>divisionsUpdated()</code> to
   * update the graphical representation.
   * 
   * @param aNewDivisions
   *          new <code>int</code> value.
   */
    protected void updateDivisions(int aNewDivisions) {
        int diff = aNewDivisions - divisions;
        if (diff == 0) return;
        if (diff > 0) {
            Object last = colors.get(divisions);
            for (int i = 0; i < diff; i++) if (divisions + i + 1 >= colors.size()) colors.add(last);
        }
        divisions = aNewDivisions;
        divisionsUpdated();
    }

    /**
   * Updates graphical representation of color intervals.
   * 
   * <p>
   * Uses <code>divisions</code> and <code>colors</code>.
   */
    protected void divisionsUpdated() {
        divisionsUpdatedIsWorking = true;
        int index = jcbIntervals.getSelectedIndex();
        jcbIntervals.removeAllItems();
        for (int i = 0; i <= divisions; i++) {
            double floor = ((double) i) / ((double) (divisions + 1));
            double ceil = ((double) (i + 1)) / ((double) (divisions + 1));
            jcbIntervals.addItem(new ComboItem(floor, ceil, (Color) colors.get(i)));
        }
        jcbIntervals.setSelectedIndex(Math.max(0, Math.min(index, divisions)));
        updateSelectedColor();
        cdSamples.invalidateGraphics();
        divisionsUpdatedIsWorking = false;
    }

    /**
   * Updates combo box to render currently selected colors.
   * 
   * <p>
   * This must be done because of an error in Swing's Metal UI, which does call
   * the renderer for the selected item but then overrides the foreground color.
   */
    protected void updateSelectedColor() {
        ComboItem item = (ComboItem) jcbIntervals.getSelectedItem();
        jcbIntervals.setForeground(item.color);
    }

    /**
   * Called when the spinner's value is considered as changed.
   * 
   * <p>
   * Updates intervals.
   */
    public void stateChanged(ChangeEvent aEvent) {
        Object src = aEvent.getSource();
        if (src == jsDivisions) updateDivisions(spinnerModel.getNumber().intValue()); else CustomLogger.INSTANCE.warning("Unable to handle state change event from unknwon source");
    }

    /**
   * Called when an interval is selected in combo box.
   */
    public void actionPerformed(ActionEvent aEvent) {
        Object src = aEvent.getSource();
        if (src == jcbIntervals) {
            if (!divisionsUpdatedIsWorking && ((aEvent.getModifiers() & InputEvent.BUTTON1_MASK) != 0)) {
                if (!readOnly) {
                    ComboItem item = (ComboItem) jcbIntervals.getSelectedItem();
                    ColorChooserDialog ccd = new ColorChooserDialog(dialog, item.color);
                    Color newColor = ccd.showDialog();
                    if (newColor != null) {
                        colors.set(jcbIntervals.getSelectedIndex(), newColor);
                        item.setColor(newColor);
                        cdSamples.invalidateGraphics();
                    }
                }
                updateSelectedColor();
            }
        } else CustomLogger.INSTANCE.warning("Unable to handle action event from unknown source");
    }

    /**
   * Graphical panel that displays the current color distribution.
   */
    protected class ColorsDisplay extends JPanel {

        /**
     * Default preferred width.
     */
        public static final int DEFAULT_WIDTH = 102;

        /**
     * Current width.
     */
        protected final int width;

        /**
     * Current height.
     */
        protected final int height;

        /**
     * Current dimension.
     */
        protected final Dimension dimension;

        /**
     * Image used to hold paint of color distribution.
     */
        protected final BufferedImage image;

        /**
     * Graphics used to paint color distribution.
     * 
     */
        protected Graphics graphics;

        /**
     * Creates a new <code>ColorsDisplay</code> instance.
     * 
     * @param aWidth
     *          preferred width.
     * @param aHeight
     *          preferred height.
     */
        public ColorsDisplay(int aWidth, int aHeight) {
            width = aWidth;
            height = aHeight;
            dimension = new Dimension(aWidth, aHeight);
            image = new BufferedImage(aWidth, aHeight, BufferedImage.TYPE_INT_RGB);
        }

        /**
     * Implementation of minimum size.
     */
        public Dimension getMinimumSize() {
            return dimension;
        }

        /**
     * Implementation of preferred size.
     */
        public Dimension getPreferredSize() {
            return dimension;
        }

        /**
     * Implementation of maximum size.
     */
        public Dimension getMaximumSize() {
            return dimension;
        }

        /**
     * Called to paint the panel to the specified graphics.
     * 
     * @param aGraphics
     *          a <code>Graphics</code> value.
     */
        protected void paintComponent(Graphics aGraphics) {
            super.paintComponent(aGraphics);
            if (graphics == null) {
                graphics = image.createGraphics();
                graphics.setColor(Color.black);
                graphics.drawRect(0, 0, width - 1, height - 1);
                int bottomY = height - 2;
                for (int x = 1; x <= width - 2; x++) {
                    int index = (int) (((double) x) / ((double) (width - 1)) * (divisions + 1));
                    Color color = (Color) colors.get(index);
                    graphics.setColor(color);
                    graphics.drawLine(x, 1, x, bottomY);
                }
            }
            aGraphics.drawImage(image, 0, 0, null);
        }

        /**
     * Invalidates current graphics and repaints it.
     */
        public void invalidateGraphics() {
            graphics = null;
            repaint();
        }
    }

    /**
   * An item (interval and associated color) of the combo box.
   */
    protected class ComboItem {

        /**
     * Floor value of the interval.
     */
        public final double floor;

        /**
     * Ceil value of the interval.
     */
        public final double ceil;

        /**
     * Text defining the interval.
     * 
     */
        public final String text;

        /**
     * Current color of the item.
     */
        protected Color color;

        /**
     * Creates a new <code>ComboItem</code> instance.
     * 
     * @param aFloor
     *          floor value.
     * @param aCeil
     *          ceil value.
     * @param aColor
     *          default color.
     */
        public ComboItem(double aFloor, double aCeil, Color aColor) {
            floor = aFloor;
            ceil = aCeil;
            color = aColor;
            text = renderRatio(aFloor) + " ≤ value " + (aCeil == 1.0d ? "≤" : "<") + " " + renderRatio(aCeil);
        }

        /**
     * Sets current color of the item.
     * 
     * @param aColor
     *          a <code>Color</code> value.
     */
        public void setColor(Color aColor) {
            color = aColor;
        }

        /**
     * Gets current color of the item.
     * 
     * @return a <code>Color</code> value.
     */
        public Color getColor() {
            return color;
        }

        /**
     * Returns the caption of the item.
     * 
     * @return a <code>String</code> value.
     */
        public String toString() {
            return text;
        }

        /**
     * Convenience method to display a decimal ratio as a percent.
     * 
     * @param aDouble
     *          a <code>double</code> value.
     * @return a <code>String</code> value.
     */
        protected String renderRatio(double aDouble) {
            return DECIMAL_FORMAT.format(aDouble);
        }
    }

    /**
   * Renderer for combo box cells (intervals).
   */
    protected class ComboCellRenderer extends DefaultListCellRenderer {

        /**
     * Renderer implementation.
     */
        public Component getListCellRendererComponent(JList aList, Object aValue, int aIndex, boolean aIsSelected, boolean aCellHasFocus) {
            Component result = super.getListCellRendererComponent(aList, aValue, aIndex, aIsSelected, aCellHasFocus);
            result.setForeground(((ComboItem) aValue).color);
            if (!readOnly && result instanceof JComponent) ((JComponent) result).setToolTipText("Left-click this item to change its color");
            return result;
        }
    }
}
