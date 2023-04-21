package cf.e_commerce.base.db.service;

import com.uplexis.idealize.base.utils.IdealizeDateTime;

/**
 * Retrieve different values of different data types.
 * 
 * @author Felipe Melo
 */
public final class DataValueService extends BaseService {

    /**
	 * Get a boolean value related to a given item(in generic terms)
	 * 
	 * @param id
	 *            Long with item id
	 * @return boolean value related to item
	 */
    public final boolean getBoolValue(final Long id) {
        return false;
    }

    /**
	 * Get an integer value related to a given item(in generic terms)
	 * 
	 * @param id
	 *            Long with item id
	 * @return int value related to item
	 */
    public final int getIntegerValue(final Long id) {
        return 0;
    }

    /**
	 * Get a double value related to a given item(in generic terms)
	 * 
	 * @param id
	 *            Long with item id
	 * @return double value related to item
	 */
    public final double getDoubleValue(final Long id) {
        return 0d;
    }

    /**
	 * Get a text value related to a given item(in generic terms)
	 * 
	 * @param id
	 *            Long with item id
	 * @return String with value related to item
	 */
    public final String getTextValue(final Long id) {
        return null;
    }

    /**
	 * Get a varchar value related to a given item(in generic terms)
	 * 
	 * @param id
	 *            Long with item id
	 * @return String with varchar value related to item
	 */
    public final String getVarcharValue(final Long id) {
        return null;
    }

    /**
	 * Get a date_time value related to a given item(in generic terms)
	 * 
	 * @param id
	 *            Long with item id
	 * @return IdealizeDateTime related to item
	 */
    public final IdealizeDateTime getDateTimeValue(final Long id) {
        return null;
    }
}
