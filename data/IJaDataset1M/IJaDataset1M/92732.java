package cunei.cli;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import cunei.config.EnumParameter;
import cunei.config.Parameter;
import cunei.config.ValueOfParameter;
import cunei.corpus.Language;
import cunei.corpus.Languages;
import cunei.document.DocumentReader;
import cunei.document.DocumentReaders;
import cunei.document.Phrase;
import cunei.document.Sentence;
import cunei.type.SequenceType;
import cunei.type.Type;
import cunei.type.TypesOfTypes;

public class Frequencies extends CommandLineInterface {

    private EnumParameter<DocumentReaders> readerArgument;

    private ValueOfParameter<Language> langArgument;

    private ValueOfParameter<SequenceType> typeArgument;

    public static void main(String[] arguments) {
        new Frequencies(arguments).run();
    }

    public Frequencies(String[] arguments) {
        super(arguments, true);
    }

    protected void initialize() {
        readerArgument = EnumParameter.get(commandArguments, "reader", DocumentReaders.class, DocumentReaders.SIMPLE);
        langArgument = ValueOfParameter.get(commandArguments, "lang", Languages.class, null);
        typeArgument = ValueOfParameter.get(commandArguments, "type", TypesOfTypes.class, SequenceType.LEXICAL);
    }

    protected Parameter[] getRequired() {
        return new Parameter[] { langArgument };
    }

    ;

    protected void run(BufferedReader input, PrintStream output) {
        final DocumentReader<Phrase> reader = readerArgument.getValue().newInstance(input, langArgument.getValue());
        Map<Type, Integer> result = new HashMap<Type, Integer>();
        Sentence<Phrase> sentence;
        while ((sentence = reader.next()) != null) {
            final Phrase phrase = sentence.get();
            if (phrase == null) continue;
            for (int i = 0; i < phrase.getLength(); i++) {
                final Type type = phrase.get(typeArgument.getValue(), i);
                if (type == null) continue;
                Integer value = result.get(type);
                if (value == null) result.put(type, 1); else result.put(type, 1 + value);
            }
        }
        for (Map.Entry<Type, Integer> entry : result.entrySet()) output.println(entry.getKey().toString() + " " + entry.getValue());
    }
}
