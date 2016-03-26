package lv.kid.vermut.intellij.yaml.parser;

import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.PlatformTestCase;
import org.junit.Assert;

public class ParserTest extends ParsingTestCase {

    public ParserTest() {
        super("", "yml", new NeonParserDefinition());
        PlatformTestCase.initPlatformLangPrefix();
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/data/parser";
    }

    protected void doTest(boolean checkResult, boolean suppressErrors) {
        doTest(true);
        if (!suppressErrors) {
            Assert.assertFalse(
                    "PsiFile contains error elements",
                    toParseTreeText(myFile, true, includeRanges()).contains("PsiErrorElement")
            );
        }
    }

    public void test01() {
        doTest(true, false);
    }

    public void test02() {
        doTest(true, false);
    }

    public void test03() {
        doTest(true, false);
    }

    public void test04() {
        doTest(true, false);
    }

    public void test05() {
        doTest(true, false);
    }

    public void test06() {
        doTest(true, false);
    }

    public void test07() {
        doTest(true, false);
    }

    public void test08() {
        doTest(true, false);
    }

    public void test09() {
        doTest(true, false);
    }

    public void test10() {
        doTest(true, false);
    }

    public void test11() {
        doTest(true, true);
    }

    public void test12() {
        doTest(true, true);
    }

    public void test13() {
        doTest(true, false);
    }

    public void test14() {
        doTest(true, true);
    }

    public void test15() {
        doTest(true, true);
    }

    public void test16() {
        doTest(true, true);
    }

    public void test17() {
        doTest(true, false);
    }

    public void test18() {
        doTest(true, true);
    }

    public void testIssue31() {
        doTest(true, false);
    }

    public void testIssue31_simple() {
        doTest(true, false);
    }

    public void testIssue33() {
        doTest(true, false);
    }

    public void testIssue40() {
        doTest(true, false);
    }

    public void testIssue42() {
        doTest(true, false);
    }

}
