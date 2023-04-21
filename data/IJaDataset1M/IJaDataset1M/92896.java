package com.jguigen.standard;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.Locale;

public class NumericTextField extends JTextField implements NumericPlainDocument.InsertErrorListener {

    public NumericTextField() {
        this(null, 0, null);
    }

    public NumericTextField(String text, int columns, DecimalFormat format) {
        super(null, text, columns);
        NumericPlainDocument numericDoc = (NumericPlainDocument) getDocument();
        if (format != null) {
            numericDoc.setFormat(format);
        }
        numericDoc.addInsertErrorListener(this);
    }

    public NumericTextField(int columns, DecimalFormat format) {
        this(null, columns, format);
    }

    public NumericTextField(String text) {
        this(text, 0, null);
    }

    public NumericTextField(String text, int columns) {
        this(text, columns, null);
    }

    public void setFormat(DecimalFormat format) {
        ((NumericPlainDocument) getDocument()).setFormat(format);
    }

    public DecimalFormat getFormat() {
        return ((NumericPlainDocument) getDocument()).getFormat();
    }

    public void formatChanged() {
        setFormat(getFormat());
    }

    public Long getLongValue() throws ParseException {
        return ((NumericPlainDocument) getDocument()).getLongValue();
    }

    public Double getDoubleValue() throws ParseException {
        return ((NumericPlainDocument) getDocument()).getDoubleValue();
    }

    public Number getNumberValue() throws ParseException {
        return ((NumericPlainDocument) getDocument()).getNumberValue();
    }

    public int getIntegerValue() throws ParseException {
        return ((NumericPlainDocument) getDocument()).getIntegerValue();
    }

    public void setValue(Number number) {
        setText(getFormat().format(number));
    }

    public void setValue(long l) {
        setText(getFormat().format(l));
        ;
    }

    public void setValue(double d) {
        setText(getFormat().format(d));
    }

    public void setValue(int i) {
        NumberFormat integerFormatter;
        Locale loc = getLocale();
        integerFormatter = NumberFormat.getNumberInstance(loc);
        setText(integerFormatter.format(i));
    }

    public void normalize() throws ParseException {
        setText(getFormat().format(getNumberValue()));
    }

    public void insertFailed(NumericPlainDocument doc, int offset, String str, AttributeSet a) {
        Toolkit.getDefaultToolkit().beep();
    }

    protected Document createDefaultModel() {
        return new NumericPlainDocument();
    }

    public static void main(String[] args) {
        DecimalFormat format = new DecimalFormat("#,###.###");
        format.setGroupingUsed(true);
        format.setGroupingSize(3);
        format.setParseIntegerOnly(false);
        DecimalFormat format1 = new DecimalFormat("#,###");
        format1.setGroupingUsed(true);
        format1.setGroupingSize(3);
        format1.setParseIntegerOnly(true);
        format1.setNegativePrefix(null);
        JFrame f = new JFrame("Numeric Text Field Example");
        f.setDefaultCloseOperation(f.DO_NOTHING_ON_CLOSE);
        final NumericTextField tf = new NumericTextField(10, format);
        final NumericTextField tf1 = new NumericTextField(10, format1);
        tf.setValue((double) 123456.789);
        tf1.setValue(32);
        JLabel lbl = new JLabel("Type a number: ");
        f.getContentPane().add(tf, "East");
        f.getContentPane().add(lbl, "West");
        f.getContentPane().add(tf1, "South");
        tf.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                try {
                    tf.normalize();
                    Long l = tf.getLongValue();
                    System.out.println("Value is (Long)" + l);
                } catch (ParseException e1) {
                    try {
                        Double d = tf.getDoubleValue();
                        System.out.println("Value is (Double)" + d);
                    } catch (ParseException e2) {
                        System.out.println(e2);
                    }
                }
                try {
                    tf1.normalize();
                    int i = tf1.getIntegerValue();
                    System.out.println("value i = " + i);
                } catch (Exception ex) {
                    System.out.println("Integer Error:" + ex);
                }
            }
        });
        f.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                System.out.println("Window closed");
                try {
                    tf.normalize();
                    Long l = tf.getLongValue();
                    System.out.println("Value is (Long)" + l);
                } catch (ParseException e1) {
                    try {
                        Double d = tf.getDoubleValue();
                        System.out.println("Value is (Double)" + d);
                    } catch (ParseException e2) {
                        System.out.println(e2);
                    }
                }
                try {
                    tf1.normalize();
                    int i = tf1.getIntegerValue();
                    System.out.println("value i = " + i);
                } catch (Exception ex) {
                    System.out.println("Integer Error:" + ex);
                }
                System.exit(0);
            }
        });
        f.pack();
        f.setVisible(true);
    }
}
