package com.sony.cc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import com.google.inject.Inject;
import lombok.Builder;

/*
 * class TWJsonLib
 */
@Slf4j
public class ccLookupJsonLib {

    /* ==========================================
     * =            Constructor                 =
     * ========================================== */
    ccLookupJsonLib() {}

    /* ==========================================
     * =        loadJsonObjFromFile             =
     * ==========================================
     * Method to create JSON object from file
     * Receives:
     *  - sfN: JSON file path/name.
     * Returns:
     *  - jfJO: file JSON object
     * ========================================== */
    private JSONObject loadJsonObjFromFile(
            String sfN) {
        JSONObject jfJO;
        File fjsonFile = new File(sfN);
        if (fjsonFile.exists()) {
            try {
                InputStream ifileStream = new FileInputStream(sfN);
                String sjsonTxt = IOUtils.toString(ifileStream, "UTF-8");
                jfJO = new JSONObject(sjsonTxt);
            } catch (JSONException e) {
                log.error("File {} " +
                                " is not proper JSON format file. Exception: {}",
                        sfN, e.toString());
                jfJO = new JSONObject();
            } catch (Exception e) {
                log.error("Error opening file {}" +
                                " for streaming or creating JSON object with exception: {}",
                        sfN, e.toString());
                jfJO = new JSONObject();
            }
        } else {
            log.error("File {} doesn't exist.", sfN);
            jfJO = new JSONObject();
        }
        return jfJO;
    }

    /* ==========================================
     * =             getKeyValueMap             =
     * ==========================================
     * Method to get Map of keys: String pairs
     * from provided JSON file JSONObject-block.
     * Receives:
     *  - String sjsonFile: JSON file path/name or
     *     JSON string (depends on 3rd param)
     *  - ArrayList alKeys: list of country codes
     *  - String strJsonBlockKey: json block key name
     *     where all country codes are located.
     *  - boolean bisFile:
     *    - true: if sjsonFile is file path/name
     *    - false: if sjsonFile is actual JSONObject string
     * Returns:
     *  - Map<String, String> mapKeyValue.
     *    - Returns empty Map if any error
     * ========================================== */
    @Builder (builderMethodName = "getKeyValueMapBuilder",
            buildMethodName = "buildgetKeyValueMap")
    public Map<String, String> getKeyValueMap(
            String sjsonFile,
            ArrayList<String> alKeys,
            String strJsonBlockKey,
            boolean bisFile) {
        Map<String, String> mapKeyValue = new HashMap<>();
        try {
            JSONObject joFile;
            /* get JSON object */
            if (bisFile) {
                joFile = loadJsonObjFromFile(sjsonFile);
            } else {
                joFile = new JSONObject(sjsonFile);
            }
            /* get JSON object for key:value pairs located in
             * this file under strJsonBlockKey -> CountryCode */
            if (!alKeys.isEmpty()) {
                JSONObject joBlockKey = joFile.getJSONObject(strJsonBlockKey);
                for (String skey : alKeys) {
                    skey = skey.replaceAll("\\ *", "");
                    try {
                        JSONObject joKeyValues = joBlockKey.getJSONObject(skey);
                        mapKeyValue.put(skey, joKeyValues.get("name").toString());
                    } catch(JSONException jem) {
                        log.error("Exception reading JSON values {} from file {}.",
                                jem.toString(),
                                sjsonFile);
                        mapKeyValue.put(skey, "Unknown Country Code");
                    }
                }
            }
        } catch(JSONException je) {
            log.error("Exception reading JSON values {} from file {}.",
                    je.toString(),
                    sjsonFile);
        } catch(Exception e) {
            log.error("Exception in getKeyValueMap method: {}",
                    e.toString());
        }
        return mapKeyValue;
    }

} /* end of ccLookupJsonLib class */
