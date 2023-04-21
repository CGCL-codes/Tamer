    public void widgetSelected(SelectionEvent event) {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        FileDialog saveDialog = new FileDialog(shell, SWT.SAVE);
        saveDialog.setFilterExtensions(new String[] { "*.txt", "*.*" });
        saveDialog.setFilterNames(new String[] { "Text File (*.txt)", "All Files" });
        String path = saveDialog.open();
        File file = new File(path);
        if (file.exists()) {
            MessageBox box = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
            box.setText("Overwrite " + file.getName() + "?");
            box.setMessage("File: " + path + " already exists.\nAre you sure that you want to overwrite it?");
            if (box.open() != SWT.YES) return;
        }
        Cursor waitCursor = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
        shell.setCursor(waitCursor);
        try {
            file.createNewFile();
            PrintWriter out = new PrintWriter(file);
            String time = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z").format(new Date());
            out.print("Created: ");
            out.println(time);
            out.print("Created By: ");
            out.println(System.getProperty("user.name"));
            for (Entry<String, Topic> entry : view.getResults().entrySet()) out.println(entry.getValue().getContent());
            if (out.checkError()) {
                MessageBox box = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
                box.setText("Error writing file " + file.getName());
                box.setMessage("An error occured while writing out the results.");
                box.open();
            }
            out.close();
        } catch (FileNotFoundException e1) {
            VJP.logError(e1.getMessage(), e1);
        } catch (IOException e1) {
            MessageBox box = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
            box.setText("Could not create " + file.getName());
            box.setMessage("File: " + path + " could not be created.\nPlease make sure that this a valid path and that you have write permissions in this directory.");
            box.open();
        } finally {
            shell.setCursor(null);
            waitCursor.dispose();
        }
    }
