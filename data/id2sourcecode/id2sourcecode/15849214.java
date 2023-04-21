    private void appendFile(Zip64File zf, String sFileName, File fileOriginal, int iMethod) {
        byte[] buffer = new byte[iBUFFER_SIZE];
        try {
            Date dateModified = new Date(fileOriginal.lastModified());
            FileInputStream fis = new FileInputStream(fileOriginal);
            EntryOutputStream eos = zf.openEntryOutputStream(sFileName, iMethod, dateModified);
            for (int iRead = fis.read(buffer); iRead >= 0; iRead = fis.read(buffer)) eos.write(buffer, 0, iRead);
            fis.close();
            eos.close();
        } catch (ZipException ze) {
            System.out.println(ze.getClass().getName() + ": " + ze.getMessage());
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getClass().getName() + ": " + fnfe.getMessage());
        } catch (IOException ie) {
            System.out.println(ie.getClass().getName() + ": " + ie.getMessage());
        }
    }
