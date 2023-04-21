/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class is the main class that does the file processing and where counts are saved
* </p>
*/


package library;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.io.FileWriter;
import java.io.*;

public class Document {
	private final File path;
	final String fileRegex = "^.*\\.java$";
	final Pattern pattern = Pattern.compile(fileRegex);
	Matcher matcher;
	
	ExpressionFinder EF ;
	CommentFinder CF;
	AmbiguousStringRemover ASR;
	FunctionDetector FD;
	OperandAnalyzer OA;
	
	private int numberOfNumericOperators;
	private int numberOfLogicalOperators;
	private int numberOfRelationalOperators;
	private int numberOfDoubleOperators;
	private int numberOfSingleOperators;
	private int numberOfFunctions;
	private int numberOfOperands;
	
	private String code;
	
	public Document(String documentPath) throws Exception {
		path = new File(documentPath);
		matcher = pattern.matcher(documentPath);
		if(!matcher.find()) {
			throw new Exception("File must be a java file !"); 
		}
	}
	
	
	public String read() throws IOException {
		try {
			return Files.readString(Path.of(path.getAbsolutePath()));
		}catch (IOException ex) {
			  IOException exception = new IOException ("File doesn't exist!");
			  exception.setStackTrace(ex.getStackTrace());
			  throw exception;
		}
	}
	
	public void readAndCleanString() throws IOException {
		CF = new CommentFinder(read());
		code = CF.removeComment();
		ASR = new AmbiguousStringRemover(code);
		code = ASR.replaceAmbiguous();
	}
	
	public void AnalyzeOperators() {
		EF = new ExpressionFinder(code);
		this.numberOfNumericOperators = EF.Analyze(EOperator.numerical);
		this.numberOfLogicalOperators = EF.Analyze(EOperator.logical);
		this.numberOfRelationalOperators = EF.Analyze(EOperator.relational);
		this.numberOfDoubleOperators = EF.Analyze(EOperator.doubleOp);
		this.numberOfSingleOperators = EF.Analyze(EOperator.single);
	}
	
	public void AnalyzeOperands() {
		OA = new OperandAnalyzer(code);
		this.numberOfOperands = OA.Analyze();
	}
	
	public void AnalyzeFunctions() {
		FD = new FunctionDetector(code);
		this.numberOfFunctions = FD.Analyze();
	}
	
	public int getNumberOfNumericOperators() {	
		return this.numberOfNumericOperators;
	}
	
	public int getNumberOfLogicalOperators() {
		return this.numberOfLogicalOperators;
	}
	
	public int getNumberOfRelationalOperators() {
		return this.numberOfRelationalOperators;
	}
	public int getNumberOfDoubleOperators() {
		return this.numberOfDoubleOperators;
	}
	
	public int getNumberOfSingleOperators() {
		return this.numberOfSingleOperators;
	}
	
	public int getNumberOfFunctions() {
		return this.numberOfFunctions;
	}
	public int getNumberOfOperands() {
		return this.numberOfOperands;
	}
}


