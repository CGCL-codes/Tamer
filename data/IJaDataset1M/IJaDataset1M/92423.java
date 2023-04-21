package guiStuff.accountGUIStuff;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import jimPreferences.PreferencePoint;
import upperAbstractionLayer.AccountChangeEvents;
import abstractionLayer.AccountSettings;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class AccountSettingsWindow extends javax.swing.JFrame implements ListSelectionListener, ActionListener, AccountChangeEvents {

    private static final long serialVersionUID = 1L;

    private JList jList1;

    private JButton jbEdit;

    private JButton jbMinus;

    private JButton jbPlus;

    public AccountSettingsWindow() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
            getContentPane().setLayout(thisLayout);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setMinimumSize(new java.awt.Dimension(578, 252));
            this.setTitle("Accounts");
            this.setResizable(false);
            {
                jList1 = new JList();
                jList1.setModel(new AccountListModel(new PreferencePoint()));
                jList1.setCellRenderer(new AccountRendererCreator());
                jList1.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
                jList1.addListSelectionListener(this);
            }
            {
                jbPlus = new JButton();
                jbPlus.setText("+");
                jbPlus.addActionListener(this);
            }
            {
                jbMinus = new JButton();
                jbMinus.setText("-");
                jbMinus.setEnabled(false);
                jbMinus.addActionListener(this);
            }
            {
                jbEdit = new JButton();
                jbEdit.setText("Edit");
                jbEdit.setEnabled(false);
                jbEdit.addActionListener(this);
            }
            thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().addContainerGap().addComponent(jList1, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jbPlus, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jbMinus, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jbEdit, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap());
            thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup().addContainerGap().addGroup(thisLayout.createParallelGroup().addComponent(jList1, GroupLayout.Alignment.LEADING, 0, 538, Short.MAX_VALUE).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(jbPlus, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jbMinus, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jbEdit, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE).addGap(251))).addContainerGap());
            pack();
            this.setSize(578, 252);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void valueChanged(ListSelectionEvent arg0) {
        boolean enabled = (jList1.getModel().getSize() >= jList1.getSelectedIndex() + 1) && (jList1.getSelectedIndex() != -1);
        jbMinus.setEnabled(enabled);
        jbEdit.setEnabled(enabled);
    }

    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == jbPlus) {
            PreferencePoint pp = new PreferencePoint();
            ModifyAccountWindow maw = new ModifyAccountWindow(pp.getNextAccountID(), this);
            maw.setVisible(true);
        } else if (arg0.getSource() == jbMinus) {
            PreferencePoint pp = new PreferencePoint();
            pp.deleteAccount(((AccountSettings) jList1.getModel().getElementAt(jList1.getSelectedIndex())).getID());
            this.accountChanged();
        } else if (arg0.getSource() == jbEdit) {
            ModifyAccountWindow maw = new ModifyAccountWindow(((AccountSettings) jList1.getModel().getElementAt(jList1.getSelectedIndex())), this);
            maw.setVisible(true);
        }
    }

    public void accountChanged() {
        ((AccountListModel) jList1.getModel()).update(new PreferencePoint());
        this.valueChanged(null);
    }
}
