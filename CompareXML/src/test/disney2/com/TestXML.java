package test.disney2.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestXML {

	public static void main(String[] args) throws IOException {
		String configFileLocation1 = "/Users/ali/Downloads/XMLCompareFile1.xml";
		String configFileLocation2 = "/Users/ali/Downloads/XMLCompareFile2.xml";
		String resultExlpath = "/Users/ali/Downloads/result.xlsx";
		ArrayList<String> file1 = readDataFromXMLFile(configFileLocation1);
		ArrayList<String> file2 = readDataFromXMLFile(configFileLocation2);
		ArrayList<String> result = findDiff(file1, file2);
		writeInExcel(resultExlpath, result);
	}

	public static ArrayList<String> readDataFromXMLFile(String configFileLocation) throws IOException {
		File xmlFile = new File(configFileLocation);
		Reader fileReader = new FileReader(xmlFile);
		BufferedReader bufReader = new BufferedReader(fileReader);
		ArrayList<String> str = new ArrayList<String>();
		String line = bufReader.readLine();
		while (line != null) {
			str.add(line);
			line = bufReader.readLine();
		}
		bufReader.close();
		return str;
	}

	public static ArrayList<String> findDiff(ArrayList<String> str1, ArrayList<String> str2) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < str1.size(); i++) {
			if (!str1.get(i).equals(str2.get(i))) {
				subString(str1.get(i));
				result.add("Mismatch at line " + (i + 1) + "********");
				result.add(subString(str1.get(i)));
				result.add(subString(str2.get(i)));
			}
		}
		return result;
	}

	public static void writeInExcel(String resultXMlpath, ArrayList<String> result) throws IOException {
		FileInputStream inputStream = new FileInputStream(new File(resultXMlpath));
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet firstSheet = workbook.getSheetAt(0);
		for (int i = 0; i < result.size(); i++) {
			if (firstSheet.getRow(i) == null) {
				firstSheet.createRow(i);
			}
			Row newRow = firstSheet.getRow(i);
			// check for create cell
			newRow.createCell(0).setCellValue(result.get(i));
		}

		FileOutputStream fileOutputStream = new FileOutputStream(resultXMlpath);
		workbook.write(fileOutputStream);

	}

	public static int findStartLocation(String text) {
		int start = 0;
		if (text.indexOf('/') == -1) {
			start = 0;
		} else {
			start = text.indexOf('>');
		}
		return start;

	}

	public static String subString(String str) {
		String text = str.trim();
		int start = findStartLocation(text) + 1;
		String sub = text.substring(start);
		int end = 0;
		if (sub.indexOf('<') == -1) {
			end = sub.indexOf('>');
		} else {
			end = sub.indexOf('<');
		}
		return (sub.substring(0, end));

	}
}