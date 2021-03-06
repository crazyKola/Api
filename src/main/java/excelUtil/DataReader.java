package excelUtil;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class that read data from XSSF sheet
 * Created by Garyson on 2019/7/18.
 */

public class DataReader {

    protected static final Logger logger = LoggerFactory.getLogger(DataReader.class);
    private HashMap<String, RecordHandler> map = new HashMap<>();

    private Boolean byColumnName = false;
    private Boolean byRowKey = false;
    private List<String> headers = new ArrayList<>();

    private Integer size = 0;

    /**
     * Primary constructor. Uses Apache POI XSSF to pull data from given excel workbook sheet. Data is stored in a
     * structure depending on the options from other parameters.
     *
     * @param sheet          Given excel sheet.
     * @param has_headers    Boolean used to specify if the data has a header or not. The headers will be used as field keys.
     * @param has_key_column Boolean used to specify if the data has a column that should be used for record keys.
     * @param key_column     Integer used to specify the key column for record keys.
     */
    public DataReader(XSSFSheet sheet, Boolean has_headers, Boolean has_key_column, Integer key_column) {

        XSSFRow myRow = null;
        HashMap<String, String> myList;
        size = 0;

        this.byColumnName = has_headers;
        this.byRowKey = has_key_column;

        try {
            //
            if (byColumnName) {
                myRow = sheet.getRow(0);
                for (Cell cell : myRow) {
                    headers.add(cell.getStringCellValue());
                }
                size = 1;
            }

            for (; (myRow = sheet.getRow(size)) != null; size++) {

                myList = new HashMap<>();

                if (byColumnName) {
                    for (int col = 0; col < headers.size(); col++) {
                        if (col < myRow.getLastCellNum()) {
                            myList.put(headers.get(col), getSheetCellValue(myRow.getCell(col))); // myRow.getCell(col).getStringCellValue());
                        } else {
                            myList.put(headers.get(col), "");
                        }
                    }
                } else {
                    for (int col = 0; col < myRow.getLastCellNum(); col++) {
                        myList.put(Integer.toString(col), getSheetCellValue(myRow.getCell(col)));
                    }
                }

                if (byRowKey) {
                    if (myList.size() == 2 && key_column == 0) {
                        map.put(getSheetCellValue(myRow.getCell(key_column)), new RecordHandler(myList.get(1)));
                    } else if (myList.size() == 2 && key_column == 1) {
                        map.put(getSheetCellValue(myRow.getCell(key_column)), new RecordHandler(myList.get(0)));
                    } else {
                        map.put(getSheetCellValue(myRow.getCell(key_column)), new RecordHandler(myList));
                    }
                } else {
                    map.put(Integer.toString(size), new RecordHandler(myList));
                }

                map.put(Integer.toString(size), new RecordHandler(myList));
            }

            // 合并单元格后，除了第一条，后面合并的条数单元格为空，为单元格添加参数
            if (sheet.getSheetName().equals("Input")){
                for (int i = 1; i <= map.size(); i++) {
                    if (map.get(Integer.toString(i)).get("URL") == null || map.get(Integer.toString(i)).get("URL").length()==0) {
                        map.get(Integer.toString(i)).set("URL", map.get(Integer.toString(i - 1)).get("URL"));
                        map.get(Integer.toString(i)).set("Method", map.get(Integer.toString(i - 1)).get("Method"));
                        map.get(Integer.toString(i)).set("Module", map.get(Integer.toString(i - 1)).get("Module"));
                        map.get(Integer.toString(i)).set("Sub_module", map.get(Integer.toString(i)).get("Module")+"_"+map.get(Integer.toString(i)).get("Sub_module"));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception while loading data from Excel sheet:" + e.getMessage());
        }
    }

    /**
     * Utility method used for getting an excel cell value. Cell's type is switched to String before accessing.
     * @param cell Given excel cell.
     */
    private String getSheetCellValue(XSSFCell cell) {

        String value = "";

        try {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            value = cell.getStringCellValue();
        } catch (NullPointerException npe) {
            return "";
        }

        return value;
    }

    /**
     * Returns entire HashMap containing this class's data.
     *
     * @return HashMap<String, RecordHandler>, map of ID-Record data.
     */
    public HashMap<String, RecordHandler> get_map() {

        return map;
    }


    /**
     * Gets an entire record.
     *
     * @param record String key value for record to be returned.
     * @return HashMap of key-value pairs representing the specified record.
     */
    public RecordHandler get_record(String record) {

        RecordHandler result = new RecordHandler();

        if (map.containsKey(record)) {
            result = map.get(record);
        }

        return result;
    }
}
