package swisseph;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * NOTE: only the main thread does add/remove to the cache.
 *
 * THIS IS INCOMPLETE (but it does seem to work):
 *      - do something with nReadAheand and nOutstanding, currently not used.
 *      - careful look at the thread interactions and when to toss an entry.
 *      - do something to make sure the requests go out in order.
 */
public class FilePtrHttpReadAheadCache extends FilePtrHttp {

    private static final int MAX_CACHE_ENTRIES = 10;

    private final boolean useCache;

    private static final int nReadAhead = 5;

    private static final boolean debugOutput = false;

    public static FilePtr get(String fnamp) throws MalformedURLException, IOException {
        if (!fnamp.startsWith("http")) return null;
        FilePtrHttpReadAheadCache fp = new FilePtrHttpReadAheadCache(fnamp, 300);
        if (!fp.init()) {
            fp.close();
            fp = null;
        }
        return fp;
    }

    /**
   * Creates a new FilePtr instance. Well, the parameters are rather
   * &quot;funny&quot; for now, but there were reasons for it. I will
   * change it later (hopefully)...<br/>
   * If you do not need to read randomly and you have access to the file
   * directly, you should use the BufferedInputStream etc. -classes, as
   * they are MUCH faster than the RandomAccessFile class that is used
   * here.
   */
    public FilePtrHttpReadAheadCache(String fnamp, int bufsize) throws MalformedURLException {
        super(fnamp, bufsize);
        useCache = true;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    private boolean init() throws IOException {
        urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setRequestMethod("HEAD");
        urlCon.setRequestProperty("User-Agent", useragent);
        if (urlCon.getResponseCode() != HttpURLConnection.HTTP_OK) return false;
        if ((savedLength = urlCon.getHeaderFieldInt("Content-Length", -1)) < 0) {
            return false;
        }
        String s;
        if ((s = urlCon.getHeaderField("Accept-Ranges")) == null || !s.equals("bytes")) {
            System.err.println("Server does not accept HTTP range requests." + " Aborting!");
            return false;
        }
        return true;
    }

    @Override
    protected void readToBuffer() throws IOException {
        if (useCache) {
            readToBufferCached();
            return;
        }
        if (data == null) {
            data = new byte[bufsize];
        }
        super.readToBuffer();
    }

    private void readToBufferCached() throws IOException {
        if (fpos != 0 && fpos < 2100 && fnamp.endsWith("sepl_18.se1")) {
            load(fpos, 5 * 300);
        }
        long idx = fpos / bufsize;
        boolean isError = false;
        CacheEntry ce;
        String debugInfo = null;
        synchronized (cache) {
            ce = cache.get(idx);
            if (debugOutput) debugInfo = cache.toString();
        }
        if (ce != null) {
            synchronized (ce) {
                while (ce.isBusy) {
                    try {
                        if (debugOutput) System.err.format("WAIT: %5d %s\n", idx, debugInfo);
                        ce.wait();
                        isError = ce.isError;
                    } catch (InterruptedException ex) {
                        isError = true;
                    }
                }
            }
            if (isError) {
                synchronized (cache) {
                    cache.remove(idx);
                }
                ce = null;
            }
        }
        if (ce != null) {
            if (debugOutput) System.err.format("HIT: %5d %10d %s %s\n", idx, fpos, fnamp, cache.toString());
            startIdx = ce.startIdx;
            endIdx = ce.endIdx;
            data = ce.data;
            return;
        }
        isError = false;
        if (debugOutput) System.err.format("READ: %5d %10d %s %s\n", idx, fpos, fnamp, cache.toString());
        urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setRequestProperty("User-Agent", useragent);
        long offsetForGet = idx * bufsize;
        urlCon.setRequestProperty("Range", "bytes=" + offsetForGet + "-" + SMath.min(length() - 1, offsetForGet + bufsize - 1));
        urlCon.connect();
        int rc = urlCon.getResponseCode();
        int contentLength = urlCon.getHeaderFieldInt("Content-Length", -1);
        int length = -1;
        if (rc != HttpURLConnection.HTTP_OK && rc != HttpURLConnection.HTTP_PARTIAL) {
            isError = true;
        } else {
            InputStream is = urlCon.getInputStream();
            byte[] newData = new byte[bufsize];
            length = is.read(newData);
            if (is.available() > 0 || length > bufsize || (length < bufsize && savedLength >= 0 && offsetForGet + length != savedLength)) {
                isError = true;
            } else {
                startIdx = offsetForGet;
                endIdx = offsetForGet + length;
                ce = new CacheEntry(idx, startIdx, endIdx);
                ce.data = newData;
                data = newData;
                synchronized (cache) {
                    cache.put(idx, ce);
                }
            }
        }
        if (isError) {
            throw new IOException("HTTP read failed with HTTP response " + rc + ". Read " + length + " (" + contentLength + ") bytes, requested " + bufsize + " bytes.");
        }
    }

    int nOutstanding;

    public void load(long pos, int nBytes) {
        if (nBytes <= 0) return;
        List<CacheEntry> l = new ArrayList();
        long stopIdx = pos + nBytes;
        try {
            synchronized (cache) {
                for (; pos < stopIdx; pos += bufsize) {
                    long idx = pos / bufsize;
                    if (cache.containsKey(idx)) continue;
                    if (nOutstanding >= nReadAhead) return;
                    long end = 0;
                    long start = idx * bufsize;
                    try {
                        end = SMath.min(length(), start + bufsize);
                    } catch (IOException ex) {
                    }
                    CacheEntry ce = new CacheEntry(idx, start, end);
                    ce.isBusy = true;
                    cache.put(idx, ce);
                    l.add(ce);
                }
            }
        } finally {
            for (CacheEntry ce : l) {
                try {
                    new FillCacheEntry((HttpURLConnection) url.openConnection(), ce).start();
                } catch (IOException ex) {
                    synchronized (cache) {
                        cache.remove(ce.idx);
                    }
                }
            }
        }
    }

    private class FillCacheEntry extends Thread {

        private final CacheEntry ce;

        private final HttpURLConnection urlCon;

        public FillCacheEntry(HttpURLConnection urlCon, CacheEntry ce) {
            this.ce = ce;
            this.urlCon = urlCon;
        }

        @Override
        public void run() {
            boolean isError = false;
            int rc = 0;
            int contentLength = -1;
            int length = -1;
            long idx = ce.idx;
            byte[] newData = null;
            try {
                if (debugOutput) System.err.format("AHEAD: %5d %10d %s\n", idx, fpos, fnamp);
                urlCon.setRequestProperty("User-Agent", useragent);
                urlCon.setRequestProperty("Range", "bytes=" + ce.startIdx + "-" + (ce.endIdx - 1));
                urlCon.connect();
                rc = urlCon.getResponseCode();
                contentLength = urlCon.getHeaderFieldInt("Content-Length", -1);
                length = -1;
                if (rc != HttpURLConnection.HTTP_OK && rc != HttpURLConnection.HTTP_PARTIAL) {
                    isError = true;
                } else {
                    InputStream is = urlCon.getInputStream();
                    newData = new byte[bufsize];
                    length = is.read(newData);
                    if (is.available() > 0 || length > bufsize || (length < bufsize && savedLength >= 0 && ce.startIdx + length != savedLength)) {
                        isError = true;
                    }
                }
            } catch (IOException ex) {
                isError = true;
            }
            synchronized (ce) {
                ce.isBusy = false;
                ce.isError = isError;
                ce.data = newData;
                ce.notify();
            }
            if (debugOutput) System.err.format("DONE: %5d, error: %b\n", idx, isError);
            if (isError) {
                System.err.println("HTTP read failed with HTTP response " + rc + ". Read " + length + " (" + contentLength + ") bytes, requested " + bufsize + " bytes.");
            }
        }
    }

    private final Cache cache = new Cache();

    private static class Cache extends LinkedHashMap<Long, CacheEntry> {

        private int max_entries;

        Cache() {
            this(MAX_CACHE_ENTRIES);
        }

        Cache(int max_entries) {
            super(16, .75f, true);
            this.max_entries = max_entries;
        }

        @Override
        protected boolean removeEldestEntry(Entry<Long, CacheEntry> eldest) {
            return size() > max_entries;
        }

        @Override
        public String toString() {
            synchronized (this) {
                StringBuilder sb = new StringBuilder();
                sb.append("{ [");
                sb.append(size());
                sb.append("] ");
                for (CacheEntry ce : this.values()) {
                    sb.append(ce.idx);
                    sb.append(' ');
                }
                sb.append('}');
                return sb.toString();
            }
        }
    }

    private static class CacheEntry {

        long idx;

        long startIdx;

        long endIdx;

        byte[] data;

        boolean isBusy;

        boolean isError;

        public CacheEntry(long idx, long startIdx, long endIdx) {
            this.idx = idx;
            this.startIdx = startIdx;
            this.endIdx = endIdx;
        }
    }
}
