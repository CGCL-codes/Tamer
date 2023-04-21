package bioweka.classifiers.sequence.alignments.parsers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import bioweka.core.debuggers.Debugger;
import bioweka.evaluators.ScoreEvaluator;

/**
 * Alignment parser for PSI-BLAST output.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.6 $
 */
public class PsiBlastParser extends BlastParser {

    /**
     * The unique class identifier.
     */
    private static final long serialVersionUID = 3979274650792376371L;

    /**
     * The description of the PSI-BLAST parser component.
     */
    public static final String PSI_BLAST_PARSER_GLOBAL_INFO = "Parser for PSI-BLAST output.";

    /**
     * Initializes the PSI-BLAST parser.
     */
    public PsiBlastParser() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    protected void parse(ScoreEvaluator scoreEvaluator) throws Exception {
        String line = null;
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(getInput()));
        Debugger debugger = getDebugger();
        ByteArrayOutputStream bufferOutput = new ByteArrayOutputStream();
        PrintWriter bufferWriter = new PrintWriter(bufferOutput);
        try {
            while ((line = reader.readLine()) != null) {
                debugger.info("Line " + reader.getLineNumber() + ": " + line);
                Thread.sleep(1);
                bufferWriter.println(line);
                if (line.trim().startsWith("Results from round")) {
                    bufferOutput = new ByteArrayOutputStream();
                    bufferWriter = new PrintWriter(bufferOutput);
                }
            }
            bufferWriter.close();
            bufferOutput.close();
            LineNumberReader bufferReader = new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(bufferOutput.toByteArray())));
            parse(bufferReader, scoreEvaluator);
            bufferReader.close();
        } finally {
            reader.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public String globalInfo() {
        return PSI_BLAST_PARSER_GLOBAL_INFO;
    }
}
