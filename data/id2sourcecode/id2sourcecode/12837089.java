    public void actionPerformed(ActionEvent event) {
        OxilFrame oxilFrame = OxilFrame.getInstance();
        JTabbedPane tabbedPane = oxilFrame.getTabbedPane();
        try {
            JFileChooser fc = new JFileChooser(ApplicationContext.basePath);
            if (fc.showOpenDialog(oxilFrame) == JFileChooser.APPROVE_OPTION) {
                tabbedPane.removeAll();
                ApplicationContext.arquivoOriginal = null;
                ApplicationContext.configuracaoROM = null;
                ApplicationContext.tabela = null;
                File file = fc.getSelectedFile();
                if (file.exists()) {
                    String baseFilePath = file.getAbsolutePath();
                    baseFilePath = baseFilePath.substring(0, baseFilePath.lastIndexOf("."));
                    ApplicationContext.basePath = file.getParentFile().getAbsolutePath();
                    File cfgFile = new File(baseFilePath + "." + SUFIXO_CFG_FILE);
                    if (!cfgFile.exists()) {
                        JFileChooser cfgFileChooser = new JFileChooser(file.getParentFile());
                        cfgFileChooser.setFileFilter(CFG_FILE_FILTER);
                        if (cfgFileChooser.showOpenDialog(oxilFrame) == JFileChooser.APPROVE_OPTION) {
                            cfgFile = cfgFileChooser.getSelectedFile();
                        }
                        if (!cfgFile.exists()) {
                            throw new ReadConfigurationException("N�o foi poss�vel carregar um arquivo de configura��es v�lido.");
                        }
                    }
                    File tblFile = new File(baseFilePath + "." + SUFIXO_TBL_FILE);
                    if (!tblFile.exists()) {
                        JFileChooser tblFileChooser = new JFileChooser(cfgFile.getParentFile());
                        tblFileChooser.setFileFilter(TBL_FILE_FILTER);
                        if (tblFileChooser.showOpenDialog(oxilFrame) == JFileChooser.APPROVE_OPTION) {
                            tblFile = tblFileChooser.getSelectedFile();
                        }
                        if (!tblFile.exists()) {
                            throw new ReadConfigurationException("N�o foi poss�vel carregar uma tabela v�lida.");
                        }
                    }
                    ConfiguracaoROM configuracaoROM = leArquivoConfiguracoes(cfgFile);
                    ApplicationContext.configuracaoROM = configuracaoROM;
                    Tabela tabela = leTabela(tblFile);
                    ApplicationContext.tabela = tabela;
                    ApplicationContext.arquivoOriginal = file;
                    oxilFrame.habilitarCampos();
                    List<InformacaoScript> informacoesScript = configuracaoROM.getInformacoesScript();
                    FileInputStream fis = new FileInputStream(file);
                    FileChannel fileChannel = fis.getChannel();
                    for (int i = 0; i < informacoesScript.size(); i++) {
                        InformacaoScript informacaoScript = informacoesScript.get(i);
                        int offsetInicioTexto = informacaoScript.getOffsetInicioTexto();
                        int offsetFimTexto = informacaoScript.getOffsetFimTexto();
                        int offsetInicioTextoExtra = informacaoScript.getOffsetInicioTextoExtra();
                        int offsetFimTextoExtra = informacaoScript.getOffsetFimTextoExtra();
                        int tamanhoTexto = offsetFimTexto - offsetInicioTexto;
                        int tamanhoTextoExtra = offsetFimTextoExtra - offsetInicioTextoExtra;
                        byte[] fileContent = new byte[tamanhoTexto + 1];
                        fileChannel.position(0);
                        fis.skip(offsetInicioTexto);
                        fis.read(fileContent);
                        StringBuffer tabContent = new StringBuffer((tamanhoTexto) + (tamanhoTextoExtra));
                        for (int j = 0; j < fileContent.length; j++) {
                            tabContent.append(HexaUtil.hexaValue(fileContent[j]));
                        }
                        if (offsetInicioTextoExtra != -1 && offsetFimTextoExtra != -1) {
                            fileContent = new byte[tamanhoTextoExtra + 1];
                            fileChannel.position(0);
                            fis.skip(offsetInicioTextoExtra);
                            fis.read(fileContent);
                            for (int j = 0; j < fileContent.length; j++) {
                                tabContent.append(HexaUtil.hexaValue(fileContent[j]));
                            }
                        }
                        final JTextArea jTextArea = criaTextArea();
                        jTextArea.setText(exibeTexto(tabContent.toString(), tabela, configuracaoROM.getValorHexaNuloString()));
                        jTextArea.requestFocusInWindow();
                        jTextArea.setFont(AlterarFonteAction.getFonteSelecionada());
                        jTextArea.setCaretPosition(0);
                        JScrollPane jScrollPane = new JScrollPane();
                        jScrollPane.setViewportView(jTextArea);
                        tabbedPane.addTab("Script " + (i + 1), jScrollPane);
                    }
                    fis.close();
                    if (tabbedPane != null) {
                        Component[] tabs = tabbedPane.getComponents();
                        if (tabs != null && tabs.length > 0) {
                            JScrollPane jScrollPane = (JScrollPane) tabs[0];
                            JTextArea textArea = (JTextArea) jScrollPane.getViewport().getView();
                            textArea.requestFocusInWindow();
                        }
                    }
                } else {
                    oxilFrame.desabilitarCampos();
                    JOptionPane.showMessageDialog(oxilFrame, "O arquivo selecionado � inv�lido.", "Ops.", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (ReadConfigurationException e) {
            e.printStackTrace();
            oxilFrame.desabilitarCampos();
            JOptionPane.showMessageDialog(oxilFrame, e.getMessage(), "Ops.", JOptionPane.ERROR_MESSAGE);
        } catch (Throwable t) {
            t.printStackTrace();
            oxilFrame.desabilitarCampos();
            JOptionPane.showMessageDialog(oxilFrame, "Ocorreu um erro ao processar a sua requisi��o.", "Ops.", JOptionPane.ERROR_MESSAGE);
        }
    }
