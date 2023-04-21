    @Override
    protected void doWork(Node[] activatedNodes) {
        try {
            DialogDescriptor descriptor;
            SelectKeyPanel keyPan;
            try {
                keyPan = BouncyCastleHelper.buildSelectKeyPanel(SelectKeyPanel.ENCRYPTING_KEY_TYPE);
                if (keyPan == null) return;
                descriptor = new DialogDescriptor(keyPan, NbBundle.getMessage(getClass(), "SelectEncryptKeyDialogTitle"));
            } catch (OperationCancelledException cex) {
                return;
            } catch (Exception ex) {
                String msg = "Keystore error: " + ex.getLocalizedMessage();
                NotifyDescriptor nd = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(nd);
                return;
            }
            Dialog dlg = null;
            try {
                dlg = DialogDisplayer.getDefault().createDialog(descriptor);
                dlg.setResizable(true);
                dlg.pack();
                dlg.setVisible(true);
            } finally {
                if (dlg != null) dlg.dispose();
            }
            if (descriptor.getValue() == DialogDescriptor.CANCEL_OPTION) return;
            Document w3cDoc = getW3cDocument();
            NodeList nlist = w3cDoc.getElementsByTagName("X3D");
            Element w3cElem = (org.w3c.dom.Element) nlist.item(0);
            Document newdoc;
            Entry ent = keyPan.getSelectedEntry();
            if (ent instanceof KeyStore.SecretKeyEntry) {
                KeyStore.SecretKeyEntry secKeyEnt = (KeyStore.SecretKeyEntry) ent;
                org.apache.xml.security.Init.init();
                XMLCipher cipher = XMLCipher.getProviderInstance(XMLCipher.TRIPLEDES, "BC");
                cipher.init(XMLCipher.ENCRYPT_MODE, secKeyEnt.getSecretKey());
                newdoc = cipher.doFinal(w3cDoc, w3cElem);
            } else if (ent instanceof KeyStore.PrivateKeyEntry) {
                KeyStore.PrivateKeyEntry prKeyEnt = (KeyStore.PrivateKeyEntry) ent;
                org.apache.xml.security.Init.init();
                XMLCipher cipher = XMLCipher.getProviderInstance(XMLCipher.TRIPLEDES, "BC");
                cipher.init(XMLCipher.ENCRYPT_MODE, prKeyEnt.getCertificate().getPublicKey());
                newdoc = cipher.doFinal(w3cDoc, w3cElem);
            } else {
                throw new Exception(NbBundle.getMessage(getClass(), "MSG_SecretToEncrypt"));
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XMLUtils.outputDOMc14nWithComments(newdoc, baos);
            String xmlString = baos.toString("UTF-8");
            if (saveChooser == null) {
                saveChooser = new JFileChooser();
                saveChooser.setDialogTitle(NbBundle.getMessage(getClass(), "MSG_SaveEncryptedFileTitle"));
                saveChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                openInEditorCB = new JCheckBox(NbBundle.getMessage(getClass(), "MSG_OpenInEditor"));
                openInEditorCB.setSelected(true);
                saveChooser.setAccessory(openInEditorCB);
            }
            FileObject thisFo = x3dEditorSupport.getDataObject().getPrimaryFile();
            FileObject directory = thisFo.getParent();
            saveChooser.setCurrentDirectory(FileUtil.toFile(directory));
            String outputType = "xml";
            String outFileNm = FileUtil.findFreeFileName(directory, thisFo.getName(), outputType);
            saveChooser.setSelectedFile(new File(outFileNm + "." + outputType));
            if (saveChooser.showSaveDialog(null) == JFileChooser.CANCEL_OPTION) return;
            File outFile = saveChooser.getSelectedFile();
            FileWriter outFw = new FileWriter(outFile);
            outFw.write(xmlString);
            outFw.close();
            if (openInEditorCB.isSelected()) {
                ConversionsHelper.openInEditor(outFile);
            }
            InputOutput io = IOProvider.getDefault().getIO("Output", false);
            io.select();
            io.getOut().println(NbBundle.getMessage(getClass(), "MSG_EncryptOpComplete"));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
