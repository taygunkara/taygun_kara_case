package com.insider.tests.dataproviders;

import com.insider.utils.JsonDataReader;
import org.testng.annotations.DataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class QAJobsDataProvider {

    private static final Logger logger = LoggerFactory.getLogger(QAJobsDataProvider.class);
    private static final String DATA_FILE_PATH = "src/test/resources/qaJobsData.json";

    @DataProvider(name = "qaJobsData")
    public static Object[][] getQaJobsData() {
        logger.info("Fetching QA jobs data from JSON to provide to test method.");
        List<Map<String, String>> data = JsonDataReader.readJsonData(DATA_FILE_PATH);

        Object[][] result = new Object[data.size()][2];
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i).get("location");
            result[i][1] = data.get(i).get("department");
            logger.debug("Prepared data row {}: location='{}', department='{}'", i, result[i][0], result[i][1]);
        }

        logger.info("DataProvider prepared {} rows for QA jobs test.", result.length);
        return result;
    }
}
