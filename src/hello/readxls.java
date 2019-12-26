package hello;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import util.*;
import util.domestic_enterprise;

public class readxls {
	public static List<Management> readXlsManagement() {
		try {
			InputStream is = new FileInputStream(new File("d:\\1.xls").getAbsolutePath());
			Workbook wb = Workbook.getWorkbook(is);
			Sheet sheet = wb.getSheet(0);
			List<Management> list = new ArrayList<>();
			for (int i=2;i<sheet.getRows();i++) {
				Management m = new Management(sheet.getCell(0, i).getContents(),
						sheet.getCell(1, i).getContents(),
						sheet.getCell(2, i).getContents(),
						sheet.getCell(3, i).getContents(),
						sheet.getCell(4, i).getContents(),
						sheet.getCell(5, i).getContents());
				list.add(m);
			}
			return list;
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	public static List<domestic_enterprise> readXlsDomestic(){
		try {
			InputStream is = new FileInputStream(new File("d:\\2.xls").getAbsolutePath());
			Workbook wb = Workbook.getWorkbook(is);
			Sheet sheet = wb.getSheet(0);
			List<domestic_enterprise> list = new ArrayList<>();
			for (int i=2;i<sheet.getRows();i++) {
				domestic_enterprise de = new domestic_enterprise(sheet.getCell(0, i).getContents(),
						sheet.getCell(1, i).getContents(),
						sheet.getCell(4, i).getContents(),
						sheet.getCell(6, i).getContents(),
						sheet.getCell(7, i).getContents(),
						sheet.getCell(8, i).getContents(),
						sheet.getCell(9, i).getContents(),
						sheet.getCell(10, i).getContents(),
						sheet.getCell(11, i).getContents(),
						sheet.getCell(12, i).getContents(),
						sheet.getCell(13, i).getContents(),
						sheet.getCell(15, i).getContents(),
						sheet.getCell(16, i).getContents(),
						sheet.getCell(17, i).getContents(),
						sheet.getCell(18, i).getContents(),
						sheet.getCell(19, i).getContents(),
						sheet.getCell(20, i).getContents(),
						sheet.getCell(21, i).getContents(),
						sheet.getCell(22, i).getContents(),
						sheet.getCell(23, i).getContents(),
						sheet.getCell(24, i).getContents(),
						sheet.getCell(26, i).getContents(),
						sheet.getCell(27, i).getContents(),
						sheet.getCell(28, i).getContents(),
						sheet.getCell(29, i).getContents(),
						sheet.getCell(31, i).getContents(),
						sheet.getCell(32, i).getContents(),
						sheet.getCell(33, i).getContents(),
						sheet.getCell(34, i).getContents(),
						sheet.getCell(36, i).getContents(),
						sheet.getCell(37, i).getContents(),
						sheet.getCell(38, i).getContents(),
						sheet.getCell(39, i).getContents(),
						sheet.getCell(40, i).getContents(),
						sheet.getCell(41, i).getContents(),
						sheet.getCell(42, i).getContents(),
						sheet.getCell(43, i).getContents(),
						sheet.getCell(44, i).getContents(),
						sheet.getCell(45, i).getContents(),
						sheet.getCell(46, i).getContents());
				list.add(de);
			}
			return list;
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	public static List<bank> readXlsBank(){
		try {
			InputStream is = new FileInputStream(new File("d:\\3.xls").getAbsolutePath());
			Workbook wb = Workbook.getWorkbook(is);
			Sheet sheet = wb.getSheet(0);
			List<bank> list = new ArrayList<>();
			for (int i=2;i<sheet.getRows();i++) {
				bank m = new bank(sheet.getCell(0, i).getContents(),
						sheet.getCell(1, i).getContents());
				list.add(m);
			}
			return list;
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	public static List<oversea_enterprise> readXlsOversea(){
		try {
			InputStream is = new FileInputStream(new File("d:\\3.xls").getAbsolutePath());
			Workbook wb = Workbook.getWorkbook(is);
			Sheet sheet = wb.getSheet(0);
			List<oversea_enterprise> list = new ArrayList<>();
			for (int i=2;i<sheet.getRows();i++) {
				oversea_enterprise m = new oversea_enterprise(sheet.getCell(0, i).getContents(),
						sheet.getCell(1, i).getContents(),
						sheet.getCell(2, i).getContents(),
						sheet.getCell(3, i).getContents());
				list.add(m);
			}
			return list;
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	public static List<collection> readXlsCollection(){
		File file  = new File("d:\\collection");
		File[] lists = file.listFiles();
		List<collection> list = new ArrayList<>();
		for (File f : lists) {
			try {
				InputStream is = new FileInputStream(f.getAbsolutePath());
				Workbook wb = Workbook.getWorkbook(is);
				Sheet sheet = wb.getSheet(0);
				
				for (int i=2;i<sheet.getRows();i++) {
					collection m = new collection(sheet.getCell(5, i).getContents(),
							sheet.getCell(25, i).getContents(),
							sheet.getCell(6, i).getContents());
					list.add(m);
				}
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
		return list;
	}
	public static List<payment> readXlsPayment(){
		File file  = new File("d:\\payment");
		File[] lists = file.listFiles();
		List<payment> list = new ArrayList<>();
		for (File f : lists) {
			try {
				InputStream is = new FileInputStream(f.getAbsolutePath());
				Workbook wb = Workbook.getWorkbook(is);
				Sheet sheet = wb.getSheet(0);
				
				for (int i=2;i<sheet.getRows();i++) {
					payment m = new payment(sheet.getCell(5, i).getContents(),
							sheet.getCell(26, i).getContents(),
							sheet.getCell(6, i).getContents());
					list.add(m);
				}
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
		return list;
	}
	public static List<fdi> readXlsFdi(){
		try {
			InputStream is = new FileInputStream(new File("d:\\5.xls").getAbsolutePath());
			Workbook wb = Workbook.getWorkbook(is);
			Sheet sheet = wb.getSheet(0);
			List<fdi> list = new ArrayList<>();
			for (int i=2;i<sheet.getRows();i++) {
				fdi m = new fdi(sheet.getCell(11, i).getContents(),
						sheet.getCell(1, i).getContents());
				list.add(m);
			}
			return list;
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	public static List<odi> readXlsOdi(){
		try {
			InputStream is = new FileInputStream(new File("d:\\6.xls").getAbsolutePath());
			Workbook wb = Workbook.getWorkbook(is);
			Sheet sheet = wb.getSheet(0);
			List<odi> list = new ArrayList<>();
			for (int i=2;i<sheet.getRows();i++) {
				odi m = new odi(sheet.getCell(2, i).getContents(),
						sheet.getCell(3, i).getContents());
				list.add(m);
			}
			return list;
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
}
