package org.peaseplate.typeconversion.conversion;

import static org.peaseplate.typeconversion.TypeConversionSingleton.*;
import java.util.Arrays;
import org.peaseplate.typeconversion.ConvertibleType;
import org.peaseplate.utils.ReflectionUtils;
import org.testng.annotations.Test;

public abstract class AbstractConversionTest {

    @Test(dataProvider = "test")
    public void test(final Object source, final Class<?> targetType, final boolean convertNull, final Object expectedResult) throws Throwable {
        final StringBuilder log = new StringBuilder();
        log.append("TESTING: test(");
        log.append(ReflectionUtils.toString(source)).append(", ");
        log.append(ReflectionUtils.toString(targetType)).append(", ");
        log.append(ReflectionUtils.toString(convertNull)).append(", ");
        log.append(ReflectionUtils.toString(expectedResult)).append(")... ");
        try {
            assert (TYPE_CONVERSION_SERVICE.isConvertible((source != null) ? source.getClass() : null, targetType, convertNull) != ConvertibleType.UNAVAILABLE) : "Not convertible as expected";
            final Object result = TYPE_CONVERSION_SERVICE.convert(source, targetType, convertNull);
            if (expectedResult == null) {
                assert (result == null) : "Expected result to be null, but got: " + ReflectionUtils.toString(result);
            } else {
                assert (result != null) : "Expected result to be not null, but result was null";
                assert (result.getClass() == expectedResult.getClass()) : "Expected result to be of type " + ReflectionUtils.getSimpleName(expectedResult.getClass()) + ", but result was of type: " + ReflectionUtils.getSimpleName(result.getClass());
                if (expectedResult.getClass() == boolean[].class) {
                    assert (Arrays.equals((boolean[]) result, (boolean[]) expectedResult)) : "Expected result to be " + ReflectionUtils.toString(expectedResult) + ", but was: " + ReflectionUtils.toString(result);
                } else if (expectedResult.getClass() == byte[].class) {
                    assert (Arrays.equals((byte[]) result, (byte[]) expectedResult)) : "Expected result to be " + ReflectionUtils.toString(expectedResult) + ", but was: " + ReflectionUtils.toString(result);
                } else if (expectedResult.getClass() == short[].class) {
                    assert (Arrays.equals((short[]) result, (short[]) expectedResult)) : "Expected result to be " + ReflectionUtils.toString(expectedResult) + ", but was: " + ReflectionUtils.toString(result);
                } else if (expectedResult.getClass() == int[].class) {
                    assert (Arrays.equals((int[]) result, (int[]) expectedResult)) : "Expected result to be " + ReflectionUtils.toString(expectedResult) + ", but was: " + ReflectionUtils.toString(result);
                } else if (expectedResult.getClass() == long[].class) {
                    assert (Arrays.equals((long[]) result, (long[]) expectedResult)) : "Expected result to be " + ReflectionUtils.toString(expectedResult) + ", but was: " + ReflectionUtils.toString(result);
                } else if (expectedResult.getClass() == float[].class) {
                    assert (Arrays.equals((float[]) result, (float[]) expectedResult)) : "Expected result to be " + ReflectionUtils.toString(expectedResult) + ", but was: " + ReflectionUtils.toString(result);
                } else if (expectedResult.getClass() == double[].class) {
                    assert (Arrays.equals((double[]) result, (double[]) expectedResult)) : "Expected result to be " + ReflectionUtils.toString(expectedResult) + ", but was: " + ReflectionUtils.toString(result);
                } else if (expectedResult.getClass() == char[].class) {
                    assert (Arrays.equals((char[]) result, (char[]) expectedResult)) : "Expected result to be " + ReflectionUtils.toString(expectedResult) + ", but was: " + ReflectionUtils.toString(result);
                } else if (expectedResult.getClass().isArray()) {
                    assert (Arrays.equals((Object[]) result, (Object[]) expectedResult)) : "Expected result to be " + ReflectionUtils.toString(expectedResult) + ", but was: " + ReflectionUtils.toString(result);
                } else {
                    assert (result.equals(expectedResult)) : "Expected result to be " + ReflectionUtils.toString(expectedResult) + ", but was: " + ReflectionUtils.toString(result);
                }
            }
            log.append("PASSED");
        } catch (final Throwable e) {
            log.append("\n  FAILED: ").append(e);
            throw e;
        } finally {
            System.out.println(log);
        }
    }
}
