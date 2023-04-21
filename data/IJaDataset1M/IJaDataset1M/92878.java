package org.koossery.adempiere.svco.impl.Requisition_to_invoice.RfQ_Response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.FactoryConfigurationError;
import org.dbunit.dataset.DataSetException;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.Requisition_to_invoice.RfQ_Response.C_RfQResponseLineQtyCriteria;
import org.koossery.adempiere.core.contract.dto.Requisition_to_invoice.RfQ_Response.C_RfQResponseLineQtyDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.Requisition_to_invoice.RfQ_Response.IC_RfQResponseLineQtySVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class C_RfQResponseLineQtySVCOImplTest extends KTADempiereBaseTest {

    private static IC_RfQResponseLineQtySVCO crfqresponselineqtySVCOImpl;

    private static C_RfQResponseLineQtyDTO newcRfQResponseLineQtyDTO = new C_RfQResponseLineQtyDTO();

    private static C_RfQResponseLineQtyDTO loadcRfQResponseLineQtyDTO = new C_RfQResponseLineQtyDTO();

    private static Date d = new Date();

    static {
        try {
            init();
            crfqresponselineqtySVCOImpl = (IC_RfQResponseLineQtySVCO) finder.get("SVCO/C_RfQResponseLineQty");
            dataSet = getDataSet("Requisition_to_invoice/RfQ_Response/C_RfQResponseLineQty.xml");
            table = dataSet.getTable("C_RfQResponseLineQty");
            connection = getConnection();
        } catch (BeansException e) {
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void cleanData() throws Exception {
        close();
    }

    @Before
    public void prepareTest() throws Exception {
    }

    @After
    public void cleanTest() throws Exception {
        DatabaseOperation.NONE.execute(connection, dataSet);
    }

    public static void buildDTO(int row) {
        try {
            newcRfQResponseLineQtyDTO.setC_RfQLineQty_ID(Integer.valueOf(table.getValue(row, "C_RFQLINEQTY_ID").toString()));
            newcRfQResponseLineQtyDTO.setC_RfQResponseLine_ID(Integer.valueOf(table.getValue(row, "C_RFQRESPONSELINE_ID").toString()));
            newcRfQResponseLineQtyDTO.setC_RfQResponseLineQty_ID(Integer.valueOf(table.getValue(row, "C_RFQRESPONSELINEQTY_ID").toString()));
            newcRfQResponseLineQtyDTO.setDiscount(BigDecimal.valueOf(Long.valueOf(table.getValue(row, "DISCOUNT").toString())));
            newcRfQResponseLineQtyDTO.setPrice(BigDecimal.valueOf(Long.valueOf(table.getValue(row, "PRICE").toString())));
            newcRfQResponseLineQtyDTO.setRanking(Integer.valueOf(table.getValue(row, "RANKING").toString()));
            newcRfQResponseLineQtyDTO.setIsActive(table.getValue(row, "ISACTIVE").toString());
        } catch (DataSetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreerC_RfQResponseLineQty() {
        try {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> TEST-CREER loading ...");
            buildDTO(0);
            int c_RfQResponseLineQty_ID = crfqresponselineqtySVCOImpl.createC_RfQResponseLineQty(ctx, newcRfQResponseLineQtyDTO, null);
            assertNotSame("C_RfQResponseLineQty creation failed ID=0", c_RfQResponseLineQty_ID, 0);
            newcRfQResponseLineQtyDTO.setC_RfQResponseLineQty_ID(c_RfQResponseLineQty_ID);
            System.out.println("TEST-CREER over____");
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChargerC_RfQResponseLineQty() {
        try {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> TEST-CHARGER loading ...");
            int key = newcRfQResponseLineQtyDTO.getC_RfQResponseLineQty_ID();
            loadcRfQResponseLineQtyDTO = crfqresponselineqtySVCOImpl.findOneC_RfQResponseLineQty(ctx, key);
            assertEquals(loadcRfQResponseLineQtyDTO.getC_RfQLineQty_ID(), newcRfQResponseLineQtyDTO.getC_RfQLineQty_ID());
            assertEquals(loadcRfQResponseLineQtyDTO.getC_RfQResponseLine_ID(), newcRfQResponseLineQtyDTO.getC_RfQResponseLine_ID());
            assertEquals(loadcRfQResponseLineQtyDTO.getC_RfQResponseLineQty_ID(), newcRfQResponseLineQtyDTO.getC_RfQResponseLineQty_ID());
            assertEquals(loadcRfQResponseLineQtyDTO.getDiscount(), newcRfQResponseLineQtyDTO.getDiscount());
            assertEquals(loadcRfQResponseLineQtyDTO.getPrice(), newcRfQResponseLineQtyDTO.getPrice());
            assertEquals(loadcRfQResponseLineQtyDTO.getRanking(), newcRfQResponseLineQtyDTO.getRanking());
            System.out.println("TEST-CHARGER over____");
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChercherC_RfQResponseLineQty() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> TEST-CHERCHER loading ...");
        ArrayList<C_RfQResponseLineQtyDTO> resultlist;
        try {
            C_RfQResponseLineQtyCriteria criteria = new C_RfQResponseLineQtyCriteria();
            resultlist = crfqresponselineqtySVCOImpl.findC_RfQResponseLineQty(ctx, criteria);
            assertNotNull(resultlist);
            for (C_RfQResponseLineQtyDTO dto : resultlist) {
                System.out.println("ID=" + dto.getC_RfQResponseLineQty_ID() + " : Name=" + dto.getC_RfQResponseLine_ID());
            }
            System.out.println("TEST-CHERCHER over____");
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModifierC_RfQResponseLineQty() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> TEST-MODIFIER loading ...");
        try {
            newcRfQResponseLineQtyDTO.setIsActive("Y");
            crfqresponselineqtySVCOImpl.updateC_RfQResponseLineQty(ctx, newcRfQResponseLineQtyDTO);
            int key = newcRfQResponseLineQtyDTO.getC_RfQResponseLineQty_ID();
            loadcRfQResponseLineQtyDTO = crfqresponselineqtySVCOImpl.findOneC_RfQResponseLineQty(ctx, key);
            assertEquals(loadcRfQResponseLineQtyDTO.getC_RfQResponseLineQty_ID(), newcRfQResponseLineQtyDTO.getC_RfQResponseLineQty_ID());
            System.out.println("TEST-MODIFIER over____");
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSupprimerC_RfQResponseLineQty() {
        try {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> TEST-SUPPRIMER loading ...");
            C_RfQResponseLineQtyCriteria criteria = new C_RfQResponseLineQtyCriteria();
            criteria.setC_RfQResponseLineQty_ID(newcRfQResponseLineQtyDTO.getC_RfQResponseLineQty_ID());
            boolean deleted = crfqresponselineqtySVCOImpl.deleteC_RfQResponseLineQty(ctx, criteria);
            assertEquals(true, deleted);
            System.out.println("TEST-SUPPRIMER over____");
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }
}
