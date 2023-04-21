package net.pandoragames.far.ui.swing.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import net.pandoragames.far.ui.FARConfig;
import net.pandoragames.far.ui.model.AbstractFileRepository;
import net.pandoragames.far.ui.model.FindForm;
import net.pandoragames.far.ui.model.MessageBox;
import net.pandoragames.far.ui.model.RenameForm;
import net.pandoragames.far.ui.model.ReplaceForm;
import net.pandoragames.far.ui.swing.ComponentRepository;
import net.pandoragames.far.ui.swing.SwingConfig;
import net.pandoragames.far.ui.swing.component.FileSetTableModel;
import net.pandoragames.far.ui.swing.component.MessageLabel;
import net.pandoragames.far.ui.swing.component.TwoComponentsPanel;
import net.pandoragames.far.ui.swing.component.listener.BackupFlagListener;
import net.pandoragames.far.ui.swing.component.listener.BrowseButtonListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JDialog for application configuration.
 *
 * @author Olivier Wehner at 02/12/2009
 * <!-- copyright note -->
 */
public class SettingsDialog extends SubWindow {

    private SwingConfig swingConfig;

    private FindForm findForm;

    private ReplaceForm replaceForm;

    private RenameForm renameForm;

    private FileSetTableModel tableModel;

    private Charset defaultCharacterSet;

    private List<Charset> addedCharsets;

    private char groupReference;

    private boolean doBackup;

    private ButtonGroup fileInfoOptions = new ButtonGroup();

    private boolean showBytes;

    private File backupDirectory;

    private BackupFlagListener backupFlagListener;

    private ItemEvent backupFlagEvent;

    private Log logger = LogFactory.getLog(this.getClass());

    /**
	 * Constructor takes root window, the application configuration and the
	 * component repository.
	 * @param owner root window
	 * @param config the configuration object where changes are applied to
	 * @param repository holding listeners and other shared components.
	 */
    public SettingsDialog(JFrame owner, SwingConfig config, ComponentRepository repository) {
        super(owner, config.getLocalizer().localize("menu.settings"), true);
        swingConfig = config;
        findForm = repository.getFindForm();
        replaceForm = repository.getReplaceForm();
        renameForm = repository.getRenameForm();
        tableModel = repository.getTableModel();
        groupReference = replaceForm.getGroupReference();
        defaultCharacterSet = config.getDefaultCharset();
        addedCharsets = new ArrayList<Charset>();
        doBackup = replaceForm.isDoBackup();
        showBytes = config.isShowPlainBytes();
        backupFlagListener = repository.getBackupFlagListener();
        backupDirectory = replaceForm.getBackupDirectory();
        init();
    }

    private void init() {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setResizable(false);
        JPanel basePanel = new JPanel();
        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));
        basePanel.setBorder(BorderFactory.createEmptyBorder(0, SwingConfig.PADDING, SwingConfig.PADDING, SwingConfig.PADDING));
        MessageLabel errorField = new MessageLabel();
        errorField.setMinimumSize(new Dimension(100, swingConfig.getStandardComponentHight()));
        errorField.setBorder(BorderFactory.createEmptyBorder(1, SwingConfig.PADDING, 2, SwingConfig.PADDING));
        TwoComponentsPanel lineError = new TwoComponentsPanel(errorField, Box.createRigidArea(new Dimension(1, swingConfig.getStandardComponentHight())));
        lineError.setAlignmentX(Component.LEFT_ALIGNMENT);
        basePanel.add(lineError);
        JLabel labelCharset = new JLabel(swingConfig.getLocalizer().localize("label.default-characterset"));
        labelCharset.setAlignmentX(Component.LEFT_ALIGNMENT);
        basePanel.add(labelCharset);
        JComboBox listCharset = new JComboBox(swingConfig.getCharsetList().toArray());
        listCharset.setAlignmentX(Component.LEFT_ALIGNMENT);
        listCharset.setSelectedItem(swingConfig.getDefaultCharset());
        listCharset.setEditable(true);
        listCharset.setMaximumSize(new Dimension(SwingConfig.COMPONENT_WIDTH_MAX, swingConfig.getStandardComponentHight()));
        listCharset.addActionListener(new CharacterSetListener(errorField));
        listCharset.setEditor(new CharacterSetEditor(errorField));
        basePanel.add(listCharset);
        basePanel.add(Box.createRigidArea(new Dimension(1, SwingConfig.PADDING)));
        JPanel selectorPanel = new JPanel();
        selectorPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        selectorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel labelSelector = new JLabel(swingConfig.getLocalizer().localize("label.group-ref-indicator"));
        selectorPanel.add(labelSelector);
        JComboBox selectorBox = new JComboBox(FARConfig.GROUPREFINDICATORLIST);
        selectorBox.setSelectedItem(Character.toString(groupReference));
        selectorBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JComboBox cbox = (JComboBox) event.getSource();
                String indicator = (String) cbox.getSelectedItem();
                groupReference = indicator.charAt(0);
            }
        });
        selectorPanel.add(selectorBox);
        basePanel.add(selectorPanel);
        basePanel.add(Box.createRigidArea(new Dimension(1, SwingConfig.PADDING)));
        JCheckBox doBackupFlag = new JCheckBox(swingConfig.getLocalizer().localize("label.create-backup"));
        doBackupFlag.setAlignmentX(Component.LEFT_ALIGNMENT);
        doBackupFlag.setHorizontalTextPosition(SwingConstants.LEADING);
        doBackupFlag.setSelected(replaceForm.isDoBackup());
        doBackupFlag.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent event) {
                doBackup = ItemEvent.SELECTED == event.getStateChange();
                backupFlagEvent = event;
            }
        });
        basePanel.add(doBackupFlag);
        JTextField backupDirPathTextField = new JTextField();
        backupDirPathTextField.setPreferredSize(new Dimension(SwingConfig.COMPONENT_WIDTH, swingConfig.getStandardComponentHight()));
        backupDirPathTextField.setMaximumSize(new Dimension(SwingConfig.COMPONENT_WIDTH_MAX, swingConfig.getStandardComponentHight()));
        backupDirPathTextField.setText(backupDirectory.getPath());
        backupDirPathTextField.setToolTipText(backupDirectory.getPath());
        backupDirPathTextField.setEditable(false);
        JButton openBaseDirFileChooserButton = new JButton(swingConfig.getLocalizer().localize("button.browse"));
        BrowseButtonListener backupDirButtonListener = new BrowseButtonListener(backupDirPathTextField, new BackUpDirectoryRepository(swingConfig, findForm, replaceForm, errorField), swingConfig.getLocalizer().localize("label.choose-backup-directory"));
        openBaseDirFileChooserButton.addActionListener(backupDirButtonListener);
        TwoComponentsPanel lineBaseDir = new TwoComponentsPanel(backupDirPathTextField, openBaseDirFileChooserButton);
        lineBaseDir.setAlignmentX(Component.LEFT_ALIGNMENT);
        basePanel.add(lineBaseDir);
        basePanel.add(Box.createRigidArea(new Dimension(1, SwingConfig.PADDING)));
        JPanel fileInfoPanel = new JPanel();
        fileInfoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), swingConfig.getLocalizer().localize("label.default-file-info")));
        fileInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fileInfoPanel.setLayout(new BoxLayout(fileInfoPanel, BoxLayout.Y_AXIS));
        JLabel fileInfoLabel = new JLabel(swingConfig.getLocalizer().localize("message.displayed-in-info-column"));
        fileInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fileInfoPanel.add(fileInfoLabel);
        fileInfoPanel.add(Box.createHorizontalGlue());
        JRadioButton nothingRadio = new JRadioButton(swingConfig.getLocalizer().localize("label.nothing"));
        nothingRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        nothingRadio.setActionCommand(SwingConfig.DefaultFileInfo.NOTHING.name());
        nothingRadio.setSelected(swingConfig.getDefaultFileInfo() == SwingConfig.DefaultFileInfo.NOTHING);
        fileInfoOptions.add(nothingRadio);
        fileInfoPanel.add(nothingRadio);
        JRadioButton readOnlyRadio = new JRadioButton(swingConfig.getLocalizer().localize("label.read-only-warning"));
        readOnlyRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        readOnlyRadio.setActionCommand(SwingConfig.DefaultFileInfo.READONLY.name());
        readOnlyRadio.setSelected(swingConfig.getDefaultFileInfo() == SwingConfig.DefaultFileInfo.READONLY);
        fileInfoOptions.add(readOnlyRadio);
        fileInfoPanel.add(readOnlyRadio);
        JRadioButton sizeRadio = new JRadioButton(swingConfig.getLocalizer().localize("label.filesize"));
        sizeRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        sizeRadio.setActionCommand(SwingConfig.DefaultFileInfo.SIZE.name());
        sizeRadio.setSelected(swingConfig.getDefaultFileInfo() == SwingConfig.DefaultFileInfo.SIZE);
        fileInfoOptions.add(sizeRadio);
        fileInfoPanel.add(sizeRadio);
        JCheckBox showPlainBytesFlag = new JCheckBox("  " + swingConfig.getLocalizer().localize("label.show-plain-bytes"));
        showPlainBytesFlag.setAlignmentX(Component.LEFT_ALIGNMENT);
        showPlainBytesFlag.setHorizontalTextPosition(SwingConstants.LEADING);
        showPlainBytesFlag.setSelected(swingConfig.isShowPlainBytes());
        showPlainBytesFlag.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent event) {
                showBytes = ItemEvent.SELECTED == event.getStateChange();
            }
        });
        fileInfoPanel.add(showPlainBytesFlag);
        JRadioButton lastModifiedRadio = new JRadioButton(swingConfig.getLocalizer().localize("label.last-modified"));
        lastModifiedRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        lastModifiedRadio.setActionCommand(SwingConfig.DefaultFileInfo.LAST_MODIFIED.name());
        lastModifiedRadio.setSelected(swingConfig.getDefaultFileInfo() == SwingConfig.DefaultFileInfo.LAST_MODIFIED);
        fileInfoOptions.add(lastModifiedRadio);
        fileInfoPanel.add(lastModifiedRadio);
        basePanel.add(fileInfoPanel);
        JPanel buttonPannel = new JPanel();
        buttonPannel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPannel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        JButton cancelButton = new JButton(swingConfig.getLocalizer().localize("button.cancel"));
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                SettingsDialog.this.dispose();
            }
        });
        buttonPannel.add(cancelButton);
        JButton saveButton = new JButton(swingConfig.getLocalizer().localize("button.save"));
        saveButton.addActionListener(new SaveButtonListener());
        buttonPannel.add(saveButton);
        this.getRootPane().setDefaultButton(saveButton);
        this.add(basePanel);
        this.add(buttonPannel);
        placeOnScreen(swingConfig.getScreenCenter());
    }

    class CharacterSetListener implements ActionListener {

        private MessageBox messageBox;

        public CharacterSetListener(MessageBox errorSink) {
            messageBox = errorSink;
        }

        public void actionPerformed(ActionEvent event) {
            JComboBox cbox = (JComboBox) event.getSource();
            defaultCharacterSet = (Charset) cbox.getSelectedItem();
            messageBox.clear();
        }
    }

    /**
	 * Editor component for the FileNamePattern combobox.
	 * @author Olivier Wehner at 03.03.2008
 * <!-- copyright note --> 
	 */
    class CharacterSetEditor extends JTextField implements ComboBoxEditor {

        private Charset lastUnsavedValue;

        private MessageBox messageBox;

        public CharacterSetEditor(MessageBox errorSink) {
            messageBox = errorSink;
        }

        public Component getEditorComponent() {
            return this;
        }

        public Object getItem() {
            String pattern = this.getText().trim();
            if (!pattern.equals(lastUnsavedValue.name())) {
                try {
                    if (Charset.isSupported(pattern)) {
                        lastUnsavedValue = Charset.forName(pattern);
                        defaultCharacterSet = lastUnsavedValue;
                        if ((!addedCharsets.contains(lastUnsavedValue)) && (!swingConfig.getCharsetList().contains(lastUnsavedValue))) {
                            addedCharsets.add(lastUnsavedValue);
                        }
                    } else {
                        defaultCharacterSet = null;
                        messageBox.error(swingConfig.getLocalizer().localize("message.charset-not-supported"));
                    }
                } catch (IllegalCharsetNameException incx) {
                    defaultCharacterSet = null;
                    messageBox.error(swingConfig.getLocalizer().localize("message.invalid-charset-name"));
                }
            }
            return lastUnsavedValue;
        }

        public void setItem(Object anObject) {
            Charset charset = (Charset) anObject;
            lastUnsavedValue = charset;
            setText(charset.name());
        }
    }

    /**
	 * Tests if the new backup directory can be accepted.
	 */
    class BackUpDirectoryRepository extends AbstractFileRepository {

        public BackUpDirectoryRepository(FARConfig cfg, FindForm find, ReplaceForm replace, MessageBox errorsink) {
            super(cfg, find, replace, errorsink);
        }

        public File getFile() {
            return farconfig.getBackupDirectory();
        }

        public boolean setFile(File file) {
            if (isSubdirectory(file, findForm.getBaseDirectory())) {
                messageBox.error(farconfig.getLocalizer().localize("message.nested-backup-parent"));
                return false;
            } else if (isSubdirectory(findForm.getBaseDirectory(), file)) {
                messageBox.error(farconfig.getLocalizer().localize("message.nested-backup-child"));
                return false;
            } else if (file.canWrite()) {
                backupDirectory = file;
                return true;
            } else {
                String message = farconfig.getLocalizer().localize("message.directory-not-writable", new Object[] { file.getPath() });
                messageBox.error(message);
                return false;
            }
        }
    }

    /**
	 * Changes the Configuration and Form objects and informs any interested listener.
	 */
    class SaveButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            if (canSave()) {
                swingConfig.setDefaultCharset(defaultCharacterSet);
                swingConfig.getCharsetList().addAll(addedCharsets);
                swingConfig.setBackupDirectory(backupDirectory);
                boolean infoSetHasChanged = (!fileInfoOptions.getSelection().getActionCommand().equals(swingConfig.getDefaultFileInfo().name()) || (fileInfoOptions.getSelection().getActionCommand().equals(SwingConfig.DefaultFileInfo.SIZE.name()) && showBytes != swingConfig.isShowPlainBytes()));
                swingConfig.setDefaultFileInfo(fileInfoOptions.getSelection().getActionCommand());
                swingConfig.setShowPlainBytes(showBytes);
                swingConfig.setGroupReferenceIndicator(groupReference);
                replaceForm.setCharacterset(defaultCharacterSet);
                replaceForm.setGroupReference(groupReference);
                replaceForm.setDoBackup(doBackup);
                replaceForm.setBackupDirectory(backupDirectory);
                renameForm.setGroupReference(groupReference);
                if (infoSetHasChanged) tableModel.notifyUpdate();
                if (backupFlagEvent != null) {
                    backupFlagListener.itemStateChanged(backupFlagEvent);
                    backupFlagEvent = null;
                }
                logger.info("Settings saved to: group_seperator=" + groupReference + "; do_backup=" + doBackup + "; default_charset=" + defaultCharacterSet + "; added_charsets=" + addedCharsets.size());
                SettingsDialog.this.dispose();
            }
        }

        private boolean canSave() {
            return defaultCharacterSet != null;
        }
    }
}
