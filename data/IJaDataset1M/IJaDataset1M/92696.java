package org.lindenb.tinytools;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import org.lindenb.io.IOUtils;
import org.lindenb.sw.PrefixMapping;
import org.lindenb.sw.io.RDFHandler;
import org.lindenb.util.C;
import org.lindenb.util.Compilation;

public class RDFToDot {

    private int ID_GENERATOR = 0;

    private Map<URI, Integer> rsrc2id = new HashMap<URI, Integer>();

    private PrintStream out = System.out;

    private PrefixMapping prefixMapping = new PrefixMapping();

    private RDFToDot() {
    }

    private void start() {
        out.println("digraph G {\nfontsize=10;");
    }

    private void end() {
        out.println("}");
        out.flush();
    }

    private Integer _id(URI uri) {
        Integer x = this.rsrc2id.get(uri);
        if (x == null) {
            x = (++ID_GENERATOR);
            this.rsrc2id.put(uri, x);
            out.println("_" + x + "[color=blue,shape=box,label=\"" + prefixMapping.shortForm(uri.toString()) + "\"];");
        }
        return x;
    }

    void parse(InputStream in) throws XMLStreamException {
        RDFHandler handler = new RDFHandler() {

            @Override
            public void found(URI subject, URI predicate, Object value, URI dataType, String lang, int index) throws IOException {
                Integer isub = _id(subject);
                String edge = "labelfontsize=7,label=\"" + prefixMapping.shortForm(predicate.toString()) + "\"";
                if (value instanceof URI) {
                    Integer iobj = _id(URI.class.cast(value));
                    out.println("_" + isub + " -> _" + iobj + "[" + edge + "];");
                } else {
                    out.println("_" + (++ID_GENERATOR) + "[color=gray,label=\"\\\"" + C.escape(value.toString()) + "\\\"\",shape=\"ellipse\"];");
                    out.println("_" + isub + " -> _" + ID_GENERATOR + "[" + edge + "];");
                }
            }
        };
        handler.parse(in);
    }

    public static void main(String[] args) {
        try {
            RDFToDot app = new RDFToDot();
            int optind = 0;
            while (optind < args.length) {
                if (args[optind].equals("-h") || args[optind].equals("-help") || args[optind].equals("--help")) {
                    System.err.println(Compilation.getLabel());
                    System.err.println("Options:");
                    System.err.println(" -h help; This screen.");
                    System.err.println(" -p <prefix> <uri> add this prefix mapping");
                    return;
                } else if (args[optind].startsWith("-p")) {
                    app.prefixMapping.setNsPrefix(args[++optind], args[++optind]);
                } else if (args[optind].equals("--")) {
                    optind++;
                    break;
                } else if (args[optind].startsWith("-")) {
                    System.err.println("Unknown option " + args[optind]);
                    return;
                } else {
                    break;
                }
                ++optind;
            }
            app.start();
            if (optind == args.length) {
                app.parse(System.in);
            } else {
                while (optind < args.length) {
                    java.io.InputStream r = IOUtils.openInputStream(args[optind++]);
                    app.parse(r);
                    r.close();
                }
            }
            app.end();
        } catch (Throwable err) {
            err.printStackTrace();
        }
    }
}
