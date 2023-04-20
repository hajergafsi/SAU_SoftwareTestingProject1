/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class is responsible for counting the operands inside the java code
* </p>
*/

package library;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import library.OperatorFinder.Match;

public class OperandAnalyzer implements IAnalyzer {
	private String code;
	Pattern expressionPattern,operandPattern,incPattern;
	Matcher expressionMatcher,operandMatcher;
	private int count;
	ExpressionFinder EF;
	final String expressionRegex = "\\s*([\\(]*((\\s*(--|\\+\\+)\\s*)?_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(([^\\)\\\"]|\\\".*\\\")*\\))*(\\s*(--|\\+\\+))?|\\\"[^\\\"]*\\\")\\s*)+((\\s|\\\\n|\\\\r)*(\\+|-|\\*|\\/|%|&|\\||\\^|=|\\+=|-=|\\/=|\\*=|%=|&=|\\|=|\\^=|&&|\\|\\||!|<|<=|>|>=|==|!=)(\\s|\\\\n|\\\\r)*([\\(]*((\\s*(--|\\+\\+)\\s*)?_*([A-Za-z0-9]|__)+(\\.\\w|\\w|,|\\(([^\\)\\\"]|\\\".*\\\")*\\))*(\\s*(--|\\+\\+))?|\\\"[^\\\"]*\\\")(\\s|\\\\n|\\\\r)*\\)*)+(\\s|\\\\n|\\\\r)*\\)*)+(;|\\{)";
	final String operandRegex = "(_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(([^\\)\\\"]|\\\"[^\\\"]*\\\")*\\))*|\\\"[^\\\"]*\\\")";
	final String incrementationCaseRegex = "(\\s*(--|\\+\\+)\\s*)[\\(]*\\s*_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(([^\\)\\\"]|\\\".*\\\")*\\))*|[\\(]*_*([A-Za-z0-9]|__)+(\\.\\w|\\w|\\(([^\\)\\\"]|\\\".*\\\")*\\))*(\\s*(--|\\+\\+))\\s*";
	
	public OperandAnalyzer(String code){
		this.code = code;
		this.count = 0;
		expressionPattern = Pattern.compile(expressionRegex,Pattern.MULTILINE); 
		operandPattern = Pattern.compile(operandRegex,Pattern.MULTILINE);
		incPattern = Pattern.compile(incrementationCaseRegex,Pattern.MULTILINE);
	}
	
	public void detectIncrementingOperators() {
		Pattern incrementPattern = Pattern.compile(incrementationCaseRegex);
		Matcher matcher = incrementPattern.matcher(this.code);
        while (matcher.find()) {
        	Match match = new Match();       
	        match.start = matcher.start();
	        match.text = matcher.group();
	        match.end = matcher.end();
	        //this.code = code.substring(0, match.start-1) + "operator" + code.substring(matcher.end()-1);
	        this.code = code.replace(match.text,"operator");
	        count++;         	
            //matcher.group(3) gives you the operator
        }
	}
	
	public int AnalyzeOperators(String text) {
		EF = new ExpressionFinder(text);
		return(EF.Analyze(EOperator.numerical) + EF.Analyze(EOperator.logical) + EF.Analyze(EOperator.relational));
	}
	
	@Override
	public int Analyze() {
		detectIncrementingOperators();
		expressionMatcher = expressionPattern.matcher(code);
        while (expressionMatcher.find()) {
        	int totalCount = AnalyzeOperators(expressionMatcher.group());
        	count += totalCount+1;
        }	
		return count;
	}

}
