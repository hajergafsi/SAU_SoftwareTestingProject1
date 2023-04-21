package library;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({AmbiguousStringRemoverTest.class,OperandAnalyzerTest.class,DocumentTest.class,OperatorFinderTest.class})
@IncludeTags("RepeatedTest")
public class RepeatedTests {

}
