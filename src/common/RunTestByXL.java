package common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import jxl.read.biff.BiffException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import common.XLReader;

public class RunTestByXL
{
	//Global initialization of Variables
	static XLReader xlsUtil;
	int rowCnt;
		
	public RunTestByXL() throws BiffException, IOException {
		xlsUtil = new XLReader("ExcelSheet/TestSuites.xls");
		//Load the Excel Sheet Col in to Dictionary for Further use in our Test cases. ExcelSheet
		XLReader.columnDictionary();
	}
		
	// Fetching the data from Excel sheet 
	public void getDataFromXL() throws Exception{
		
		ArrayList<XmlSuite> suites;
		XmlSuite suite = new XmlSuite();
		XmlTest test = new XmlTest(suite);
		TestNG tng = new TestNG();

		ArrayList<XmlClass>  classes = new ArrayList<XmlClass>();
		ArrayList<String> includedGroupList = new ArrayList<String>();
		//HashSet<String> hsGroup = new HashSet<String>();
		HashSet<XmlClass> hsClass = new HashSet<XmlClass>();
			
		for(rowCnt=2; rowCnt < XLReader.rowCount(); rowCnt++)
		{
			try{
				if(XLReader.readCell(XLReader.getCell("RunMode"), rowCnt).equalsIgnoreCase("Yes")){
					// Read suite name from excel sheet
					suite.setName(XLReader.readCell(XLReader.getCell("SuiteName"), rowCnt));
					test.setName(XLReader.readCell(XLReader.getCell("TestName"), rowCnt));
							
					String packageName = XLReader.readCell(XLReader.getCell("PackageName"), rowCnt);
					String className = XLReader.readCell(XLReader.getCell("ClassName"), rowCnt);
					String groupName = XLReader.readCell(XLReader.getCell("GroupName"), rowCnt);
							
					String classNameWithPackage=packageName+"."+className;
					classes.add(new XmlClass(classNameWithPackage));
							
					includedGroupList.add(groupName);
				}
			}
			catch(Throwable e)
			{
				e.printStackTrace();
				Assert.fail("Exception Occured : "+e.getMessage());
				Reporter.log("Exception Occured : "+e.getMessage());
			}
		}
				
		hsClass.addAll(classes);
		classes.clear();
		classes.addAll(hsClass);
				
		test.setIncludedGroups(includedGroupList);
		test.setXmlClasses(classes) ;
		suites = new ArrayList<XmlSuite>();
		suites.add(suite);
				
		tng.setXmlSuites(suites);
		tng.run(); 
	}
		
	public static void main(String []a) throws Exception{
		RunTestByXL runtest = new RunTestByXL();
		runtest.getDataFromXL();
	}
}
