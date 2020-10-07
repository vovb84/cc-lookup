package com.sony.cc;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class ccLookupClient {

    private static final ccLookupGetOptLib ccLookupGetOptLib = new ccLookupGetOptLib();
    private static final ccLookupJsonLib ccLookupJsonLib = new ccLookupJsonLib();
    private static final ccLookupUtilLib ccLookupUtilLib = new ccLookupUtilLib();
    private static final ccLookupHttpLib ccLookupHttpLib = new ccLookupHttpLib();

    /* ==========================================
     * =            Constructor                 =
     * ========================================== */
    public ccLookupClient() {}

    public static void main(String[] args) {
        /* get args */
        ccLookupGetOptLib.getOpt(args);

        /* verify if work dir exists and create it if it doesn't */
        boolean bdirExists = ccLookupUtilLib.localDirBuilder()
            .sdirPath(ccLookupGetOptLib.getStrLocalDir())
            .bdirCreate(true)
            .bdirClean(false)
            .buildlocalDir();

        /* Map CountryCode -> CountryName */
        Map<String, String> mapCountryCode = new HashMap<>();

        /* Reversed Map CountryName -> CountryCode*/
        Map<String, String> mapCountryName = new HashMap<>();

        String strLocalFile = ccLookupGetOptLib.strLocalDir +
                "/" +
                ccLookupGetOptLib.strLocalFileName;

        /* if -g flag is set */
        if (ccLookupGetOptLib.isBcollect()) {
            /* get data page as a string and save it in locak file */
            long longTime = ccLookupUtilLib.getCurrentDateTimeEpoch();
            String strTime = ccLookupUtilLib.getDateTimeBuilder()
                    .sdateFormat("yyyy-mm-dd HH:mm:ss")
                    .isUTC(true)
                    .ddate(ccLookupUtilLib.getCurrentDateTime())
                    .buildgetDateTime();
            log.debug("Begin retrieving page from {} at {}.",
                    ccLookupGetOptLib.getStrHttpAddress(),
                    strTime);

            /* get page as a string */
            String strPage = ccLookupHttpLib.getPageBuilder()
                    .intConnectTimeout(3000)
                    .intSocketTimeout(3000)
                    .strURL(ccLookupGetOptLib.getStrHttpAddress())
                    .strQueryPath("")
                    .buildgetPage();
            log.debug("Page from {} retrieved at {}.",
                    ccLookupGetOptLib.getStrHttpAddress(),
                    strTime);
            /* save it to the file */
            boolean bwriteFile = ccLookupUtilLib.writeToFileBuilder()
                    .sbody(strPage)
                    .slocalFileName(strLocalFile)
                    .buildwriteToFile();
            if (bwriteFile) {
                log.info("File {} is written successfully.",
                        strLocalFile);
                if (!ccLookupGetOptLib.bparse) {
                    System.out.println("File " +
                            strLocalFile +
                            "is written successfully.");
                }
            } else {
                log.error("Error writing local file {}.",
                        strLocalFile);
                System.out.println("Error writing local file " +
                        strLocalFile +
                        ". Look for errors in log file");
            }
        }
        /* if -p flag is set */
        if (ccLookupGetOptLib.isBparse()) {
            /* get mapCodeName from the retrieved page */
            Map<String, String> mapCodeName = ccLookupJsonLib.getKeyValueMapBuilder()
                    .sjsonFile(strLocalFile)
                    .strJsonBlockKey(ccLookupGetOptLib.getStrJsonBlockKey())
                    .bisFile(true)
                    .alKeys(ccLookupGetOptLib.getAlCountryCodes())
                    .buildgetKeyValueMap();
            Map<String, String> mapCodeNameSorted = new TreeMap<>(mapCodeName);
            /* print in human readable format */
            if (!mapCodeNameSorted.isEmpty()) {
                for (Map.Entry<String, String> strCodeName : mapCodeNameSorted.entrySet()) {
                    System.out.println(strCodeName.getKey() + " -> " + strCodeName.getValue());
                }
            } else {

            }
        }

    } /* end of MAIN method */

}
