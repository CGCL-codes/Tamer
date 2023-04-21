package org.jcvi.common.core.seq.fastx.fastq;

import org.jcvi.common.core.io.TextFileVisitor;
import org.jcvi.common.core.seq.fastx.FastXFileVisitor;
import org.jcvi.common.core.symbol.residue.nt.NucleotideSequence;

/**
 * {@code FastQFileVisitor} is a {@link TextFileVisitor}
 * implementation for FASTQ files.
 * @author dkatzel
 *
 *
 */
public interface FastqFileVisitor extends FastXFileVisitor {

    /**
     * Visit the defline of a given fastq record.
     * <strong>Note: </strong>if the Fastq records were created using 
     * Casava 1.8+, then the id will contain a whitespace
     * followed by the mate information and no comment.
     * This is different than most other fastq parsers which separate
     * on whitespace and therefore will create duplicate ids for each
     * mate in the template (but with different values for the "comments").
     * Duplicate ids will break any applications that combine all the reads
     * from multiple fastq files so it was decided that {@link FastqRecord} id
     * contain both the template and mate information to guarantee uniqueness.
     * <p/>
     * {@inheritDoc}
     */
    DeflineReturnCode visitDefline(String id, String optionalComment);

    void visitNucleotides(NucleotideSequence nucleotides);

    void visitEncodedQualities(String encodedQualities);
}
