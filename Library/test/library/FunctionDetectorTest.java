/**
*
* @author Hajer Gafsi hajer.gafsi@ogr.sakarya.edu.tr
* @since 20/04/2023
* <p>
* 	This class is responsible for testing the FunctionDetector class
* </p>
*/

package library;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FunctionDetectorTest {

	private FunctionDetector FD;
	
	@Test
	@DisplayName("Simple test")
	void SampleTest() {
		FD = new FunctionDetector("private String foo() {jhj};String foo() {} int helloWorled() {}");
		assertEquals(FD.Analyze(),3);
	}
	
	//integration test
	@ParameterizedTest
	@DisplayName("Testing the project java files by Integrating the Document class")
	@CsvSource(value={"FunctionDetector.java:2","AmbiguousStringRemover.java:2","OperatorFinder.java:4","IAnalyzer.java:1","Document.java:13"},delimiter= ':')
	void testWithDocument(String filePath,int funcNmbr) {
		try {
			Document doc = new Document("./src/library/" + filePath);
			doc.readAndCleanString();
			doc.AnalyzeFunctions();
			assertEquals(doc.getNumberOfFunctions(),funcNmbr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("File not found");
		}
	}

}
