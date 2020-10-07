package com.sony.cc;

import gnu.getopt.Getopt;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
/* for List vars creation */

@Slf4j
@Getter
@Setter
public class ccLookupGetOptLib {

    /* ==========================================
     * =            Constructor                 =
     * ========================================== */
    public ccLookupGetOptLib() {}

    /* ===============================
     * =        variables            =
     * =============================== */
    public int concurRequests = 10;
    public String strLocalDir = "";
    public String strHttpAddress = "";
    public String strLocalFileName = "";
    public String strJsonBlockKey = "data";
    public ArrayList<String> alCountryCodes = null;
    public boolean bhelp = false;
    public boolean bcollect = false;
    public boolean bparse = false;

    /* ===============================
     * =          getOpt             =
     * ===============================
     * method to parse args
     * ===============================
     * Receives:
     *  - arguments:
     *    - n <number> - number of concurrent requests
     *    - c <csv>    - country code: comma separated
     *                   list of country codes
     *    - g          - copy to local file only
     *    - p          - parse already collected server
     *                   file.
     *    - d <dir>    - local dir path
     *    - q <addr>   - http address
     *    - h          - print help info.
     * =============================== */
    public void getOpt(String[] sArgs) {
        Getopt g = new Getopt(
                "com.sony.cc",
                sArgs,
                "n:c:q:gpd:h");
        int iarg;
        while ((iarg = g.getopt()) != -1) {
            switch(iarg) {
                case 'c':
                    setAlCountryCodes(new ArrayList<>(Arrays.asList(g.getOptarg().split("\\ *,\\ *"))));
                    break;
                case 'n':
                    setConcurRequests(Integer.parseInt(g.getOptarg()));
                    break;
                case 'd':
                    setStrLocalDir(g.getOptarg());
                    break;
                case 'q':
                    setStrHttpAddress(g.getOptarg());
                    break;
                case 'g':
                    setBcollect(true);
                    break;
                case 'p':
                    setBparse(true);
                    break;
                case 'h':
                    setBhelp(true);
                    break;
                case '?':
                default:
                    setBhelp(true);
            }
        }
        /* Verify and set some params */
        if (isBhelp()) {
            help();
            System.exit(0);
        }
        if (null == getAlCountryCodes() && !isBcollect()) {
            log.error("At least one country code should be provided.");
            System.out.println("At least one country code should be provided.\n\n");
            help();
            System.exit(1);
        }
        if (getConcurRequests() > 100) {
            setConcurRequests(100);
            log.warn("Number of concurrent requests couldn't be more than 100. Limiting it to 100");
        } else if (getConcurRequests() <= 0) {
            setConcurRequests(10);
            log.warn("Number of concurrent requests couldn't be less than 0. Setting it up to 10");
        }
        if (getStrLocalDir().isEmpty()) {
            setStrLocalDir("/tmp/sony");
        }
        if (getStrHttpAddress().isEmpty()) {
            setStrHttpAddress("https://www.travel-advisory.info/api");
        }
        if (getStrLocalFileName().isEmpty()) {
            setStrLocalFileName("data.json");
        }
        if (isBcollect()) {
            setBcollect(true);
        }
        if (isBparse()) {
            setBparse(true);
        }
        if (!isBcollect() && !isBparse()) {
            setBcollect(true);
            setBparse(true);
        }
    }

    /* ===============================
     * =          help               =
     * ===============================
     * method to return info if any problems
     * =============================== */
    private void help() {
        System.out.println("Usage:\n" +
            "\n" +
            "  java -jar target/cc-lookup-client-<current-version>.jar <options>\n" +
            "  java -jar target/cc-lookup-client-0.1-SNAPSHOT.jar <options>\n" +
            "\n" +
            "  <options>:\n" +
            "\n" +
            "    -n <number-of-concurrent-requests> - number of concurrent requests\n" +
            "       Limited to 100. Optional. [default]: 10\n" +
            "\n" +
            "    -q <http-addr> - server http address + page.\n" +
            "       Optional. [default]: https://www.travel-advisory.info/api\n" +
            "\n" +
            "    -d <local-dir> - Local dir to save page.\n" +
            "       Optional. [default]: /tmp/sony\n" +
            "\n" +
            "    -c - Country codes separated by comma.\n" +
            "       Ex: AU, AX, etc.\n" +
            "\n" +
            "    -p - Parse already collected file only.\n" +
            "       Optional. [default]: false.\n" +
            "\n" +
            "    -g - Collect page to the file only.\n" +
            "       Optional. [default]: false.\n" +
            "\n" +
            "   -h show help.\n" +
            "\n" +
            "Notes:\n" +
            "  1. -c argument is required in all situations except -g.\n" +
            "  2. If -n is > then 100 it automatically got limited to 100.\n" +
            "  3. Log file is located in /usr/local/var/log. To change the location and log\n" +
            "     level - edit src/main/resources/log4j.properties file and recompile app.");
    }

}
