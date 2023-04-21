    public void download(String nom_fichier_dl, BooleanUpdate applyUpdateOnReload) {
        new File(Constantes.DOS_UPDATES).mkdir();
        int diviseur = 1024;
        String nom_diviseur = "Ko";
        int pourcentage = 0;
        float taille_dl = 0;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        df.setDecimalSeparatorAlwaysShown(true);
        URL url;
        try {
            url = new URL(nom_fichier_dl);
            URLConnection uc = url.openConnection();
            float taille_total = uc.getContentLength();
            if (taille_total > 1000000) {
                diviseur = 1024 * 1024;
                nom_diviseur = "Mo";
            }
            InputStream in = uc.getInputStream();
            String FileName = url.getFile();
            FileName = FileName.substring(FileName.lastIndexOf('/') + 1);
            FileOutputStream WritenFile = new FileOutputStream(Constantes.DOS_UPDATES + FileName);
            byte[] buff = new byte[1024];
            int l = in.read(buff);
            taille_dl += l;
            pourcentage = Math.round(taille_dl / taille_total * 100);
            ihm.jpb_maj.setValue(pourcentage);
            ihm.jl_valeur.setText(pourcentage + "% (" + df.format((taille_dl / diviseur)) + " sur " + df.format((taille_total / diviseur)) + " " + nom_diviseur + ")");
            while (l > 0) {
                WritenFile.write(buff, 0, l);
                l = in.read(buff);
                taille_dl += l;
                pourcentage = Math.round(taille_dl / taille_total * 100);
                ihm.jpb_maj.setValue(pourcentage);
                ihm.jl_valeur.setText(pourcentage + "% (" + df.format((taille_dl / diviseur)) + " sur " + df.format((taille_total / diviseur)) + " " + nom_diviseur + ")");
            }
            WritenFile.flush();
            WritenFile.close();
            String new_name = "update_from_" + num_version + "_to_" + maj_file_version + ".jar";
            File f_old = new File(Constantes.DOS_UPDATES + FileName);
            File f_new = new File(Constantes.DOS_UPDATES + new_name);
            f_old.renameTo(f_new);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        end_dl(applyUpdateOnReload);
    }
