    public boolean validar() {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        validado = false;
        entidadCastellano = null;
        errores.clear();
        try {
            conn = ToolsBD.getConn();
            validarFecha(dataentrada);
            if (error) {
                errores.put("dataentrada", "Data d'entrada no es lògica");
            }
            Date fechaHoy = new Date();
            fechaTest = dateF.parse(dataentrada);
            if (fechaTest.after(fechaHoy)) {
                errores.put("dataentrada", "Data d'entrada posterior a la del dia");
            }
            if (hora == null) {
                errores.put("hora", "Hora d'entrada no es logica");
            } else {
                try {
                    horaF.setLenient(false);
                    horaTest = horaF.parse(hora);
                } catch (ParseException ex) {
                    errores.put("hora", "Hora d'entrada no es lògica");
                    errores.put("hora", hora);
                }
            }
            if (!oficina.equals("")) {
                try {
                    String sentenciaSql = "SELECT * FROM BZAUTOR WHERE FZHCUSU=? AND FZHCAUT=? AND FZHCAGCO IN " + "(SELECT FAACAGCO FROM BAGECOM WHERE FAAFBAJA=0 AND FAACAGCO=?)";
                    ps = conn.prepareStatement(sentenciaSql);
                    ps.setString(1, usuario.toUpperCase());
                    ps.setString(2, "AE");
                    ps.setInt(3, Integer.parseInt(oficina));
                    rs = ps.executeQuery();
                    if (rs.next()) {
                    } else {
                        errores.put("oficina", "Oficina: " + oficina + " no vàlida per a l'usuari: " + usuario);
                    }
                } catch (Exception e) {
                    System.out.println(usuario + ": Error en validar l'oficina " + e.getMessage());
                    e.printStackTrace();
                    errores.put("oficina", "Error en validar l'oficina: " + oficina + " de l'usuari: " + usuario + ": " + e.getClass() + "->" + e.getMessage());
                } finally {
                    ToolsBD.closeConn(null, ps, rs);
                }
            } else {
                errores.put("oficina", "Oficina: " + oficina + " no vàlida per a l'usuari: " + usuario);
            }
            if (data == null) {
                data = dataentrada;
            }
            validarFecha(data);
            if (error) {
                errores.put("data", "Data document, no es lògica");
            }
            try {
                String sentenciaSql = "SELECT * FROM BZTDOCU WHERE FZICTIPE=? AND FZIFBAJA=0";
                ps = conn.prepareStatement(sentenciaSql);
                ps.setString(1, tipo);
                rs = ps.executeQuery();
                if (rs.next()) {
                } else {
                    errores.put("tipo", "Tipus de document : " + tipo + " no vàlid");
                }
            } catch (Exception e) {
                System.out.println(usuario + ": Error en validar el tipus de document" + e.getMessage());
                e.printStackTrace();
                errores.put("tipo", "Error en validar el tipus de document : " + tipo + ": " + e.getClass() + "->" + e.getMessage());
            } finally {
                ToolsBD.closeConn(null, ps, rs);
            }
            try {
                String sentenciaSql = "SELECT * FROM BZIDIOM WHERE FZMCIDI=?";
                ps = conn.prepareStatement(sentenciaSql);
                ps.setString(1, idioma);
                rs = ps.executeQuery();
                if (rs.next()) {
                } else {
                    errores.put("idioma", "Idioma del document : " + idioma + " no vàlid");
                }
            } catch (Exception e) {
                System.out.println(usuario + ": Error en validar l'idioma del document" + e.getMessage());
                e.printStackTrace();
                errores.put("idioma", "Error en validar l'idioma del document: " + idioma + ": " + e.getClass() + "->" + e.getMessage());
            } finally {
                ToolsBD.closeConn(null, ps, rs);
            }
            if (entidad1.trim().equals("") && altres.trim().equals("")) {
                errores.put("entidad1", "És obligatori introduir el remitent");
            } else if (!entidad1.trim().equals("") && !altres.trim().equals("")) {
                errores.put("entidad1", "Heu d'introduir: Entitat o Altres");
            } else if (!entidad1.equals("")) {
                if (entidad2.equals("")) {
                    entidad2 = "0";
                }
                try {
                    String sentenciaSql = "SELECT * FROM BZENTID WHERE FZGCENT2=? AND FZGNENTI=? AND FZGFBAJA=0";
                    ps = conn.prepareStatement(sentenciaSql);
                    ps.setString(1, entidad1);
                    ps.setInt(2, Integer.parseInt(entidad2));
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        entidadCastellano = rs.getString("FZGCENTI");
                    } else {
                        errores.put("entidad1", "Entitat Remitent : " + entidad1 + "-" + entidad2 + " no vàlid");
                        System.out.println(usuario + ": ERROR: en validar l'entitat remitent : " + entidad1 + "-" + entidad2 + " no vàlid");
                    }
                } catch (Exception e) {
                    System.out.println(usuario + ": Error en validar l'entitat remitent " + e.getMessage());
                    e.printStackTrace();
                    errores.put("entidad1", "Error en validar l'entitat remitent: " + entidad1 + "-" + entidad2 + ": " + e.getClass() + "->" + e.getMessage());
                } finally {
                    ToolsBD.closeConn(null, ps, rs);
                }
            }
            if (!oficina.equals("32") && !correo.trim().equals("")) {
                errores.put("correo", "El valor nombre de correu només es pot introduir per l'Oficina 32 (BOIB)");
                System.out.println(usuario + ": ERROR: El valor nombre de correu només es pot introduir per l'Oficina 32 (BOIB)");
            }
            if ((balears.equals("") || balears == null) && (fora.equals("") || fora == null)) {
                errores.put("balears", "Obligatori introduir Procedència Geogràfica");
            } else if (!balears.equals("") && !fora.equals("")) {
                errores.put("balears", "Heu d'introduir Balears o Fora de Balears");
            } else if (!balears.equals("")) {
                try {
                    String sentenciaSql = "SELECT * FROM BAGRUGE WHERE FABCTAGG=90 AND FABCAGGE=? AND FABFBAJA=0";
                    ps = conn.prepareStatement(sentenciaSql);
                    ps.setInt(1, Integer.parseInt(balears));
                    rs = ps.executeQuery();
                    if (rs.next()) {
                    } else {
                        errores.put("balears", "Procedència geogràfica de Balears : " + balears + " no vàlid");
                        System.out.println(usuario + ": ERROR: Procedència geogràfica de Balears : " + balears + " no vàlid");
                    }
                } catch (Exception e) {
                    System.out.println(usuario + ": Error en validar la procedència geogràfica de Balears " + e.getMessage());
                    e.printStackTrace();
                    errores.put("balears", "Error en validar la procedència geogràfica de Balears : " + balears + ": " + e.getClass() + "->" + e.getMessage());
                } finally {
                    ToolsBD.closeConn(null, ps, rs);
                }
            }
            if (salida1.equals("") && salida2.equals("")) {
            } else {
                int chk1 = 0;
                int chk2 = 0;
                try {
                    chk1 = Integer.parseInt(salida1);
                    chk2 = Integer.parseInt(salida2);
                } catch (Exception e) {
                    errores.put("salida1", "Ambdós camps de numero de sortida han de ser numèrics");
                    System.out.println(usuario + ": ERROR: Ambdós camps de numero de sortida han de ser numèrics");
                }
                if (chk2 < 1990 || chk2 > 2050) {
                    errores.put("salida1", "Any de sortida, incorrecte");
                }
            }
            try {
                String sentenciaSql = "SELECT * FROM BZOFIOR WHERE FZFCAGCO=? AND FZFCORGA=?";
                ps = conn.prepareStatement(sentenciaSql);
                ps.setInt(1, Integer.parseInt(oficina));
                ps.setInt(2, Integer.parseInt(destinatari));
                rs = ps.executeQuery();
                if (rs.next()) {
                } else {
                    errores.put("destinatari", "Organisme destinatari : " + destinatari + " no vàlid");
                }
            } catch (NumberFormatException e1) {
                errores.put("destinatari", "Organisme destinatari : " + destinatari + " codi no numèric");
            } catch (Exception e) {
                System.out.println(usuario + ": Error en validar l'organisme destinatari " + e.getMessage());
                e.printStackTrace();
                errores.put("destinatari", "Error en validar l'organisme destinatari : " + destinatari + ": " + e.getClass() + "->" + e.getMessage());
            } finally {
                ToolsBD.closeConn(null, ps, rs);
            }
            if (!idioex.equals("1") && !idioex.equals("2")) {
                errores.put("idioex", "L'idioma ha de ser 1 o 2, idioma=" + idioex);
            }
            try {
                if (!disquet.equals("")) {
                    int chk1 = Integer.parseInt(disquet);
                }
            } catch (Exception e) {
                errores.put("disquet", "Numero de disquet no vàlid");
            }
            if (comentario.equals("")) {
                errores.put("comentario", "Heu d'introduir un extracte del document ");
            }
            if (actualizacion && !motivo.trim().equals("")) {
                if (comentarioNuevo.equals("")) {
                    errores.put("comentario", "Heu d'introduir un extracte del document ");
                }
                if (entidad1Nuevo.equals("") && altresNuevo.equals("")) {
                    errores.put("entidad1", "Obligatori introduir remitent");
                } else if (!entidad1Nuevo.equals("") && !altresNuevo.equals("")) {
                    errores.put("entidad1", "Heu d'introduir: Entitat o Altres");
                } else if (!entidad1Nuevo.equals("")) {
                    if (entidad2Nuevo.equals("")) {
                        entidad2Nuevo = "0";
                    }
                    try {
                        String sentenciaSql = "SELECT * FROM BZENTID WHERE FZGCENT2=? AND FZGNENTI=? AND FZGFBAJA=0";
                        ps = conn.prepareStatement(sentenciaSql);
                        ps.setString(1, entidad1Nuevo);
                        ps.setInt(2, Integer.parseInt(entidad2Nuevo));
                        rs = ps.executeQuery();
                        if (rs.next()) {
                        } else {
                            errores.put("entidad1", "Entitat Remitent : " + entidad1 + "-" + entidad2 + " no vàlid");
                            System.out.println("Error en validar l'entitat Remitent " + entidad1 + "-" + entidad2 + " no vàlid");
                        }
                    } catch (Exception e) {
                        System.out.println(usuario + ": Error en validar l'entitat Remitent " + e.getMessage());
                        e.printStackTrace();
                        errores.put("entidad1", "Error en validar l'entitat Remitent : " + entidad1 + "-" + entidad2 + ": " + e.getClass() + "->" + e.getMessage());
                    } finally {
                        ToolsBD.closeConn(null, ps, rs);
                    }
                }
            }
            if (!oficina.equals("") && !actualizacion) {
                try {
                    String sentenciaSql = "SELECT MAX(FZAFENTR) FROM BZENTRA WHERE FZAANOEN=? AND FZACAGCO=?";
                    fechaTest = dateF.parse(dataentrada);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaTest);
                    DateFormat date1 = new SimpleDateFormat("yyyyMMdd");
                    ps = conn.prepareStatement(sentenciaSql);
                    ps.setInt(1, cal.get(Calendar.YEAR));
                    ps.setInt(2, Integer.parseInt(oficina));
                    rs = ps.executeQuery();
                    int ultimaFecha = 0;
                    if (rs.next()) {
                        ultimaFecha = rs.getInt(1);
                        if (ultimaFecha > Integer.parseInt(date1.format(fechaTest))) {
                            errores.put("dataentrada", "Data inferior a la darrera entrada");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errores.put("dataentrada", "Error inesperat a data d'entrada");
                    System.out.println(usuario + ": Error inesperat a la data d'entrada" + ": " + e.getClass() + "->" + e.getMessage());
                } finally {
                    ToolsBD.closeConn(null, ps, rs);
                }
            }
        } catch (Exception e) {
            validado = false;
        } finally {
            try {
                ToolsBD.closeConn(conn, null, null);
            } catch (Exception e) {
                System.out.println("Excepció en tancar la connexió: " + e.getMessage());
                e.printStackTrace();
            }
        }
        if (errores.size() == 0) {
            validado = true;
        } else {
            validado = false;
        }
        return validado;
    }
