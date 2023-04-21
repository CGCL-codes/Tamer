    private void cargar(Connection conn) throws SQLException, ClassNotFoundException, Exception {
        PreparedStatement ms = null;
        if (!validado) {
            validado = validar();
        }
        if (!validado) {
            throw new Exception("No s'ha realitzat la validació de les dades del registre ");
        }
        registroGrabado = false;
        int fzaanoe;
        String campo;
        fechaTest = dateF.parse(dataentrada);
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaTest);
        DateFormat date1 = new SimpleDateFormat("yyyyMMdd");
        fzaanoe = cal.get(Calendar.YEAR);
        anoEntrada = String.valueOf(fzaanoe);
        int fzafent = Integer.parseInt(date1.format(fechaTest));
        int fzanume = ToolsBD.RecogerNumeroEntrada(conn, fzaanoe, oficina, errores);
        setNumeroEntrada(Integer.toString(fzanume));
        int fzacagc = Integer.parseInt(oficina);
        int off_codi = Integer.parseInt(oficinafisica);
        fechaTest = dateF.parse(data);
        cal.setTime(fechaTest);
        int fzafdoc = Integer.parseInt(date1.format(fechaTest));
        String fzacone, fzacone2;
        if (idioex.equals("1")) {
            fzacone = comentario;
            fzacone2 = "";
        } else {
            fzacone = "";
            fzacone2 = comentario;
        }
        String fzaproce;
        int fzactagg, fzacagge;
        if (fora.equals("")) {
            fzactagg = 90;
            fzacagge = Integer.parseInt(balears);
            fzaproce = "";
        } else {
            fzaproce = fora;
            fzactagg = 0;
            fzacagge = 0;
        }
        int ceros = 0;
        int fzacorg = Integer.parseInt(destinatari);
        int fzanent;
        String fzacent;
        if (altres.equals("")) {
            altres = "";
            fzanent = Integer.parseInt(entidad2);
            fzacent = entidadCastellano;
        } else {
            fzanent = 0;
            fzacent = "";
        }
        int fzacidi = Integer.parseInt(idioex);
        horaTest = horaF.parse(hora);
        cal.setTime(horaTest);
        DateFormat hhmm = new SimpleDateFormat("HHmm");
        int fzahora = Integer.parseInt(hhmm.format(horaTest));
        if (salida1.equals("")) {
            salida1 = "0";
        }
        if (salida2.equals("")) {
            salida2 = "0";
        }
        int fzanloc = Integer.parseInt(salida1);
        int fzaaloc = Integer.parseInt(salida2);
        if (disquet.equals("")) {
            disquet = "0";
        }
        int fzandis = Integer.parseInt(disquet);
        if (fzandis > 0) {
            ToolsBD.actualizaDisqueteEntrada(conn, fzandis, oficina, anoEntrada, errores);
        }
        Date fechaSystem = new Date();
        DateFormat aaaammdd = new SimpleDateFormat("yyyyMMdd");
        int fzafsis = Integer.parseInt(aaaammdd.format(fechaSystem));
        DateFormat hhmmss = new SimpleDateFormat("HHmmss");
        DateFormat sss = new SimpleDateFormat("S");
        String ss = sss.format(fechaSystem);
        if (ss.length() > 2) {
            ss = ss.substring(0, 2);
        }
        int fzahsis = Integer.parseInt(hhmmss.format(fechaSystem) + ss);
        if (correo != null && !correo.equals("")) {
            String insertBZNCORR = "INSERT INTO BZNCORR (FZPCENSA, FZPCAGCO, FZPANOEN, FZPNUMEN, FZPNCORR)" + "VALUES (?,?,?,?,?)";
            ms = conn.prepareStatement(insertBZNCORR);
            ms.setString(1, "E");
            ms.setInt(2, fzacagc);
            ms.setInt(3, fzaanoe);
            ms.setInt(4, fzanume);
            ms.setString(5, correo);
            ms.execute();
            ms.close();
        }
        String insertOfifis = "INSERT INTO BZENTOFF (FOEANOEN, FOENUMEN, FOECAGCO, OFE_CODI)" + "VALUES (?,?,?,?)";
        ms = conn.prepareStatement(insertOfifis);
        ms.setInt(1, fzaanoe);
        ms.setInt(2, fzanume);
        ms.setInt(3, fzacagc);
        ms.setInt(4, off_codi);
        ms.execute();
        ms.close();
        ms = conn.prepareStatement(SENTENCIA);
        ms.setInt(1, fzaanoe);
        ms.setInt(2, fzanume);
        ms.setInt(3, fzacagc);
        ms.setInt(4, fzafdoc);
        ms.setString(5, (altres.length() > 30) ? altres.substring(0, 30) : altres);
        ms.setString(6, (fzacone.length() > 160) ? fzacone.substring(0, 160) : fzacone);
        ms.setString(7, (tipo.length() > 2) ? tipo.substring(0, 2) : tipo);
        ms.setString(8, "N");
        ms.setString(9, "");
        ms.setString(10, (fzaproce.length() > 25) ? fzaproce.substring(0, 25) : fzaproce);
        ms.setInt(11, fzafent);
        ms.setInt(12, fzactagg);
        ms.setInt(13, fzacagge);
        ms.setInt(14, fzacorg);
        ms.setInt(15, ceros);
        ms.setString(16, (fzacent.length() > 7) ? fzacent.substring(0, 7) : fzacent);
        ms.setInt(17, fzanent);
        ms.setInt(18, fzahora);
        ms.setInt(19, fzacidi);
        ms.setString(20, (fzacone2.length() > 160) ? fzacone2.substring(0, 160) : fzacone2);
        ms.setInt(21, fzanloc);
        ms.setInt(22, fzaaloc);
        ms.setInt(23, fzandis);
        ms.setInt(24, fzafsis);
        ms.setInt(25, fzahsis);
        ms.setString(26, (usuario.toUpperCase().length() > 10) ? usuario.toUpperCase().substring(0, 10) : usuario.toUpperCase());
        ms.setString(27, idioma);
        registroGrabado = ms.execute();
        registroGrabado = true;
        if (!municipi060.equals("")) cargarMunicipio060(conn, fzaanoe, fzanume, fzacagc, municipi060, numeroRegistros060);
        String remitente = "";
        if (!altres.trim().equals("")) {
            remitente = altres;
        } else {
            javax.naming.InitialContext contexto = new javax.naming.InitialContext();
            Object ref = contexto.lookup("es.caib.regweb.ValoresHome");
            ValoresHome home = (ValoresHome) javax.rmi.PortableRemoteObject.narrow(ref, ValoresHome.class);
            Valores valor = home.create();
            remitente = valor.recuperaRemitenteCastellano(fzacent, fzanent + "");
            valor.remove();
        }
        try {
            Class t = Class.forName("es.caib.regweb.module.PluginHook");
            Class[] partypes = { String.class, Integer.class, Integer.class, Integer.class, Integer.class, String.class, String.class, String.class, Integer.class, Integer.class, String.class, Integer.class, String.class, String.class, Integer.class, Integer.class, Integer.class, String.class, String.class, String.class };
            Object[] params = { "A", new Integer(fzaanoe), new Integer(fzanume), new Integer(fzacagc), new Integer(fzafdoc), remitente, comentario, tipo, new Integer(fzafent), new Integer(fzacagge), fzaproce, new Integer(fzacorg), idioma, null, null, null, null, null, null, null };
            java.lang.reflect.Method metodo = t.getMethod("entrada", partypes);
            metodo.invoke(null, params);
        } catch (IllegalAccessException iae) {
        } catch (IllegalArgumentException iae) {
        } catch (InvocationTargetException ite) {
        } catch (NullPointerException npe) {
        } catch (ExceptionInInitializerError eiie) {
        } catch (NoSuchMethodException nsme) {
        } catch (SecurityException se) {
        } catch (LinkageError le) {
        } catch (ClassNotFoundException le) {
        }
        String Stringsss = sss.format(fechaSystem);
        switch(Stringsss.length()) {
            case (1):
                Stringsss = "00" + Stringsss;
                break;
            case (2):
                Stringsss = "0" + Stringsss;
                break;
        }
        int horamili = Integer.parseInt(hhmmss.format(fechaSystem) + Stringsss);
        logLopdBZENTRA("INSERT", (usuario.length() > 10) ? usuario.substring(0, 10) : usuario, fzafsis, horamili, fzanume, fzaanoe, fzacagc);
        ms.close();
    }
