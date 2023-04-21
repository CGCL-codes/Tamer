package com.etc.util;

import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author magicbank
 */
public class LotNumber {

    public static Logger log4j = Logger.getLogger(LotNumber.class);

    public static String getRandom() {
        return Digest.encode(Long.toString(new Date().getTime()), Misc.vocalString(16));
    }
}
