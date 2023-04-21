package codebarre.com.google.zxing.qrcode.detector;

import codebarre.com.google.zxing.ResultPoint;

/**
 * <p>Encapsulates an alignment pattern, which are the smaller square patterns found in
 * all but the simplest QR Codes.</p>
 *
 * @author Sean Owen
 */
public final class AlignmentPattern extends ResultPoint {

    private final float estimatedModuleSize;

    AlignmentPattern(float posX, float posY, float estimatedModuleSize) {
        super(posX, posY);
        this.estimatedModuleSize = estimatedModuleSize;
    }

    /**
   * <p>Determines if this alignment pattern "about equals" an alignment pattern at the stated
   * position and size -- meaning, it is at nearly the same center with nearly the same size.</p>
   */
    boolean aboutEquals(float moduleSize, float i, float j) {
        if (Math.abs(i - getY()) <= moduleSize && Math.abs(j - getX()) <= moduleSize) {
            float moduleSizeDiff = Math.abs(moduleSize - estimatedModuleSize);
            return moduleSizeDiff <= 1.0f || moduleSizeDiff / estimatedModuleSize <= 1.0f;
        }
        return false;
    }
}
