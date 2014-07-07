package common;

import java.io.File;

import jxl.Sheet;
import jxl.Workbook;

public class DataProvider_getTableArray {

	public static String[][] getTableArray(String xlFilePath, String sheetName){
        String[][] tabArray=null;
        try{
	        Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
	        Sheet sheet = workbook.getSheet(sheetName);
	        int startRow = 0, startCol = 0, endRow = 0, endCol = 0, ci, cj;
	        //Cell tableStart=sheet.findCell(tableName);
	        //startRow=tableStart.getRow();
	        startRow=1;
	        //startCol=tableStart.getColumn();
	        startCol=1;
	        //Cell tableEnd= sheet.findCell(tableName, startCol+1,startRow+1, 100, 64000,  false);
	        //Cell tableEnd= sheet.findCell(tableName);
	        endRow=37;
	        endCol=7;
	        System.out.println("startRow="+startRow+", endRow="+endRow+", " + "startCol="+startCol+", endCol="+endCol);
	        tabArray=new String[endRow-startRow-1][endCol-startCol-1];
	        ci=0;
	        for (int i=startRow+1;i<endRow;i++,ci++){
	        	cj=0;
                for (int j=startCol+1;j<endCol;j++,cj++){
                	tabArray[ci][cj]=sheet.getCell(j,i).getContents();
                }
	        }
        }
        catch (Exception e)    {
        	e.printStackTrace();
        	System.out.println("error in getTableArray()");
        }
        return(tabArray);
	}
}
