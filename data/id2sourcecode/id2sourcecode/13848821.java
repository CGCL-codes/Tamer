    private static void iDateiKopieren(File von, File nach) throws F_Sys_InOut {
        byte[] buffer = new byte[8192];
        int read = 0;
        RandomAccessFile raf_von = null;
        RandomAccessFile raf_nach = null;
        try {
            raf_von = new RandomAccessFile(von, "r");
            raf_nach = new RandomAccessFile(nach, "rw");
            raf_nach.setLength(raf_von.length());
            while ((read = raf_von.read(buffer)) > 0) raf_nach.write(buffer, 0, read);
        } catch (IOException e) {
            throw Fehler.weitergeben(e, "Datei kann nicht verarbeitet werden!");
        } finally {
            try {
                raf_von.close();
            } catch (IOException e) {
                Ausgabe.fehler(e, "Datei kann nicht geschlossen werden!");
            } finally {
                try {
                    raf_nach.close();
                } catch (IOException e) {
                    Ausgabe.fehler(e, "Datei kann nicht geschlossen werden!");
                }
            }
        }
    }
