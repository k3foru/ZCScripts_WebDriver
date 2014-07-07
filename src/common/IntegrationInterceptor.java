package common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.xml.Parser;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.xml.sax.SAXException;

/**
 * @author Abhishek Singh - http://knorrium.info
 *
 * This class implements the IMethodInterceptor interface to execute the tests 
 * in the exact same order as defined in the TestNG XML file.
 * 
 * In order to use it, you will have to include the methods manually.
 * 
 * Below is a sample XML which illustrates how to run two tests, one with methods
 * from the same class and another one with methods from multiple classes, suitable
 * for end-to-end tests.
 */
public class IntegrationInterceptor implements IMethodInterceptor {
	public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
		List<String> methodsToRun = new ArrayList<String>();
		try {
			Collection<XmlSuite> suites = new Parser(context.getSuite().getXmlSuite().getFileName()).parse();
			for (XmlSuite xmlSuite : suites)
			{
				for (XmlTest xmlTest: xmlSuite.getTests())
				{
					if (xmlTest.getName().equals(context.getCurrentXmlTest().getName()))
					{
						for (XmlClass xmlClass : xmlTest.getXmlClasses())
						{
							for (XmlInclude xmlMethod : xmlClass.getIncludedMethods())
							{
								String searchMethod = xmlClass.getName() + "." + xmlMethod.getName() + "(";
								try {
									Class c = Class.forName(xmlClass.getName());
									Method m[] = c.getDeclaredMethods();
									for (int i = 0; i < m.length; i++) {
										if (m[i].toString().contains(searchMethod)){
											methodsToRun.add(xmlMethod.getName());
										}
									}
								}
								catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		List<IMethodInstance> result = new ArrayList<IMethodInstance>(methodsToRun.size());
		for (int i = 0; i < methodsToRun.size(); i++)
		{
			result.add(methods.get(0));
		}
		for (IMethodInstance m : methods) {
			String methodName = m.getMethod().getMethod().getName();
			if (methodsToRun.indexOf(methodName) >= 0)
			{
				int i = 0;
				for (String method : methodsToRun)
				{
					if (method.equals(methodName))
					{
						result.set(i, m);							
					}
					i++;
				}
			}
		}
		return result;
	}
}