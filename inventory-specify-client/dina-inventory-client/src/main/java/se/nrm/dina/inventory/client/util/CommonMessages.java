/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.inventory.client.util;
 
/**
 *
 * @author idali
 */
public class CommonMessages {
    
    
    // login and out messages
    public final String AUTHENTICATION_FAILED = "You are not authorized to access this client.  Authentication failed!";
    public final String RE_LOGIN_EXISTS_USER = "Attempt to re-login while the user identity already exists";
    public final String INCORRECT_USERNAME_OR_PASSWORD = "Incorrect Username or Password!";
    public final String LOGOUT_FAILED = "Failed to logout user!";
//    public final String USER_NOT_IN_DB = "Username is not in database";
    
    
    
    
    // Admin
//    public final String DUPLICATE_USER = "Duplicate username";
    
    
    // Metadata page
    public final String INVALID_METADATA_MISSING_TAXON = "Invalid excel file: metadata sheet: Missing taxon";
    public final String INVALID_METADATA_MISSING_LOAN_NUMBER = "Invalid excel file: metadata sheet: Missing loan number";
    public final String INVALID_METADATA_MISSING_DYNTAXA_DUMP_DATE = "Invalid excel file: metadata sheet: Missing dyntaxa dump date";
    public final String INVALID_METADATA_MISSING_DETERMINAER = "Invalid excel file: metadata sheet: Missing determiner";
    
    
    public final String INVALID_METADATA_MISSING_TAXON_TITLE = "Invalid excel file: metadata sheet: Missing taxon title";
    public final String INVALID_METADATA_MISSING_LOAN_NUMBER_TITLE = "Invalid excel file: metadata sheet: Missing loan number title";
    public final String INVALID_METADATA_MISSING_DYNTAXA_DUMP_DATE_TITLE = "Invalid excel file: metadata sheet: Missing dyntaxa dump date title";
    public final String INVALID_METADATA_MISSING_DETERMINAER_TITLE = "Invalid excel file: metadata sheet: Missing determiner title";
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public final String INVALID_EXCEL_FILE = "Invalid excel file";
    public final String INVALID_EXCEL_FILE_NAME = "Invalid excel filename.  File name must contains _C* or _D* to indicate it is a C file or D file.";
    public final String INVALID_C_FILE = "The filaname contails _C* to indicate this is a C file, but the Observation sheet contains Cat. No column.";
    public final String INVALID_D_FILE = "The filaname contails _C* to indicate this is a C file, but the Observation sheet missing Cat. No column.";
    public final String NUMBER_OF_PAGE_INVALID = "Invalid excel file, number of sheet is less than 5.";
    public final String FAILED_UPLOAD_EXCEL_FILE = "Failed to upload excel file";
    
    public final String INVALID_DETERMINER_NAME = "Invalid determiner name.";
    
    
    public final String INVALID_FILE_ON_ROW = "Invalid excel file: Row number ";
    public final String MEDIA_IS_EMPTY = " media is empty.";
    public final String CATALOGNUMEBR_IS_EMPTY = " catalognumber is empty.";
    public final String CATALOGNUMBER = " catalognumber: ";
    public final String DUPLICATED = " is duplicated.";
    public final String EXIST_IN_DB = " is already in database.";
    
    public final String FAILED_UPLOAD_TAXONLIST = "Failed to upload Dyntaxa list";
    public final String FAILED_UPLOAD_OBSERVATION_DATA = "Failed to upload Observation data";
    
    
    public final String EVENT_ID = "Event ID ";
    public final String MISSING_EVENTID_IN_DB = " is not valid, or not in database.  If you are sure the event id is correct, please contect Administrator to add it in database.";
    public final String MISSING_AGENT_IN_DB = " not exist in database.  Please contect Administrator to add as agent in database.";
    public final String MISSING_TAXON_IN_DB = " is not in database, please contect ";
    public final String EVENT_ID_EMPTY = "Event ID can not be empty or 0.";

    public final String MULTIPLE_TAXON_IN_DB = "There are more than one taxon: ";
    public final String MULTIPLE_TAXON_IN_DB_ERROR = " in database, please contect ";

    public final String OBSERVATION_COLUMN_NOT_MAPPING = "Unable to mapping observation sheet column. Column: ";
    public final String MISSING = " is missing.";

 
    public final String NOT_OBSERVATION_FILE = "This is not an Observation records file.";
    public final String NOT_SPECIMENS_FILE = "This is not a Specimens records file.";

    public final String MISSING_METADAT_SHEET = "Sheet 1: Metadata is missing.";
    public final String MISSING_OBSERVATION_SHEET = "Sheet 2: Observation is missing.";
    public final String MISSING_TRAP_SHEET = "Sheet 3: Trap is missing.";
    public final String MISSING_COLLECTINGEVENT_SHEET = "Sheet 4: CollectingEvent is missing.";
    public final String MISSING_TAXONLIST_SHEET = "Sheet 5: TaxonList is missing.";
    
    public final String TAXON_COMPUTED_IS_EMPTY = "Taxon name [Computed] is empty.";
    public final String TAXON_COMPUTED_IS_INCORRECT = "Taxon name [Computed] is incorrect.";
    
    public final String TAXON_ON_ROW = "Taxon on row ";
    public final String NOT_IN_DB = " is not in database.";
    public final String INVALID_TAXON_NAME_ON_ROW = "Taxon name is not valid. Row number: "; 
    public final String INVALID_TAXON_NAME = " invalid taxon name: "; 
    
     public final String TAXON_COLUMN_NOT_MAPPING = "Unable to mapping taxonlist sheet column. Column: ";
    
    public final String UNDEFINED_FILE_TYPE = "File type can not define.  Please check file name. ";
    
    public final String MISSING_SPECIES = " species is empty";
    public final String MISSING_CATALOGNUMBER = " catalog number is missing";
    public final String MISSING_COLLECTINGEVENT = ": no Collectingevent.";
    
    public final String MISMATCH_TOTAL = "The total number is not match number of males + number of females.";

    public final String LOGGED_IN_USER = "loggedInUser";
    
    public final String SUCCESS = "Success";
    
    // Test file download messages
    public final String SUCCESS_TEST_DATA_DELETE = "Test data are deleted";
    public final String FAILED_TEST_DATA_DELETE = "Web service are not available.  Test data can not be deleted";
    
    public String determinerNotInDB(String determiner) {
        StringBuilder sb = new StringBuilder();
        sb.append("Sheet 1: Determined by ");
        sb.append(determiner);
        sb.append(NOT_IN_DB);
        sb.append(" Please contact admin to add into database");
        return sb.toString();
    }
    
      
    private static CommonMessages instance = null;
    
    public static synchronized CommonMessages getInstance() {
        if (instance == null) {
            instance = new CommonMessages();
        } 
        return instance;
    } 
    
      
    
    public String buildMessage(String message, int rowNumber) {
        StringBuilder errorSb = new StringBuilder();
        errorSb.append(CommonMessages.getInstance().INVALID_FILE_ON_ROW); 
        errorSb.append(rowNumber);
        errorSb.append(". ");
        errorSb.append(message); 
        return errorSb.toString();
    }
    
    
    public String buildMessage(String msg1, String value1, String msg2, int rowNumber) {
        StringBuilder errorSb = new StringBuilder();
        errorSb.append(CommonMessages.getInstance().INVALID_FILE_ON_ROW); 
        errorSb.append(rowNumber);
        errorSb.append(". ");
        errorSb.append(msg1); 
        errorSb.append(value1);
        errorSb.append(msg2);
        return errorSb.toString();
    }
    
    
    
    
    public String buildMessage(String value, String message1, String message2) { 
        return buildMessage(value, "",  message1, message2, "");
    }
    
    public String buildMessage(int value, String message1, String message2) { 
        return buildMessage(String.valueOf(value), "",  message1, message2, "");
    }
    
    public String buildMessage(String value1, String value2, String message1, String message2, String message3) {
        StringBuilder sb = new StringBuilder();
        sb.append(message1);
        sb.append(value1);
        sb.append(message2);
        sb.append(value2);
        sb.append(message3);
        return sb.toString();
    }
    
    
    
    public String buildMessage(String value1, String value2, String value3, String message1, String message2, String message3) {
        StringBuilder sb = new StringBuilder();
        sb.append(message1);
        sb.append(value1);
        sb.append(message2);
        sb.append(value2);
        sb.append(message3);
        sb.append(value3);
        return sb.toString();
    }
    
    public String buildInvalidTaxonNotInTaxonListMessage(int rownumber, String genus, String species) {
        StringBuilder sb = new StringBuilder();
        sb.append(INVALID_FILE_ON_ROW); 
        sb.append(rownumber);
        sb.append(" taxon name: ");
        sb.append(genus);
        sb.append(" ");
        sb.append(species);
        sb.append(" is not in TaxonList sheet.");
        return sb.toString();
    }
}
