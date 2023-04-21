    public EditWindow(Frame owner, Connection connection, String tableName, Properties dbProperties) {
        super(owner, String.format("Editando %s.%S", dbProperties.getProperty("dbCatalogName"), tableName), true);
        this.connection = connection;
        this.tableName = tableName;
        this.dbProperties = dbProperties;
        useSQLite = dbProperties.getProperty("protocol").contains("sqlite");
        datum = new ArrayList<Object[]>();
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String q = String.format(dbProperties.getProperty("all"), tableName);
        Statement stm = null;
        ResultSet result = null;
        try {
            stm = connection.createStatement();
            result = stm.executeQuery(q);
            while (result.next()) {
                Object[] row = new Object[2];
                row[0] = f.parse(result.getString(1));
                row[1] = result.getDouble(2);
                datum.add(row);
            }
        } catch (ParseException e) {
            System.err.println(e);
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                result.close();
                stm.close();
            } catch (SQLException se) {
                System.err.println(se);
            }
        }
        datum.trimToSize();
        tableModel = new CustomTableModel();
        tableModel.addTableModelListener(this);
        table = new JTable(tableModel) {

            /**
       * Cria um table header que fornece textos de ajuda contextual
       * para os headers das colunas.
       *
       * @return Componente JTableHeader especializado.
      */
            @Override
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {

                    /**
           * Obtêm o texto de ajuda contextual a ser exibido quando o mouse
           * paira sobre um header da tabela.
           *
           * @param e Evento de mouse pairando sobre um header da tabela.
          */
                    @Override
                    public String getToolTipText(MouseEvent e) {
                        String[] headerToolTips = { "datas no formato dia/mês/ano", "taxas mensais de correção monetária" };
                        int ndx = columnModel.getColumnIndexAtX(e.getPoint().x);
                        ndx = columnModel.getColumn(ndx).getModelIndex();
                        return headerToolTips[ndx];
                    }

                    private static final long serialVersionUID = -6921792200568000158L;
                };
            }

            private static final long serialVersionUID = 5317136280874641962L;
        };
        table.getSelectionModel().addListSelectionListener(this);
        Font font = UIManager.getFont("FormattedTextField.font");
        FontMetrics metric = getFontMetrics(font);
        int w = 11 * metric.stringWidth("00/00/0000") / 10;
        int h = (int) (w * (1 + Math.sqrt(5d)) / 2);
        table.setPreferredScrollableViewportSize(new Dimension(2 * w, 2 * h));
        table.setRowHeight(metric.getMaxAscent() + metric.getMaxDescent() + metric.getAscent() / 4);
        if (!System.getProperty("java.version").startsWith("1.5")) {
            table.setFillsViewportHeight(true);
        }
        table.setDefaultRenderer(java.util.Date.class, new DateRenderer());
        @SuppressWarnings("deprecation") java.util.Date minDate = new java.util.Date(0, Calendar.JANUARY, 1);
        @SuppressWarnings("deprecation") java.util.Date maxDate = new java.util.Date(199, Calendar.DECEMBER, 31);
        table.setDefaultEditor(java.util.Date.class, new DateEditor(minDate, maxDate));
        table.setDefaultRenderer(Double.class, new DoubleRenderer());
        table.setDefaultEditor(Double.class, new DoubleEditor(-1d, 1d));
        newBtn = buildButton("images/add01.png", "images/add02.png", "<html><center>adiciona novo registro no final da tabela<br>ou após o último selecionado</center></html>", true);
        eraseBtn = buildButton("images/remove01.png", "images/remove02.png", "apaga o(s) registro(s) selecionado(s)", false);
        closeBtn = buildButton("images/exit01.png", "images/exit02.png", "encerra edição", true);
        JToolBar toolBar = new JToolBar();
        toolBar.setMargin(new Insets(0, 0, 0, 0));
        toolBar.setRollover(true);
        toolBar.add(newBtn);
        toolBar.add(eraseBtn);
        Component glue = Box.createHorizontalGlue();
        glue.setFocusable(false);
        toolBar.add(glue);
        toolBar.addSeparator();
        toolBar.add(closeBtn);
        font = font.deriveFont((5 * font.getSize2D()) / 6f);
        sizeLabel = new JLabel();
        sizeLabel.setFont(font);
        counterLabel = new JLabel();
        counterLabel.setFont(font.deriveFont(Font.ITALIC));
        JPanel statusPane = new JPanel();
        statusPane.setFont(font.deriveFont(11f));
        statusPane.setBackground(Color.WHITE);
        statusPane.setBorder(BorderFactory.createEmptyBorder(1, 2, 0, 2));
        statusPane.setLayout(new BoxLayout(statusPane, BoxLayout.X_AXIS));
        statusPane.add(sizeLabel);
        statusPane.add(Box.createGlue());
        statusPane.add(counterLabel);
        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout());
        pane.add(toolBar, BorderLayout.NORTH);
        pane.add(new JScrollPane(table), BorderLayout.CENTER);
        add(pane, BorderLayout.CENTER);
        add(statusPane, BorderLayout.SOUTH);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent we) {
                for (int j = datum.size(); --j >= 0; ) datum.remove(j);
                heap.clear();
            }
        });
        heap = new HashSet<Object[]>();
        updateStatus();
        scrollToTarget();
    }
