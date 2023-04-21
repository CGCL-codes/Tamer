    private boolean createNewZipFile(boolean ask, File f) {
        boolean status = ask;
        if (status) {
            Object[] options = { yesOption, noOption };
            int result = JOptionPane.showOptionDialog(this, createNewZip.toString(), fileDoesNotExist.toString(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
            status = (result == JOptionPane.YES_OPTION);
        }
        if (status) {
            try {
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f);
                ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));
                ZipEntry newZipEntry = new ZipEntry("Readme.txt");
                newZipEntry.setComment("Important information");
                zos.putNextEntry(newZipEntry);
                OutputStreamWriter osw = new OutputStreamWriter(zos);
                osw.write("This is a sample resource file. All data like ");
                osw.write("images for icons and buttons sould be contained here. ");
                osw.write("This file must have at least one entry (ZIP files cannot ");
                osw.write("be empty). \n\n ");
                osw.write("Commentsto files are very important, as they are ");
                osw.write("matched with the caption tags of translatable objects.");
                osw.close();
                zos.close();
                fos.close();
                return true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return false;
    }
