    private void showDsdf(URL url) {
        InputStream in = null;
        try {
            in = url.openStream();
            StringBuilder sb = new StringBuilder();
            int by = in.read();
            while (by != -1) {
                sb.append((char) by);
                by = in.read();
            }
            in.close();
            String s = sb.toString();
            int contentLength = Integer.parseInt(s.substring(4, 10));
            String sxml = s.substring(10, 10 + contentLength);
            Reader xin = new BufferedReader(new StringReader(sxml));
            DocumentBuilder builder;
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource source = new InputSource(xin);
            Document document = builder.parse(source);
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = (XPath) factory.newXPath();
            NodeList o = (NodeList) xpath.evaluate("/stream/properties/@*", document, XPathConstants.NODESET);
            String result = "";
            for (int ii = 0; ii < o.getLength(); ii++) {
                result += "\n" + o.item(ii).getNodeName() + "  =  " + o.item(ii).getNodeValue();
            }
            in.close();
            final String fresult = result;
            Runnable run = new Runnable() {

                public void run() {
                    JTextArea area = new JTextArea();
                    area.setText(fresult);
                    area.setEditable(false);
                    final JPopupMenu copyMenu = new JPopupMenu();
                    copyMenu.add(new DefaultEditorKit.CopyAction()).setText("Copy");
                    area.addMouseListener(new MouseAdapter() {

                        @Override
                        public void mousePressed(MouseEvent e) {
                            if (e.isPopupTrigger()) copyMenu.show(e.getComponent(), e.getX(), e.getY());
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                            if (e.isPopupTrigger()) copyMenu.show(e.getComponent(), e.getX(), e.getY());
                        }
                    });
                    JScrollPane sp = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    sp.setPreferredSize(new java.awt.Dimension(480, 480));
                    JOptionPane.showMessageDialog(Das2ServerDataSourceEditorPanel.this, sp);
                }
            };
            SwingUtilities.invokeLater(run);
        } catch (XPathExpressionException ex) {
            JOptionPane.showMessageDialog(examplesComboBox, "Unable to parse dsdf: " + ex.getMessage());
            Logger.getLogger(Das2ServerDataSourceEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            JOptionPane.showMessageDialog(examplesComboBox, "Unable to parse dsdf: " + ex.getMessage());
            Logger.getLogger(Das2ServerDataSourceEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            JOptionPane.showMessageDialog(examplesComboBox, "Unable to parse dsdf: " + ex.getMessage());
            Logger.getLogger(Das2ServerDataSourceEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(examplesComboBox, "Unable to parse dsdf: " + ex.getMessage());
            Logger.getLogger(Das2ServerDataSourceEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(Das2ServerDataSourceEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
