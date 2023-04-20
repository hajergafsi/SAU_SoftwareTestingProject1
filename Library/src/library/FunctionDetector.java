/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class is responsible for counting the number of functions present inside the java file
* </p>
*/

package library;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionDetector implements IAnalyzer {

	final String regex = "(public|protected|private|static)?\\s*[\\w\\<\\>\\[\\]]+\\s+(\\w+)*\\s*\\([^\\)]*\\)*\\s*(?:\\s*throws\\s*\\w[\\w.]+(\\s*,*\\s*[\\w.]+))?\\s*((\\{[^\\}]*)|;)";
	final String specialCasesRegex = "(while|for|if|switch|catch|else if)\\s+\\(.*\\)\\s*(\\{[^\\}]*|;)";
	private String code;
	private int count;
	Matcher matcher;
	Pattern pattern,specialCasePattern;
	
	public FunctionDetector(String code) {
		this.code = code;
		this.count = 0;
		pattern = Pattern.compile(regex,Pattern.MULTILINE);
		specialCasePattern = Pattern.compile(specialCasesRegex,Pattern.MULTILINE);
	}
	
	
	@Override
	public int Analyze() {
		Matcher specialCase;
		String temp = code;
		 specialCase = specialCasePattern.matcher(code);
		 while (specialCase.find()) {
			 temp = temp.replace(specialCase.group(),"special case");
		 }	
		 matcher = pattern.matcher(temp);
		 while (matcher.find()) {
			 count++;
		 }
		return count;
	}

}
