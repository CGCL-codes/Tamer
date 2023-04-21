                @Override
                protected Object doInBackground() {
                    st = System.currentTimeMillis();
                    pl.setText(LocaleI18n.getMessage("ExportCSVDialog.9"));
                    final ExportData qs = table.getCVSExportData();
                    final int count = qs.getCount();
                    pb.setMaximum(count);
                    final ArrayList<JTableExColumn> columns = new ArrayList<JTableExColumn>();
                    final ArrayList<String> columnLine = new ArrayList<String>();
                    table.doRowObjects(-1, new IRowObject() {

                        @Override
                        public void doRow(final JTableExColumn column, final Object o) {
                            columns.add(column);
                            columnLine.add(column.getColumnText());
                        }
                    });
                    try {
                        final CSVWriter writer = new CSVWriter((new FileWriter(IoUtils.createFile(new File(filename)))));
                        writer.writeNext(columnLine.toArray(new String[columnLine.size()]));
                        Object bean;
                        int line = 0;
                        while (qs.hasMoreElements()) {
                            bean = qs.nextElement();
                            if (abort) {
                                abort = false;
                                break;
                            }
                            final String[] nextLine = new String[columns.size()];
                            int i = 0;
                            for (final JTableExColumn column : columns) {
                                nextLine[i++] = table.convertToString(column, BeanUtils.getProperty(bean, column.getColumnName()));
                            }
                            writer.writeNext(nextLine);
                            publish(qs.getCount(), ++line);
                        }
                        writer.close();
                    } catch (final Exception e) {
                        SwingUtils.showError(ExportCSVDialog.this, e);
                    }
                    return count;
                }
