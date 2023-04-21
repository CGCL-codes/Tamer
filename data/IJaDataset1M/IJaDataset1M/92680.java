package com.centropresse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import com.centropresse.dto.Categoria;
import com.centropresse.dto.Corsa;
import com.centropresse.dto.Costruttore;
import com.centropresse.dto.Luce;
import com.centropresse.dto.Macchina;
import com.centropresse.dto.Modello;
import com.centropresse.dto.Spinta;
import com.centropresse.dto.Stato;
import com.centropresse.dto.Tipo;
import com.centropresse.dto.Ubicazione;
import com.centropresse.util.Utility;

public class MacchinaDAO {

    private ResultSet rs;

    private PreparedStatement ps;

    private Connection conn;

    private String className;

    public MacchinaDAO(Connection conn) {
        this.conn = conn;
        this.className = "MacchinaDAO";
    }

    public Macchina[] select(Macchina machine, String country, String orderBy, String orderType) throws Throwable {
        String methodName = "select";
        Macchina[] macchine = null;
        Macchina macchina = null;
        Tipo tipo = null;
        Categoria categoria = null;
        Modello modello = null;
        Costruttore costruttore = null;
        Ubicazione ubicazione = null;
        Stato statoCondizioni = null;
        Stato statoImpiantoElettrico = null;
        ArrayList array = new ArrayList();
        System.out.println(Utility.intestazioneLog(className, methodName) + " BEGIN.");
        try {
            if (country == null || country.trim().equals("")) {
                country = ConstantsDAO.COUNTRY_IT;
            }
            String select = "" + "SELECT M.ID_MACCHINA,M.ID_TIPO,T.DESC_TIPO_" + country + ",M.ID_CATEGORIA,C.DESC_CATEGORIA_" + country + "," + "M.ID_MODELLO,MD.DESC_MODELLO_IT,M.DESC_MACCHINA_" + country + "," + "M.ID_COSTRUTTORE,CS.DESC_COSTRUTTORE," + "M.ID_ANNO,M.CODICE_INTERNO,M.INGOMBRO,M.PESO,M.FRONTE,M.NUM_MOTORI,M.POTENZA,M.ALTEZZA," + "M.PROFONDITA,M.ID_STATO_CONDIZIONI,SC.DESC_STATO_" + country + "," + "M.ID_STATO_IMPIANTOELETTRICO,SIE.DESC_STATO_" + country + ",M.PREZZO_PUBBLICO,M.NOTE,M.VISIBILE," + "M.PREZZO_OPERATORI,M.ID_UBICAZIONE,U.DESC_UBICAZIONE_" + country + ",M.FOTO,M.VIDEO, " + "MC1.VALORE AS SPINTA,MC2.VALORE AS LUCE,MC3.VALORE AS CORSA " + "FROM MACCHINA M " + "LEFT OUTER JOIN TIPO T                         ON M.ID_TIPO=T.ID_TIPO " + "LEFT OUTER JOIN CATEGORIA C                    ON M.ID_CATEGORIA=C.ID_CATEGORIA " + "LEFT OUTER JOIN MODELLO MD                     ON M.ID_MODELLO=MD.ID_MODELLO " + "LEFT OUTER JOIN COSTRUTTORE CS                 ON M.ID_COSTRUTTORE=CS.ID_COSTRUTTORE " + "LEFT OUTER JOIN STATO_CONDIZIONI SC            ON M.ID_STATO_CONDIZIONI=SC.ID_STATO " + "LEFT OUTER JOIN STATO_IMPIANTOELETTRICO SIE    ON M.ID_STATO_IMPIANTOELETTRICO=SIE.ID_STATO " + "LEFT OUTER JOIN UBICAZIONE U                   ON M.ID_UBICAZIONE=U.ID_UBICAZIONE " + "LEFT OUTER JOIN T_MACCHINA_CARATTERISTICHE MC1 ON M.ID_MACCHINA=MC1.ID_MACCHINA AND MC1.ID_CARATTERISTICA=50 " + "LEFT OUTER JOIN T_MACCHINA_CARATTERISTICHE MC2 ON M.ID_MACCHINA=MC2.ID_MACCHINA AND MC2.ID_CARATTERISTICA=51 " + "LEFT OUTER JOIN T_MACCHINA_CARATTERISTICHE MC3 ON M.ID_MACCHINA=MC3.ID_MACCHINA AND MC3.ID_CARATTERISTICA=52 " + "WHERE 1=1 ";
            if (machine != null && machine.getId_macchina() != null && !machine.getId_macchina().equals("")) {
                select += " AND M.ID_MACCHINA='" + machine.getId_macchina() + "' ";
            }
            if (machine != null && machine.getTipo() != null) {
                if (machine.getTipo().getId_tipo() != null && !machine.getTipo().getId_tipo().equals("")) {
                    select += " AND M.ID_TIPO='" + machine.getTipo().getId_tipo() + "' ";
                }
                if (machine.getTipo().getId_categoria() != null && !machine.getTipo().getId_categoria().equals("")) {
                    select += " AND M.ID_CATEGORIA='" + machine.getTipo().getId_categoria() + "' ";
                }
            }
            if (machine != null && machine.getCategoria() != null && machine.getCategoria().getId_categoria() != null && !machine.getCategoria().getId_categoria().equals("")) {
                select += " AND M.ID_CATEGORIA='" + machine.getCategoria().getId_categoria() + "' ";
            }
            if (machine != null && machine.getModello() != null && machine.getModello().getId_modello() != null && !machine.getModello().getId_modello().equals("")) {
                select += " AND M.ID_MODELLO='" + machine.getModello().getId_modello() + "' ";
            }
            if (machine != null && machine.getDesc_macchina() != null && !machine.getDesc_macchina().equals("")) {
                select += " AND M.DESC_MACCHINA_" + country + " LIKE '%" + machine.getDesc_macchina() + "%' ";
            }
            if (machine != null && machine.getCostruttore() != null && machine.getCostruttore().getId_costruttore() != null && !machine.getCostruttore().getId_costruttore().equals("")) {
                select += " AND M.ID_COSTRUTTORE='" + machine.getCostruttore().getId_costruttore() + "' ";
            }
            if (machine != null && machine.getUbicazione() != null && machine.getUbicazione().getId_ubicazione() != null && !machine.getUbicazione().getId_ubicazione().equals("")) {
                select += " AND M.ID_UBICAZIONE='" + machine.getUbicazione().getId_ubicazione() + "' ";
            }
            if (machine != null && machine.getId_anno() != null && !machine.getId_anno().equals("")) {
                select += " AND M.ID_ANNO='" + machine.getId_anno() + "' ";
            }
            if (machine != null && machine.getCodiceInterno() != null && !machine.getCodiceInterno().equals("")) {
                select += " AND M.CODICE_ANNO='" + machine.getCodiceInterno() + "' ";
            }
            if (machine != null && machine.getStato_condizioni() != null && machine.getStato_condizioni().getId_stato() != null && !machine.getStato_condizioni().getId_stato().equals("")) {
                select += " AND M.ID_STATO_CONDIZIONI='" + machine.getStato_condizioni().getId_stato() + "' ";
            }
            if (machine != null && machine.getStato_impianto_elettrico() != null && machine.getStato_impianto_elettrico().getId_stato() != null && !machine.getStato_impianto_elettrico().getId_stato().equals("")) {
                select += " AND M.ID_STATO_IMPIANTOELETTRICO='" + machine.getStato_impianto_elettrico().getId_stato() + "' ";
            }
            if (machine != null && machine.getVisibile() != null && !machine.getVisibile().equals("")) {
                select += " AND M.VISIBILE='" + machine.getVisibile() + "' ";
            }
            if (machine != null && machine.getPrezzoOperatori_da() != null && !machine.getPrezzoOperatori_da().equals("")) {
                select += " AND M.PREZZO_OPERATORI >=" + machine.getPrezzoOperatori_da() + " ";
            }
            if (machine != null && machine.getPrezzoOperatori_a() != null && !machine.getPrezzoOperatori_a().equals("")) {
                select += " AND M.PREZZO_OPERATORI <=" + machine.getPrezzoOperatori_a() + " ";
            }
            if (machine != null && machine.getPrezzoPubblico_da() != null && !machine.getPrezzoPubblico_da().equals("")) {
                select += " AND M.PREZZO_PUBBLICO >=" + machine.getPrezzoPubblico_da() + " ";
            }
            if (machine != null && machine.getPrezzoPubblico_a() != null && !machine.getPrezzoPubblico_a().equals("")) {
                select += " AND M.PREZZO_PUBBLICO <=" + machine.getPrezzoPubblico_a() + " ";
            }
            if (machine != null && machine.getNote() != null && !machine.getNote().equals("")) {
                select += " AND M.NOTE LIKE '%" + machine.getNote() + "%' ";
            }
            if (machine != null && machine.getElencoCaratteristiche() != null && machine.getElencoCaratteristiche().size() > 0) {
                for (int i = 0; i < machine.getElencoCaratteristiche().size(); i++) {
                    if (machine.getElencoCaratteristiche().get(i) instanceof Spinta) {
                        Spinta spintaVO = (Spinta) machine.getElencoCaratteristiche().get(i);
                        select += " AND MC1.VALORE BETWEEN " + spintaVO.getMin() + " AND " + spintaVO.getMax() + " ";
                    } else if (machine.getElencoCaratteristiche().get(i) instanceof Luce) {
                        Luce luceVO = (Luce) machine.getElencoCaratteristiche().get(i);
                        select += " AND MC2.VALORE BETWEEN " + luceVO.getMin() + " AND " + luceVO.getMax() + " ";
                    } else if (machine.getElencoCaratteristiche().get(i) instanceof Corsa) {
                        Corsa corsaVO = (Corsa) machine.getElencoCaratteristiche().get(i);
                        select += " AND MC3.VALORE BETWEEN " + corsaVO.getMin() + " AND " + corsaVO.getMax() + " ";
                    }
                }
            }
            if (orderBy != null && !orderBy.trim().equals("")) {
                select += " ORDER BY " + orderBy;
            } else {
                select += " ORDER BY CS.DESC_COSTRUTTORE,T.DESC_TIPO_" + country + ",M.ID_ANNO,SC.DESC_STATO_" + country + ",U.DESC_UBICAZIONE_" + country + " ";
            }
            if (orderType != null && !orderType.trim().equals("")) {
                select += " " + orderType;
            } else {
                select += " " + ConstantsDAO.ORDER_TYPE_ASC;
            }
            System.out.println(Utility.intestazioneLog(className, methodName) + " select:" + select);
            ps = conn.prepareStatement(select);
            rs = ps.executeQuery();
            while (rs.next()) {
                String id_macchina = rs.getString(1);
                String id_tipo = rs.getString(2);
                String desc_tipo = rs.getString(3);
                String id_categoria = rs.getString(4);
                String desc_categoria = rs.getString(5);
                String id_modello = rs.getString(6);
                String desc_modello = rs.getString(7);
                String desc_macchina = rs.getString(8);
                String id_costruttore = rs.getString(9);
                String desc_costruttore = rs.getString(10);
                String id_anno = rs.getString(11);
                String codiceInterno = rs.getString(12);
                String ingombro = rs.getString(13);
                String peso = rs.getString(14);
                String fronte = rs.getString(15);
                String num_motori = rs.getString(16);
                String potenza = rs.getString(17);
                String altezza = rs.getString(18);
                String profondita = rs.getString(19);
                String id_stato_condizioni = rs.getString(20);
                String desc_stato_condizioni = rs.getString(21);
                String id_stato_impianto = rs.getString(22);
                String desc_stato_impianto = rs.getString(23);
                String prezzoPubblico = rs.getString(24);
                String note = rs.getString(25);
                String visibile = rs.getString(26);
                String prezzoOperatori = rs.getString(27);
                String id_ubicazione = rs.getString(28);
                String desc_ubicazione = rs.getString(29);
                String foto = rs.getString(30);
                String video = rs.getString(31);
                String spinta = rs.getString(32);
                String luce = rs.getString(33);
                String corsa = rs.getString(34);
                macchina = new Macchina(id_macchina, desc_macchina);
                tipo = new Tipo(id_tipo, id_categoria, desc_tipo);
                categoria = new Categoria(id_categoria, desc_categoria);
                modello = new Modello(id_modello, desc_modello);
                costruttore = new Costruttore(id_costruttore, desc_costruttore);
                statoCondizioni = new Stato(id_stato_condizioni, desc_stato_condizioni);
                statoImpiantoElettrico = new Stato(id_stato_impianto, desc_stato_impianto);
                ubicazione = new Ubicazione(id_ubicazione, desc_ubicazione);
                macchina.setTipo(tipo);
                macchina.setCategoria(categoria);
                macchina.setModello(modello);
                macchina.setDesc_macchina(desc_macchina);
                macchina.setCostruttore(costruttore);
                macchina.setId_anno(id_anno);
                macchina.setCodiceInterno(codiceInterno);
                macchina.setIngombro(ingombro);
                macchina.setPeso(peso);
                macchina.setFronte(fronte);
                macchina.setNum_motori(num_motori);
                macchina.setPotenza(potenza);
                macchina.setAltezza(altezza);
                macchina.setProfondita(profondita);
                macchina.setStato_condizioni(statoCondizioni);
                macchina.setStato_impianto_elettrico(statoImpiantoElettrico);
                macchina.setPrezzoPubblico(prezzoPubblico);
                macchina.setNote(note);
                macchina.setVisibile(visibile);
                macchina.setPrezzoOperatori(prezzoOperatori);
                macchina.setUbicazione(ubicazione);
                macchina.setVideo(video);
                macchina.setFoto(foto);
                array.add(macchina);
            }
            macchine = (Macchina[]) array.toArray(new Macchina[array.size()]);
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
        System.out.println(Utility.intestazioneLog(className, methodName) + " END.");
        return macchine;
    }

    /**
     * 
     * @param id_accessorio
     * @throws Throwable
     */
    public void delete(int id_macchina) throws Throwable {
        String methodName = "delete";
        try {
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    /**
     * 
     * @param accessorio
     * @throws Throwable
     */
    public void insert(Macchina macchina) throws Throwable {
        String methodName = "insert";
        try {
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    /**
  * 
  * @param accessorio
  * @throws Throwable
  */
    public void update(Macchina macchina) throws Throwable {
        String methodName = "update";
        try {
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    private void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
