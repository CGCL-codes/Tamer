package org.osmorc.manifest.lang.headerparser;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.osmorc.manifest.lang.psi.ManifestHeaderValue;

/**
 * @author Robert F. Beeger (robert@beeger.net)
 */
public interface HeaderParser {

    PsiReference[] getReferences(@NotNull ManifestHeaderValue headerValue);

    /**
   * The value of the given header value. Complex headers will probably have several header values. This method is
   * used to convert the raw data into some domain specific value as for example a Version or VersionRange.
   *
   * @param headerValue The raw header value to be converted
   * @return The converted value.
   */
    Object getValue(@NotNull ManifestHeaderValue headerValue);

    /**
   * Simple headers don't have clauses, attributes and directives. Semicolons and commas don't have any special meaning
   * @return true, if the header parsed by this parser is a simple header, in which commas and semicolons don't
   * have any special meaning
   */
    boolean isSimpleHeader();

    /**
   * Annotate the header value with error or any other useful information.
   * @param manifestHeaderValue the header value to be annotated
   * @param holder The annotation holder into which to put the annotations.
   */
    void annotate(ManifestHeaderValue manifestHeaderValue, AnnotationHolder holder);
}
