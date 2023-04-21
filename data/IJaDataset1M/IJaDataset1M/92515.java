package com.ec.service;

public final class DateTimeArrayHelper {

    public static void write(IceInternal.BasicStream __os, DateTime[] __v) {
        if (__v == null) {
            __os.writeSize(0);
        } else {
            __os.writeSize(__v.length);
            for (int __i0 = 0; __i0 < __v.length; __i0++) {
                __v[__i0].__write(__os);
            }
        }
    }

    public static DateTime[] read(IceInternal.BasicStream __is) {
        DateTime[] __v;
        final int __len0 = __is.readSize();
        __is.checkFixedSeq(__len0, 12);
        __v = new DateTime[__len0];
        for (int __i0 = 0; __i0 < __len0; __i0++) {
            __v[__i0] = new DateTime();
            __v[__i0].__read(__is);
        }
        return __v;
    }
}
