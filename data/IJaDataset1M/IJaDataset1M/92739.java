package org.rjam.gui.comment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;
import org.jfree.ui.RefineryUtilities;
import org.rjam.gui.beans.Comment;

/**
 *
 * @author  Tony Bringardner
 */
public class CommentDisplayPanel extends javax.swing.JPanel implements MouseListener {

    private static final long serialVersionUID = 1L;

    public static final String PROP_DELETE = "DeleteComment";

    private Comment comment;

    /** Creates new form CommentDisplayPanel */
    public CommentDisplayPanel() {
        initComponents();
        addMouseListener(this);
        setComment(new Comment());
    }

    public CommentDisplayPanel(Comment comment) {
        this();
        setComment(comment);
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
        textArea.setLineWrap(false);
        textArea.setEditable(false);
        textArea.setBackground(comment.getBackground());
        textArea.setForeground(comment.getForeground());
        Font font = comment.getFont();
        if (font == null) {
            font = textArea.getFont();
        } else {
            textArea.setFont(font);
        }
        String text = comment.getText();
        if (text != null) {
            textArea.setText(comment.getText());
            int lines = text.split("\n").length + 2;
            Dimension dim = textScrollPane.getSize();
            FontMetrics fontMetrics = getFontMetrics(font);
            int height = fontMetrics.getLeading() + fontMetrics.getAscent() + fontMetrics.getDescent();
            dim.height = height * lines;
            textScrollPane.setPreferredSize(dim);
        }
        detailLabel.setText(comment.getDetails());
        String tmp = comment.getLevel();
        if (Comment.WARNING.equals(tmp)) {
            attributePanel.setBackground(Color.yellow);
        } else if (Comment.ATTENTION.equals(tmp)) {
            attributePanel.setBackground(Color.red);
        }
    }

    private void initComponents() {
        textScrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        attributePanel = new javax.swing.JPanel();
        deleteButton = new javax.swing.JButton();
        detailLabel = new javax.swing.JLabel();
        setLayout(new java.awt.BorderLayout());
        textArea.setColumns(20);
        textArea.setRows(2);
        textScrollPane.setViewportView(textArea);
        add(textScrollPane, java.awt.BorderLayout.CENTER);
        attributePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        deleteButton.setText("Remove Comment");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        attributePanel.add(deleteButton);
        detailLabel.setText("Details");
        attributePanel.add(detailLabel);
        add(attributePanel, java.awt.BorderLayout.NORTH);
    }

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
        firePropertyChange(PROP_DELETE, null, comment);
    }

    private javax.swing.JPanel attributePanel;

    private javax.swing.JButton deleteButton;

    private javax.swing.JLabel detailLabel;

    private javax.swing.JTextArea textArea;

    private javax.swing.JScrollPane textScrollPane;

    public static void main(String[] args) {
        final Frame frame = new Frame();
        Comment comment = new Comment();
        StringBuffer buf = new StringBuffer();
        for (int idx = 0; idx < 20; idx++) {
            buf.append("Line " + idx + "\n");
        }
        comment.setText(buf.toString());
        CommentDisplayPanel cmt = new CommentDisplayPanel(comment);
        frame.add(cmt);
        frame.setSize(900, 600);
        frame.pack();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                frame.setVisible(true);
            }
        });
    }

    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            CommentDialog cd = new CommentDialog(getComment(), this);
            RefineryUtilities.centerFrameOnScreen(cd);
            cd.setVisible(true);
            if (!cd.isCancel()) {
                setComment(comment);
            }
        }
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mousePressed(MouseEvent event) {
    }

    public void mouseReleased(MouseEvent event) {
    }
}
