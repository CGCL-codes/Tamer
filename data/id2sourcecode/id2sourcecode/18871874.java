    private void saveToPod() {
        try {
            MessageBox msgBox = new MessageBox(sShell, SWT.OK);
            msgBox.setMessage("The file already exists! Do you want to overwrite it?");
            if (!ListPodDir.isDirectory()) {
                msgBox.setMessage("There is a problem with the player device! Please check the player device and restart the programm!");
                msgBox.open();
                return;
            }
            File program = new File(ListPodDir.getAbsolutePath() + ((ListPodDir.getAbsolutePath().charAt(ListPodDir.getAbsolutePath().length() - 1)) == '/' ? "" : "/") + combo.getText());
            if ((tableDay01.getItemCount() == 0 && tableDay02.getItemCount() == 0 && tableDay03.getItemCount() == 0 && tableDay04.getItemCount() == 0 && tableDay05.getItemCount() == 0 && tableDay06.getItemCount() == 0 && tableDay07.getItemCount() == 0 && tableDay08.getItemCount() == 0 && tableDay09.getItemCount() == 0 && tableDay10.getItemCount() == 0 && tableDay11.getItemCount() == 0 && tableDay12.getItemCount() == 0 && tableDay13.getItemCount() == 0 && tableDay14.getItemCount() == 0 && tableDay15.getItemCount() == 0 && !program.isDirectory())) {
                MessageBox msgBox3 = new MessageBox(sShell, SWT.OK);
                msgBox3.setMessage("All days are empty!");
                msgBox3.open();
                return;
            }
            if (program.isDirectory()) {
                MessageBox msgBox2 = new MessageBox(sShell, SWT.OK | SWT.CANCEL);
                if ((tableDay01.getItemCount() == 0 && tableDay02.getItemCount() == 0 && tableDay03.getItemCount() == 0 && tableDay04.getItemCount() == 0 && tableDay05.getItemCount() == 0 && tableDay06.getItemCount() == 0 && tableDay07.getItemCount() == 0 && tableDay08.getItemCount() == 0 && tableDay09.getItemCount() == 0 && tableDay10.getItemCount() == 0 && tableDay11.getItemCount() == 0 && tableDay12.getItemCount() == 0 && tableDay13.getItemCount() == 0 && tableDay14.getItemCount() == 0 && tableDay15.getItemCount() == 0)) {
                    msgBox2.setMessage("The program on the player already exists! Delete?");
                    if (msgBox2.open() != SWT.OK) return;
                    nukeSubFolder(program);
                    return;
                } else {
                    msgBox2.setMessage("The program on the player already exists! Overwrite?");
                    if (msgBox2.open() != SWT.OK) return;
                }
                nukeSubFolder(program);
            }
            program.mkdirs();
            makePodList(Constants.DAY + Constants.PalylistDay01, program, tableDay01);
            makePodList(Constants.DAY + Constants.PalylistDay02, program, tableDay02);
            makePodList(Constants.DAY + Constants.PalylistDay03, program, tableDay03);
            makePodList(Constants.DAY + Constants.PalylistDay04, program, tableDay04);
            makePodList(Constants.DAY + Constants.PalylistDay05, program, tableDay05);
            makePodList(Constants.DAY + Constants.PalylistDay06, program, tableDay06);
            makePodList(Constants.DAY + Constants.PalylistDay07, program, tableDay07);
            makePodList(Constants.DAY + Constants.PalylistDay08, program, tableDay08);
            makePodList(Constants.DAY + Constants.PalylistDay09, program, tableDay09);
            makePodList(Constants.DAY + Constants.PalylistDay10, program, tableDay10);
            makePodList(Constants.DAY + Constants.PalylistDay11, program, tableDay11);
            makePodList(Constants.DAY + Constants.PalylistDay12, program, tableDay12);
            makePodList(Constants.DAY + Constants.PalylistDay13, program, tableDay13);
            makePodList(Constants.DAY + Constants.PalylistDay14, program, tableDay14);
            makePodList(Constants.DAY + Constants.PalylistDay15, program, tableDay15);
            msgBox.setMessage("Export to player complete!");
            msgBox.open();
        } catch (Exception e) {
            MessageBox msgBox = new MessageBox(sShell, SWT.OK);
            msgBox.setMessage("Error on export!");
            msgBox.open();
        }
    }
