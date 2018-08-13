package demos;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.grobid.core.engines.Engine;
import org.grobid.core.engines.config.GrobidAnalysisConfig;
import org.grobid.core.factory.GrobidFactory;
import org.grobid.core.main.GrobidHomeFinder;
import org.grobid.core.utilities.GrobidProperties;

import config.Config;
import method.Method;

/**
 * http://grobid.readthedocs.io/en/latest/Grobid-java-library/
 *
 */
public class GrobidDemo extends AbstractDemo
{
	private static final Method METHOD = Method.GROBID;

	private Engine engine;
	private GrobidAnalysisConfig config;

	private boolean consolidate = false;
	private boolean consolidateCitations = consolidate;
	private boolean consolidateHeader = consolidate;

	public GrobidDemo()
	{
		// in old version
		// MockContext.setInitialContext(Config.pGrobidHome, Config.pGrobidProperties);
		// GrobidProperties.getInstance();
		GrobidHomeFinder grobidHomeFinder = new GrobidHomeFinder(Arrays.asList(Config.pGrobidHome));
		GrobidProperties.getInstance(grobidHomeFinder);

		engine = GrobidFactory.getInstance().createEngine();
		config = GrobidAnalysisConfig.builder().consolidateHeader(consolidateHeader).consolidateCitations(consolidateCitations).build();
	}

	public static void main(String[] args) throws IOException
	{
		List<File> groundTruthFiles = new ArrayList<>();
		// groundTruthFiles.add(Demos.getAllGroundTruthFiles().subList(0, 1));
		groundTruthFiles.add(new File("D:\\output\\methods\\GroundTruthNoSubDir\\TUW-247743.pdf"));
		// groundTruthFiles.add(Demos.getAllGroundTruthFiles().subList(0, 1));
		// groundTruthFiles.add(new File("D:\\output\\methods\\GroundTruthNoSubDir\\TUW-200745.pdf"));
		// groundTruthFiles.add(Demos.getAllGroundTruthFiles().subList(0, 1));
		// groundTruthFiles.add(new File("D:\\output\\methods\\GroundTruthNoSubDir\\TUW-200948.pdf"));

		new GrobidDemo().runDemoList(groundTruthFiles, Demos.grobIdOutputDir);
		// new GROBIDDemo().runDemoInBatch("D:/TU/Masterarbeit/Papers/Methoden/", Demos.grobIdOutputDir.getPath());
		// new GROBIDDemo().runDemoInBatch("D:/output/GroundTruth-subset", grobIdOutputDir.getPath());
	}

	public void runDemoInBatch(String inputDir, String outputDir)
	{
		try
		{
			int tei = engine.batchProcessFulltext(inputDir, outputDir, consolidateHeader, consolidateCitations);
			System.out.println(tei);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * https://github.com/kermitt2/grobid-example
	 */
	@Override
	String runDemoSingleFile(File inputFile, File outputFile) throws IOException
	{
		try
		{
			String resultString = engine.fullTextToTEI(inputFile, config);
			FileUtils.writeStringToFile(outputFile, resultString, StandardCharsets.UTF_8);

			System.out.println(inputFile);

			return null;
		}
		catch(Exception e)
		{
			return e.getMessage();
		}
	}

	@Override
	protected Method getMethod()
	{
		return METHOD;
	}
}
