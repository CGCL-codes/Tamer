        @Override
        public void run() {
            List<AccountEntry> entries = (List<AccountEntry>) tv.getInput();
            NavigationView nav = (NavigationView) Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(NavigationView.ID);
            IStructuredSelection sel = (IStructuredSelection) nav.getTreeViewer().getSelection();
            TreeObject node = (TreeObject) sel.getFirstElement();
            if (node == null || node instanceof TreeParent) {
                return;
            }
            AccountBill bill = node.getAccountBill();
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(bill.getBillName());
            int rownum = 0;
            HSSFRow row = sheet.createRow(rownum++);
            row.createCell((short) 0).setCellValue(new HSSFRichTextString("���"));
            row.createCell((short) 1).setCellValue(new HSSFRichTextString("���"));
            row.createCell((short) 2).setCellValue(new HSSFRichTextString("����"));
            row.createCell((short) 3).setCellValue(new HSSFRichTextString("�����"));
            row.createCell((short) 4).setCellValue(new HSSFRichTextString("��������"));
            row.createCell((short) 5).setCellValue(new HSSFRichTextString("��¼����"));
            row.createCell((short) 6).setCellValue(new HSSFRichTextString("��ע"));
            for (AccountEntry entry : entries) {
                row = sheet.createRow(rownum++);
                if (entry instanceof IncomeAccountEntry) {
                    row.createCell((short) 0).setCellValue(new HSSFRichTextString("����"));
                } else {
                    row.createCell((short) 0).setCellValue(new HSSFRichTextString("֧��"));
                }
                row.createCell((short) 1).setCellValue(entry.getEntryID());
                row.createCell((short) 2).setCellValue(new HSSFRichTextString(entry.getMoney().getType().toString()));
                row.createCell((short) 3).setCellValue(entry.getMoney().getValue());
                HSSFCell cell = row.createCell((short) 4);
                cell.setCellValue(entry.getDate());
                HSSFCellStyle style = wb.createCellStyle();
                style.setDataFormat(wb.createDataFormat().getFormat("yyyy-m-d"));
                cell.setCellStyle(style);
                cell = row.createCell((short) 5);
                cell.setCellValue(entry.getCreateDate());
                cell.setCellStyle(style);
                row.createCell((short) 6).setCellValue(new HSSFRichTextString(entry.getDescription()));
            }
            FileOutputStream fileOut;
            final Shell shell = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
            try {
                FileDialog dialog = new FileDialog(shell, SWT.SAVE);
                dialog.setFileName("iwallet.xls");
                dialog.setFilterNames(new String[] { "Microsoft Excel (*.xls)" });
                dialog.setFilterExtensions(new String[] { "*.xls" });
                String fileName = dialog.open();
                if (fileName == null) {
                    return;
                }
                if (new File(fileName).exists()) {
                    if (!MessageDialog.openQuestion(shell, "iWallet - Question", "This file already exists. Do you want to overwrite?")) {
                        return;
                    }
                }
                fileOut = new FileOutputStream(fileName);
                wb.write(fileOut);
                fileOut.close();
                MessageDialog.openInformation(shell, "iWallet - Information", "Exportation finished successfully. :)");
            } catch (FileNotFoundException e) {
                MessageDialog.openInformation(shell, "Oops...", "File not found! :(");
            } catch (IOException e) {
                MessageDialog.openInformation(shell, "Oops...", "An I/O exception has been caught. :(");
            }
            super.run();
        }
