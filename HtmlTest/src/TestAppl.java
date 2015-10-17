import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import tel_ran.autotesting.model.*;

public class TestAppl {
	
	private static final String FILE_MATCHING = "matching.txt";
	private static final String FILE_SOLUTION = "solution.html";
	private static final String FILE_TASK = "task.html";

	public static void main(String[] args) throws Exception {
		Task task;// = new TagNesting();
		task = new ListsTesting();
		System.out.println(task.getCondition());
		String taskData = task.getData();
		System.out.println("-----------------------------------");
		System.out.println(taskData);

		saveToFile(taskData, FILE_TASK);
//		saveToFile(task.getMatching(), FILE_MATCHING);

		String matching = readFromFile(FILE_MATCHING);
//		String solution = readFromFile(FILE_TASK);
		String solution = readFromFile(FILE_SOLUTION);

		System.out.println(task.checkSolution(solution,matching));

	}
	
	private static String readFromFile(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		StringBuffer sb = new StringBuffer();
		while(br.ready()) {
			sb.append(br.readLine());
		}
		br.close();
		return sb.toString();
	}
	
	private static void saveToFile(String strData, String fileName) throws IOException {
		PrintWriter pw = new PrintWriter(fileName);
		pw.print(strData);
		pw.close();
		
	}
}

