package uk.ac.shef.wit.runes.rune.nlp;

import uk.ac.shef.wit.commons.UtilCollections;
import uk.ac.shef.wit.runes.Runes;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionCannotHandle;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchContent;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchStructure;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionRuneExecution;
import uk.ac.shef.wit.runes.rune.Rune;
import uk.ac.shef.wit.runes.runestone.Runestone;
import uk.ac.shef.wit.runes.runestone.Structure;
import uk.ac.shef.wit.runes.runestone.StructureAndContent;
import uk.ac.shef.wit.text.gazetteer.Gazetteer;
import uk.ac.shef.wit.text.util.SimpleTextNormaliser;
import java.text.MessageFormat;
import java.util.*;

/**
 * Identifies word tokens which can be found in gazetteers.
 *
 * @author <a href="mailto:z.zhang@dcs.shef.ac.uk">Ziqi Zhang</a>
 * @version $Id: RuneGazetteerWordWise.java $
 */
public class RuneGazetteerWordWise implements Rune {

    private static final double VERSION = 0.6;

    static {
        Runes.registerRune(RuneGazetteerWordWise.class, MessageFormat.format("[nlp] gazetteer word-wise {0}", VERSION));
    }

    /**
     * {@inheritDoc}
     * <br/><br/>
     * <strong>Required:</strong>
     * <pre>
     * "gazetteer",
     * "case_insensitive_gazetteer",
     * "$_string"
     * </pre>
     */
    public Set<String> analyseRequired(Runestone stone) throws RunesExceptionCannotHandle, RunesExceptionRuneExecution {
        return UtilCollections.add(new HashSet<String>(), "gazetteers", "$_string");
    }

    /**
     * {@inheritDoc}
     * <br/><br/>
     * <strong>Provided:</strong>
     * <pre>
     * "category",
     * "string_has_category",
     * "compare_lower_case"
     * </pre>
     */
    public Set<String> analyseProvided(Runestone stone) throws RunesExceptionCannotHandle, RunesExceptionRuneExecution {
        return UtilCollections.add(new HashSet<String>(), "gazetteer_category", "$_string_has_gazetteer_category|$_string|gazetteer_category", "normalisers");
    }

    public void carve(Runestone stone) throws RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent, RunesExceptionCannotHandle, RunesExceptionRuneExecution {
        final StructureAndContent<String> strings = stone.getStructureAndContent("$_string");
        final StructureAndContent<String> categories = stone.getStructureAndContent("gazetteer_category");
        final Structure stringsHasCategories = stone.getStructure("$_string_has_gazetteer_category");
        for (Gazetteer gaz : stone.<Gazetteer>getContent("gazetteers")) {
            int catId = categories.encode(gaz.getCategory());
            for (Map.Entry<int[], String> e : strings) {
                String s = gaz.isLowercase() ? e.getValue().toLowerCase() : e.getValue();
                if (s != null && gaz.contains(s)) stringsHasCategories.inscribe(e.getKey()[0], catId); else {
                    s = SimpleTextNormaliser.normaliseToAlphaNumeric(s);
                    if (s != null && gaz.contains(s)) stringsHasCategories.inscribe(e.getKey()[0], catId);
                }
            }
        }
    }

    public String toString() {
        return "gazetteer word based";
    }
}
