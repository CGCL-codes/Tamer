package com.entelience.probe.httplog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.entelience.probe.LocalFileState;
import com.entelience.probe.TextFileImport;
import com.entelience.util.DateHelper;
import com.entelience.probe.ForeignDbInfo;
import com.entelience.probe.ProbeException;
import com.entelience.probe.InvalidLogLineException;
import com.entelience.probe.httplog.objects.HttpLogData;
import com.entelience.probe.httplog.provider.DbHttp;
import com.entelience.probe.httplog.provider.DomainSiteHelper;
import com.entelience.objects.http.HttpStatus;
import com.entelience.util.NetIP;
import com.entelience.util.StringHelper;

/**
 *  Imports data from ironport Access Logs and put it into HttpLogData objects.
 *
 *  IronPort access log file entry (WSA 6.3.0 end user doc)
 *
 *  1/ Unix timestamp
 *  2/ Elapsed time (latency)
 *  3/ Client IP Adress
 *  4/ Transaction result code (ie. TCP_MISS)
 *  5/ HTTP Response code
 *  6/ Response size (headers + body)
 *  7/ Request (URL encoded for native FTP) (ie. GET http://www.example.com)
 *  8/ Back end information (NONE : use cache, DIRECT : go for it , DEFAULT_PARENT : up the food chain) indicates which server was contacted
 *  9/ Mime type
 * 10/ ACL decision tag (ie. ALLOW_WBRS)
 * 11/ Access or Description Policy group name (spaces are _), default is DefaultGroup
 * 12/ Identity Policy Group (spaces are _)
 * 13/ DataSecurityPolicyGroup (spaces are _)
 * 14/ External DLP Policy Group (space are _)
 * 15/ Routing Policy Group (spaces are _)
 * 16/ Web Reputation filtering and AntiMalware (ie. <IW_misc,9.9,-,-,-,-,-,-,-,-,-,-,-,-,-,IW_misc,->)
 * 17/ - "Date with timezone" (ie. - "09/Feb/2011:08:14:17 +0100")
 */
public class IronportAccessLogImport extends GenericHttpLogImport {

    private static enum TRANSACTION_RESULT {

        TCP_HIT, TCP_IMS_HIT, TCP_MEM_HIT, TCP_MISS, TCP_REFRESH_HIT, TCP_CLIENT_REFRESH_MISS, TCP_DENIED, NONE, FTP_HIT, FTP_MEM_HIT, FTP_MISS, FTP_REFRESH_HIT, FTP_DENIED;

        public boolean isFtp() {
            return (this == FTP_HIT || this == FTP_MEM_HIT || this == FTP_MISS || this == FTP_REFRESH_HIT || this == FTP_DENIED);
        }

        public boolean isBlocked() {
            return (this == TCP_DENIED || this == FTP_DENIED);
        }
    }

    private static enum DECISION_TAG {

        ALLOW_ADMIN(true), ALLOW_ADMIN_ERROR_PAGE(true), ALLOW_WBRS(true), ALLOW_CUSTOMCAT(true), BLOCK_ADMIN(false), BLOCK_ADMIN_CONNECT(false), BLOCK_ADMIN_CUSTOM_USER_AGENT(false), BLOCK_ADMIN_DLP(false), BLOCK_ADMIN_FILE_TYPE(false), BLOCK_ADMIN_PROTOCOL(false), BLOCK_ADMIN_SIZE(false), BLOCK_ADMIN_SIZE_DLP(false), BLOCK_AMW_REQ(false), BLOCK_AMW_RESP(false), BLOCK_AMW_RESP_URL(false), BLOCK_CONTINUE_WEBCAT(false), BLOCK_ICAP(false), BLOCK_SUSPECT_USER_AGENT(false), BLOCK_WBRS(false), BLOCK_WBRS_DLP(false), BLOCK_WEBCAT(false), BLOCK_WEBCAT_DLP(false), BLOCK_UNSUPPORTED_SEARCH_APP(false), BLOCK_SEARCH_UNSAFE(false), BLOCK_CUSTOMCAT(false), BLOCK_CONTENT_UNSAFE(false), BLOCK_AVC(false), BLOCK_CONTINUE_CUSTOMCAT(false), BLOCK_CONTINUE_CONTENT_UNSAFE(false), DEFAULT_CASE(true), MONITOR_AMW_REQ(true), MONITOR_AMW_RESP_URL(true), MONITOR_AMW_RESP(true), MONITOR_DLP(true), MONITOR_SUSPECT_USER_AGENT(true), MONITOR_WBRS(true), MONITOR_AVC(true), MONITOR_CONTINUE_WEBCAT(true), MONITOR_CONTINUE_CUSTOMCAT(true), MONITOR_CONTINUE_CONTENT_UNSAFE(true), NO_AUTHORIZATION(false), NO_PASSWORD(false), SAAS_AUTH(true), REDIRECT_CUSTOMCAT(true), OTHER(true);

        private boolean auth;

        DECISION_TAG(boolean _auth) {
            auth = _auth;
            _logger.trace("Decision tag (" + this + ") is authorized (" + Boolean.toString(auth) + ")");
        }

        public String getAuth() {
            return (auth == true ? DbHttp.ALLOWED : DbHttp.DENIED);
        }
    }

    private static enum URL_CATEGORIES {

        SEAR("Search Engines", 7503), SPOR("Sports", 10), TRAV("Travel", 11), HOBB("Hobbies & Recreation", 12), GAMB("Gambling", 13), HEAL("Health & Medecine", 14), NEWS("News", 20), FINA("Finance & Investment", 25), FASH("Fashion & Beauty", 9501), KIDS("Kids site", 7003), GOVE("Government", 40), GAME("Games", 1202), ARTS("Arts", 50), ENTE("Entertainment", 51), CHAT("Chat", 3001), SOCI("Society & Culture", 3003), JOB("Job Search & Carrer Development", 60), RELI("Religion", 3006), REAL("Real Estate", 3010), PHIL("Philantropic & Professional Orgs.", 9803), EDUC("Education", 70), PEER("Peer-to-Peer", 9801), INFR("Infrastructure", 9802), COMP("Computing & Internet", 75), RING("Ringtones/Mobile Phone Downloads", 9804), MOTO("Motor Vehicles", 1101), POLI("Politics", 9806), SUSP("Suspect/Threat URLs", 9101), HACK("Hacking", 7504), SEX("Sex education", 1490), WEB_("Web-based email", 7507), STRE("Streaming Media", 7509), REFE("Reference", 7001), ADUL("Adult/Sexually explicit", 90), CRIM("Criminal Activity", 91), INTO("Intolerance & Hate", 92), VIOL("Violence", 93), WEAP("Weapons", 94), INTI("Intimate Apparel & Swimwear", 95), PERS("Personals & Dating", 96), PHOT("Photo Searches", 97), PROX("Proxies & Translators", 98), HOST("Hosting Sites", 99), BUSI("Business", 100), SHOP("Shoppping", 80), FOOD("Food & Dining", 3004), BLOG("Blogs & Forums", 2002), ADVE("Advertisement & popups", 76), DOWN("Downloads", 7501), ILLE("Illegal Drugs", 1403), ALCO("Alcohol & Tobacco", 1404), TAST("Tasteless & offensive", 9301), _("Url Filtering Bypassed", 1073741824), NC("Uncategorized URLs", 1073741825), ERR("URL Filtering Bypassed", 1073741826), ADLT("Adult", 1006), ADV("Advertisements", 1027), AT("Alcohol and Tobacco", 1048), ART("Arts and Entertainment", 1002), PLAG("Cheating and Plagiarism", 1051), CPRN("Child Porn", 1064), CSEC("Computer Security", 1065), CULT("Cults", 1041), DATE("Dating", 1055), EDU("Education", 1001), FTS("File Transfer Services", 1071), FILT("Filter Avoidance", 1025), FNNC("Finance", 1015), FREE("Freeware and ShareWare", 1068), GOV("Government and Law", 1011), HATE("Hate Speech", 1016), HLTH("Health and Nutrition", 1009), CRMR("Illegal Activities", 1022), DRUG("Illegal Drugs", 1047), MISC("Infrastructure", 1018), IM("Instant Messaging", 1039), VOIP("Internet Telephony", 1067), LING("Lingerie and Swimsuits", 1031), LOTR("Lottery and sweepstakes", 1034), CELL("MObile Phones", 1070), NATR("Nature", 1013), NSN("Non-Sexual Nudity", 1060), COMM("Online Communities", 1024), OSB("Online Storage and Backup", 1066), TRAD("Online Trading", 1028), NONM("Paranormal and Occult", 1029), P2P("Peer File Transfer", 1056), PORN("Porn", 1054), REST("Real Estate", 1045), FYI("Reference", 1017), YC("Safe for Kids", 1057), SCI("Science and Technology", 1012), SRCH("Search Engines and Portals", 1020), SXED("Sex Ed and Abortion", 1052), SNET("Social Networking", 1069), SOCS("Social Science", 1014), SCTY("Society and Culture", 1010), SWUP("Software Updates", 1053), SPRT("Sports and Recreation", 1008), MDIA("Streaming Media", 1026), OBS("Tasteless or Obscene", 1033), TAT("Tattoos", 1043), TRNS("Transportation", 1044), TRVL("Travel", 1046), WHST("Web Hosting", 1037), TRAN("Web Page Translation", 1063), MAIL("Web-based email", 1038);

        private String full_category;

        private int code;

        URL_CATEGORIES(String _full_category, int _code) {
            full_category = _full_category;
            code = _code;
            _logger.trace("Url category (" + full_category + ") for abbreviation (" + this + ")");
        }

        public String getFullCategory() {
            return full_category;
        }

        public int getCode() {
            return code;
        }
    }

    private static enum MALWARE_SCANNING {

        S_0("UNKNOWN"), S_1("NOT SCANNED OR SKIPPED"), S_2("TIMEOUT"), S_3("UNSCANNABLE"), S_4("UNSCANNABLE"), S_5("FREELIST EXHAUSTION"), S_6("BAD VERDICT"), S_7("NO LAUNCH METHOD"), S_10("OTHER MALWARE"), S_12("BROWSER HELPER OBJECT"), S_13("ADWARE"), S_14("SYSTEM MONITOR"), S_18("COMMERCIAL SYSTEM MONITOR"), S_19("DIALER"), S_20("HIJACKER"), S_21("PHISHING URL"), S_22("TROJAN DOWNLOADER"), S_23("TROJAN HORSE"), S_24("TROJAN PHISHER"), S_25("WORM"), S_26("ENCRYPTED FILE"), S_27("VIRUS"), S_28("TOO MANY OUTSTANDING REQUESTS"), S_33("OTHER MALWARE"), S_34("PUA");

        private String category;

        MALWARE_SCANNING(String _category) {
            category = _category;
            _logger.trace("Malware scanning category (" + category + ") for (" + this + ")");
        }

        public String getCategory() {
            return category;
        }

        public static MALWARE_SCANNING getCategoryName(String name) {
            for (MALWARE_SCANNING ms : MALWARE_SCANNING.values()) {
                if (ms.getCategory().equalsIgnoreCase(name)) return ms;
            }
            return null;
        }

        public static MALWARE_SCANNING findCategory(String in) {
            return (StringHelper.checkStringIsDigit(in) ? MALWARE_SCANNING.valueOf("S_" + in) : MALWARE_SCANNING.getCategoryName(in));
        }
    }

    private static String[] knownInvalidLine = { "NONE/0 0  - NONE/- - OTHER-NONE-NONE-NONE-NONE <-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,->" };

    private List<MALWARE_SCANNING> categoryAsMalware = new ArrayList<MALWARE_SCANNING>();

    private List<TRANSACTION_RESULT> ignoreTransactionResults;

    private List<String> unknownCategory = new ArrayList<String>();

    private List<String> customDecisionTag = new ArrayList<String>();

    private Integer logVersion = 7;

    private boolean ignoreField9 = true;

    /** Sets the major/compatible log format */
    public void setVersion(Integer _version) {
        logVersion = _version;
    }

    /** Ignore fields 9 of the log, which relates to user */
    public void setIgnoreField9(boolean _ignoreField9) {
        ignoreField9 = _ignoreField9;
    }

    /** Sets the transaction to ignore */
    public void setIgnoreTransactionResults(String[] list) {
        ignoreTransactionResults = new ArrayList<TRANSACTION_RESULT>();
        if (list != null && list.length > 0) {
            for (String tr : list) {
                try {
                    TRANSACTION_RESULT trans = TRANSACTION_RESULT.valueOf(tr);
                    ignoreTransactionResults.add(trans);
                } catch (IllegalArgumentException e) {
                    _logger.error("Unknown transaction result (" + tr + ")");
                }
            }
        }
    }

    /** Sets the list of malware type to be counted as virus */
    public void setCategoryAsMalware(String[] list) {
        if (list != null && list.length > 0) {
            for (String cat : list) {
                try {
                    MALWARE_SCANNING mlc = MALWARE_SCANNING.valueOf("S_" + cat);
                    categoryAsMalware.add(mlc);
                } catch (IllegalArgumentException e) {
                    _logger.error("Unknown webroot malware scanning verdict (" + cat + ")");
                }
            }
        }
    }

    @Override
    protected boolean isHeader() {
        String line = getCurrentLine();
        return line != null && line.length() > 0 && line.charAt(0) == '#';
    }

    /**
     * this is the regexp that parses an access.log line  
     */
    public static final Pattern p_access_log_v5 = Pattern.compile("(\\d*\\.\\d*)\\s*(\\d*)\\s(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\s([a-zA-Z_]*)/(\\d{3})\\s(\\d*)\\s([a-zA-Z_]*)\\s(\\S*)\\s(\\S*)\\s([a-zA-Z_]*)/(.*)\\s(\\S*)\\s([a-zA-Z_]*)(-(\\S*))?\\s<([a-zA-Z_-]*),(\\S*)>(.*)");

    public static final Pattern p_access_log = Pattern.compile("(\\d+\\.\\d+)\\s*" + "([\\d\\-]+)\\s" + "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\s" + "([a-zA-Z_]*)/" + "(\\d+)\\s" + "(\\d*)\\s+" + "([a-zA-Z_\\-]*)\\s" + "(\\S+)\\s" + "((?:\".*\")|(?:\\S+))\\s" + "([a-zA-Z_]*)/" + "(.*)\\s" + "([a-zA-Z0-9_\\-]+/{0,1}[a-zA-Z0-9_\\-\\+\\.]*)\\s" + "([a-zA-Z0-9_]+)\\-" + "([a-zA-Z0-9_\\-]+)\\s" + "<(.*)>" + "(.*)");

    /**
	 * Process the anti-malware and reputation filter <....> with , to delimit field. An empty field i '-'.
	 *
     * Old format 
     *
	 * 0/ URL category (ie. IW_adv or comp)
	 * 1/ Web reputation filters score (ie. ns)
	 * 2/ Malware scanning verdict (ie. 13 0) Ironport internal
	 * 3/ Name of the spyware (ie. Comedy-Planet)
	 * 4/ Webroot specific value
	 * 5/ Webroot threat identifier
	 * 6/ Webroot trace identifier
	 * 7/ Malware scanning verdict McAfee passed to the DVS engine
	 * 8/ Name of file scanned by McAdee
	 * 9/ Value used by McAfee, scan error
	 * 10/ Value used by McAfee as detection type
	 * 11/ Value used as virus type by McAfee
	 * 12/ Name of the virus by McAfee
	 * 13/ Ironport Data Security scan verdict (O = allow, 1 = Block, - = no scannning)
	 * 14/ External DLP scan verdict (0 = allow, 1 = Block, - = no scanning)
	 * 15/ URL Category verdict during request-side scanning (- = filtering is disabled)
	 * 16/ URL Category verdict during response-side scannning
     *
     * New format (v7.1)
     * 
     * 13/ The malware scanning verdict Sophos passed to the DVS engine.
     * 14/ A value that Sophos uses as a scan return code. 
     * 15/ The file location where Sophos found the objectionable content. 
     * 16/ A value that Sophos uses as the threat name. 
     * 17/ Ironport Data Security scan verdict (O = allow, 1 = Block, - = no scannning)
	 * 18/ External DLP scan verdict (0 = allow, 1 = Block, - = no scanning)
     * 19/ URL Category verdict during request-side scanning (- = filtering is disabled)
	 * 20/ URL Category verdict during response-side scannning
     * 21/ Unified response-side anti-malware scanning verdict independent of which scanning engines are enabled. 
     * 22/ The threat type returned by the Web Reputation filters 
     * 23/ The application name as returned by the AVC engine, if applicable.
     * 24/ The application type as returned by the AVC engine, if applicable.
     * 25/ The application behavior as returned by the AVC engine, if applicable.
     * 26/ Safe browsing scanning verdict. 
     * 27/ The average bandwidth consumed serving the request in Kb per second
     * 28/ A value that indicates whether or not the request was throttled due to bandwidth limit control settings. 
     * 29/ The type of user making the request, either “local” or “remote.”
     * 30/ Unified request-side anti-malware scanning verdict independent of which scanning engines are enabled. 
     * 31/ The threat name assigned to the client request that was blocked or monitored due to an applicable Outbound Malware Scanning Policy.
	 */
    public HttpLogData treatAntiMalwareField(String field, HttpLogData data) throws InvalidLogLineException {
        if (field == null) {
            _logger.error("A null anti-malware/reputation filter was found !");
            return data;
        }
        String[] filter = field.split(",");
        if (filter.length != 17 && filter.length != 32) {
            _logger.error("The expected size for the anti-malware/reputation filter is 17 or 31 fields, found (" + filter.length + ") in (" + field + ")");
            throw new InvalidLogLineException("Invalid line (" + getCurrentLine() + ")");
        }
        boolean extendedFormat = (filter.length == 32);
        StringHelper.unquote(filter);
        if (filter[0] != null && !"nc".equals(filter[0]) && !"-".equals(filter[0])) {
            data.category = getCategory(filter[0]);
            if ("Unavailable".equalsIgnoreCase(data.category) && !unknownCategory.contains(filter[0])) {
                _logger.warn("Unknown url category (" + filter[0] + ")");
                unknownCategory.add(filter[0]);
            }
        }
        processMalwareVerdict(data, filter, 2, 3, 0);
        processMalwareVerdict(data, filter, 7, 12, 8);
        if (extendedFormat) processMalwareVerdict(data, filter, 13, 16, 15);
        return data;
    }

    /**
     * Process malware verdict
     */
    private void processMalwareVerdict(HttpLogData data, String[] filter, int verdictField, int virusField, int fileField) {
        if (filter[verdictField] != null && !"1".equals(filter[verdictField]) && !"0".equals(filter[verdictField]) && !"-".equals(filter[verdictField]) && !"Skipped".equalsIgnoreCase(filter[verdictField])) {
            try {
                MALWARE_SCANNING ms = MALWARE_SCANNING.findCategory(filter[verdictField]);
                if (categoryAsMalware.contains(ms)) {
                    data.virus_blocked = true;
                    data.virus_type = ms.getCategory();
                    if (filter[virusField] != null && !"-".equals(filter[virusField])) {
                        data.virus_name = filter[virusField].trim();
                        data.virus_file_name = (fileField > 0 ? filter[fileField] : null);
                        _logger.trace("Found virus (" + data.virus_name + ") of category (" + data.virus_type + ") for verdict (" + ms + ")");
                    }
                }
            } catch (IllegalArgumentException e) {
                _logger.warn("Unknown malware verdict (" + filter[verdictField] + ")");
            }
        }
    }

    /**
     * transform a line into a HttpLogData
     */
    protected HttpLogData toHttpLogData(String line) throws Exception {
        return (logVersion < 6 ? treatHttpLogDataVersion5(line) : treatHttpLogDataVersion6(line));
    }

    /**
	 * Process v6 and v7 access logs
	 */
    private HttpLogData treatHttpLogDataVersion6(String line) throws Exception {
        Matcher m = p_access_log.matcher(line);
        HttpLogData hld = null;
        if (m.matches()) {
            if (m.groupCount() != 16) {
                _logger.error("Expecting 16 matching group and got (" + m.groupCount() + " for line (" + getCurrentLine() + ")");
                throw new InvalidLogLineException("Invalid line (" + getCurrentLine() + ")");
            }
            hld = new HttpLogData();
            try {
                hld.datetime = DateHelper.fromUnixTimestamp(Double.parseDouble(m.group(1)));
            } catch (Exception e) {
                throw new InvalidLogLineException("Invalid date in line (" + getCurrentLine() + ")");
            }
            ensureDateOk(hld.datetime);
            hld.ip_client = m.group(3);
            if (!NetIP.isValidIP(hld.ip_client) && !NetIP.isIPv6(hld.ip_client)) throw new InvalidLogLineException("Invalid IP address in line (" + getCurrentLine() + ")");
            try {
                hld.time_taken = Long.parseLong(m.group(2));
                hld.http_status = Integer.parseInt(m.group(5));
                hld.volume_server_to_client = Long.parseLong(m.group(6));
            } catch (NumberFormatException nfe) {
                _logger.warn("Failed to a key item that should have been a number");
                throw new InvalidLogLineException("Invalid line (" + getCurrentLine() + ")");
            }
            HttpStatus status = httpStatus.get(hld.http_status);
            if (status == null) throw new InvalidLogLineException("Unknown HTTP status (" + status + ")");
            hld.mime_type = ("-".equals(m.group(12)) ? null : m.group(12));
            if (!ignoreField9) hld.user_name = ("-".equals(hld.user_name) ? null : m.group(9));
            TRANSACTION_RESULT tr = null;
            try {
                tr = TRANSACTION_RESULT.valueOf(m.group(4));
                if (ignoreTransactionResults.contains(tr)) {
                    return null;
                }
            } catch (IllegalArgumentException e) {
                _logger.warn("Unknown transaction result (" + m.group(4) + ").");
                throw new InvalidLogLineException("Invalid line (" + getCurrentLine() + ")");
            }
            hld.type = (tr.isFtp() ? DbHttp.FTP_PROTOCOL : getTraficTypeFromHttpMethod(m.group(7)));
            DECISION_TAG tag;
            try {
                tag = DECISION_TAG.valueOf(m.group(13));
            } catch (IllegalArgumentException e) {
                if (!customDecisionTag.contains(m.group(13))) {
                    _logger.warn("Unknown decision tag (" + m.group(13) + ") for transaction (" + tr + "). Checking if transaction denied or decision tag contains BLOCK");
                    customDecisionTag.add(m.group(13));
                }
                if (tr.isBlocked()) {
                    tag = DECISION_TAG.BLOCK_WBRS;
                } else {
                    tag = (m.group(13).contains("BLOCK") ? DECISION_TAG.BLOCK_WBRS : DECISION_TAG.ALLOW_WBRS);
                }
            }
            hld.authorization = tag.getAuth();
            String url = m.group(8);
            try {
                fillDomainAndSiteFromUrl(hld, url);
            } catch (Exception e) {
                DbHttp dbHttp = logImport.getHttpDb();
                dbHttp.invalidUrlDaily(url, hld.datetime, hld.ip_client, hld.user_name);
                return null;
            }
            hld.site_name = StringHelper.nullify(hld.site_name);
            hld.domain_name = StringHelper.nullify(hld.domain_name);
            if (hld.site_name == null) {
                _logger.trace("Found a null site name for url (" + url + ")");
            }
            if (hld.domain_name == null) {
                _logger.warn("Unable to retrieve the domain name from (" + url + ")");
                logImport.getHttpDb().invalidUrlDaily(url, hld.datetime, hld.ip_client, hld.user_name);
                throw new InvalidLogLineException("Invalid line (" + getCurrentLine() + ")");
            }
            if (!DomainSiteHelper.hasDots(hld.domain_name)) {
                if (hld.site_name == null) {
                    _logger.debug("Found a domain name without any dots (" + hld.domain_name + ") and a null site name");
                    resolvedWithDns(hld, hld.domain_name);
                } else {
                    if (DomainSiteHelper.DEFAULT_FOR_IP_ONLY.equals(hld.domain_name)) {
                        resolvedWithDns(hld, hld.site_name);
                    } else {
                        _logger.warn("Found a domain name without any dots (" + hld.domain_name + ") and for site (" + hld.site_name + ")");
                    }
                }
            }
            hld = treatAntiMalwareField(m.group(15), hld);
        } else {
            for (String s : knownInvalidLine) {
                if (line.contains(s)) {
                    _logger.trace("Skipping known invalid line (" + s + ")");
                    return null;
                }
            }
            throw new InvalidLogLineException("Invalid line (" + getCurrentLine() + ")");
        }
        return hld;
    }

    private HttpLogData treatHttpLogDataVersion5(String line) throws Exception {
        Matcher m = p_access_log_v5.matcher(line);
        HttpLogData hld = null;
        if (m.matches()) {
            hld = new HttpLogData();
            hld.datetime = DateHelper.fromUnixTimestamp(Double.parseDouble(m.group(1)));
            ensureDateOk(hld.datetime);
            hld.time_taken = Long.parseLong(m.group(2));
            hld.ip_client = m.group(3);
            hld.http_status = Integer.parseInt(m.group(5));
            hld.volume_server_to_client = Long.parseLong(m.group(6));
            hld.type = getTraficTypeFromHttpMethod(m.group(7));
            String url = m.group(8);
            hld.user_name = m.group(9);
            if ("-".equals(hld.user_name)) hld.user_name = null;
            hld.mime_type = m.group(12);
            String actionResult = m.group(13);
            hld.category = getCategory(m.group(16));
            hld.authorization = getAuth(actionResult);
            try {
                String host = dbh.extractHostNameFromUrl(url);
                hld.site_name = dbh.getSiteName(host);
                hld.domain_name = dbh.getDomainName(host);
            } catch (Exception e) {
                logImport.getHttpDb().invalidUrlDaily(url, hld.datetime, hld.ip_client, hld.user_name);
                return null;
            }
        } else {
            _logger.warn("Line [" + line + "] not matching known log format");
            throw new InvalidLogLineException("Invalid line (" + getCurrentLine() + ")");
        }
        return hld;
    }

    private int getTraficTypeFromHttpMethod(String httpMethod) {
        return ("CONNECT".equalsIgnoreCase(httpMethod) ? DbHttp.HTTPS_PROTOCOL : DbHttp.HTTP_PROTOCOL);
    }

    /**
	 * Returns the category if available
	 * "The Web Security appliance uses the first four characters of custom URL category names preceded by “c_” in the access logs."
     *  "The URL category abbreviations for Cisco IronPort Web Usage Controls include the prefix “IW_” 
     *  before each abbreviation so that the “art” category becomes “IW_art.” 
  	 */
    public String getCategory(String abbrev) {
        String result = null;
        if (abbrev != null) {
            if (abbrev.startsWith("IW_")) abbrev = abbrev.substring(3);
            if (abbrev.endsWith("_")) abbrev = abbrev.substring(1, abbrev.length() - 1);
            abbrev = abbrev.replace("-", "_");
            abbrev = abbrev.toUpperCase();
            if (abbrev.startsWith("C_")) {
                result = "Custom category";
            } else {
                try {
                    URL_CATEGORIES cat = URL_CATEGORIES.valueOf(abbrev);
                    result = cat.getFullCategory();
                } catch (IllegalArgumentException e) {
                    _logger.trace("Unknown category (" + abbrev + ")");
                }
            }
        }
        return (result != null ? result : "Unavailable");
    }

    private static final Pattern p_block = Pattern.compile(".*BLOCK.*");

    private String getAuth(String abbrev) {
        if (p_block.matcher(abbrev).matches()) return DbHttp.DENIED; else return DbHttp.ALLOWED;
    }
}
