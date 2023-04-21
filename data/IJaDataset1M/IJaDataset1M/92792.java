package net.sf.imca.model;

import static org.junit.Assert.*;
import java.util.Locale;
import net.sf.imca.model.entities.FeeEntity;
import net.sf.imca.web.backingbeans.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.mysql.jdbc.Util;

public class TestFeeBO {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetConvertedToCurrency() throws Exception {
        FeeBO feeFrom = new FeeBO(new FeeEntity());
        feeFrom.getEntity().setCurrency("EUR");
        feeFrom.getEntity().setAmount(10000);
    }

    @Test
    public void testFormatFeeBOCurrency() {
        FeeBO feeFrom = new FeeBO(new FeeEntity());
        feeFrom.getEntity().setCurrency("EUR");
        feeFrom.getEntity().setAmount(1234.555);
        Utils.getWebAppLocale().setDefault(Locale.ENGLISH);
        assertEquals("EUR 1,234.56", feeFrom.toString());
        feeFrom.getEntity().setAmount(11000.5550);
        assertEquals("EUR 11,000.56", feeFrom.toString());
        feeFrom.getEntity().setCurrency("USD");
        feeFrom.getEntity().setAmount(1);
        assertEquals("USD 1.00", feeFrom.toString());
        feeFrom.getEntity().setCurrency("AUD");
        assertEquals("AUD 1.00", feeFrom.toString());
    }
}
