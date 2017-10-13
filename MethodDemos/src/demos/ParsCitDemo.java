package demos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.ExecUtil;

public class ParsCitDemo extends AbstractDemo
{
	private static final String METHOD_NAME = "parscit";
	private static final boolean INPUT_IN_XML_FORMAT = true; // true=xml, false=txt

	// "cmd.exe /c citeExtract.pl C:/Users/Angela/git/ParsCit/demodata/sample1.txt"
	private static final String PARSCIT_HOME = "C:/Users/Angela/git/ParsCit/bin";
	private static final List<String> command = Arrays.asList("cmd.exe", "/c", "citeExtract.pl", "-m", "extract_all");

	public static void main(String[] args) throws IOException
	{
		// List<File> groundTruthFiles = Demos.getAllGroundTruthFilesAsTxt().subList(0, 1);
		new ParsCitDemo().runDemo(Arrays.asList(new File("D:/output/GroundTruthsubset/TUW-139994.xml")), Demos.parsCitOutputDir);
	}

	@Override
	String runDemo(File inputFile, File outputFile) throws IOException
	{
		// execute cileExtract.pl command
		// String in = "C:/Users/Angela/git/ParsCit/demodata/sample1.txt";

		List<String> commandWithParameters = new ArrayList<>(command);
		if(INPUT_IN_XML_FORMAT)
		{
			commandWithParameters.add("-i");
			commandWithParameters.add("xml");
		}
		commandWithParameters.add(inputFile.toString());
		commandWithParameters.add(outputFile.toString());
		String err = ExecUtil.execInWorkingDir(new File(PARSCIT_HOME), commandWithParameters);

		return err;
	}

	@Override
	String getMethodName()
	{
		return METHOD_NAME;
	}

	@Override
	public String createOutputFileName(File inputFile)
	{
		// parscit-TUW-137078-omnipage-xstream.xml
		String outputFileName = super.createOutputFileName(inputFile);

		// remove "-omnipage" from "parscit-TUW-137078-omnipage-xstream.xml"
		outputFileName = outputFileName.replace("-omnipage", "");
		return outputFileName;
	}
}
