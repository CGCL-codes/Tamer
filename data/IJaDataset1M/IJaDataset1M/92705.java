package edu.ucdavis.cs.movieminer.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.Lists;

/**
 * 
 * @author pfishero
 * @version $Id$
 */
public class CsvInputSourceItemProvider implements InputSourceItemProvider<Object> {

    public static final Logger logger = Logger.getLogger(CsvInputSourceItemProvider.class);

    private InputStream source;

    private CSVReader reader;

    public void close() throws Exception {
        if (null != reader) reader.close();
    }

    public void open() throws Exception {
        reader = new CSVReader(new InputStreamReader(source));
    }

    public void setInputSource(Resource source) {
        try {
            if (source.getFilename().endsWith("gz")) {
                this.source = new GZIPInputStream(source.getInputStream());
            } else if (source.getFilename().endsWith("csv") || source.getFilename().endsWith("txt")) {
                this.source = source.getInputStream();
            } else {
                String msg = "unknown file extension on input source: " + source;
                throw new IllegalArgumentException(msg);
            }
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    public Iterator<Object> iterator() {
        return new InternalIterator<Object>();
    }

    private final class InternalIterator<Object> implements Iterator<Object> {

        private String[] nextLine;

        public InternalIterator() {
            if (null != reader) {
                try {
                    nextLine = reader.readNext();
                } catch (IOException e) {
                    logger.error("IO problem - " + e);
                }
            }
        }

        public boolean hasNext() {
            boolean hasNext = false;
            try {
                hasNext = (nextLine != null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return hasNext;
        }

        public Object next() {
            String[] currentLine = nextLine;
            try {
                nextLine = reader.readNext();
            } catch (IOException e) {
                logger.error("error while reading input file " + nextLine);
            }
            return (Object) currentLine;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
