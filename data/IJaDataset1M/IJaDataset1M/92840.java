package fr.insee.rome.factory;

import java.net.Proxy;
import fr.insee.rome.io.genre.AtomicGenreGenerator;
import fr.insee.rome.io.genre.ControlGenreGenerator;
import fr.insee.rome.io.genre.GenreGenerator;
import fr.insee.rome.io.genre.SimplifiedGenreGenerator;
import fr.insee.rome.io.genre.TxtGenreGenerator;
import fr.insee.rome.io.mots.Mots;
import fr.insee.rome.io.mots.MapMots;
import fr.insee.rome.io.net.HttpRomeDownloader;
import fr.insee.rome.io.net.HttpRomeRequest;
import fr.insee.rome.io.net.RomeDownloader;
import fr.insee.rome.io.net.RomeRequest;
import fr.insee.rome.io.reader.DomainesReader;
import fr.insee.rome.io.reader.MetierReader;
import fr.insee.rome.io.reader.RegexMetierReader;
import fr.insee.rome.io.reader.RegexResultatsReader;
import fr.insee.rome.io.reader.ResultatsReader;
import fr.insee.rome.io.reader.TxtDomainesReader;
import fr.insee.rome.io.writer.CsvRomeWriter;
import fr.insee.rome.io.writer.RomeWriter;
import fr.insee.rome.string.comp.LevenshteinComparator;
import fr.insee.rome.string.comp.StringComparator;
import fr.insee.rome.string.norm.MapSynonymes;
import fr.insee.rome.string.norm.RomeNormalizationSteps;
import fr.insee.rome.string.norm.RomeNormalizer;
import fr.insee.rome.string.norm.SimpleNormalizationSteps;
import fr.insee.rome.string.norm.SimpleNormalizer;
import fr.insee.rome.string.norm.Synonymes;

public abstract class RomeFactory {

    public enum GeneratorType {

        SIMPLE, COMPACT, ATOMIC, CONTROL
    }

    private RomeFactory() {
    }

    public static RomeDownloader getDownloader() {
        return HttpRomeDownloader.getInstance();
    }

    public static RomeRequest getRequest(Proxy proxy) {
        return new HttpRomeRequest(proxy);
    }

    public static DomainesReader getDomainesReader() {
        return TxtDomainesReader.getInstance();
    }

    public static MetierReader getMetierReader() {
        return RegexMetierReader.getInstance();
    }

    public static ResultatsReader getResultatsReader() {
        return RegexResultatsReader.getInstance();
    }

    public static StringComparator getComparator() {
        return LevenshteinComparator.getInstance();
    }

    public static RomeWriter getWriter() {
        return CsvRomeWriter.getInstance();
    }

    public static RomeNormalizationSteps getNormalizationSteps() {
        return SimpleNormalizationSteps.getInstance();
    }

    public static RomeNormalizer getNormalizer() {
        return SimpleNormalizer.getInstance();
    }

    public static Synonymes getSynonymes() {
        return MapSynonymes.getInstance();
    }

    public static Mots getMots() {
        return MapMots.getInstance();
    }

    public static GenreGenerator getGenerator(GeneratorType type) {
        switch(type) {
            case SIMPLE:
                return TxtGenreGenerator.getInstance();
            case COMPACT:
                return SimplifiedGenreGenerator.getInstance();
            case ATOMIC:
                return AtomicGenreGenerator.getInstance();
            case CONTROL:
                return ControlGenreGenerator.getInstance();
        }
        return AtomicGenreGenerator.getInstance();
    }
}
