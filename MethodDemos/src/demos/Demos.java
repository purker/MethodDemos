package demos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;

import config.Config;
import mapping.cermine.CermineMapper;
import mapping.grobid.GROBIDMapper;
import mapping.parscit.ParsCitMapper;
import mapping.pdfx.PDFXMapper;

/**
 * Runs all Demos with Ground Truth files from inputDir
 *
 */
public class Demos
{
	private static final boolean USE_SPECIFIC_OUTPUTDIR = true;

	public static File inputDir = Config.groundTruth;

	// public static File inputDir = new File("D:/output/nosubdir");
	// public static File inputDirTxt = new File("D:/output/methods/Cermine");

	public static File cermineOutputDir = Config.cermineOutputDir;
	public static File grobIdOutputDir = Config.grobidOutputDir;
	public static File parsCitOutputDir = Config.parsCitOutputDir;
	public static File pdfxOutputDir = Config.pdfxOutputDir;
	public static File[] outputDirs = {cermineOutputDir, grobIdOutputDir, parsCitOutputDir, pdfxOutputDir};
	public static File loggingDir = new File("D:/output/methods");

	static File allOutputDir = new File("D:/output/methods/all");

	public static void main(String[] args) throws Exception
	{
		executeDemos();
	}

	public static void executeDemos() throws IOException, JAXBException
	{
		List<String> idList = Config.groundTruthIds;
		// List<String> idList = Arrays.asList("200745", "200948", "225252", "201821", "247743");

		boolean runDemos = false;
		boolean runCermineDemo = false;
		boolean runGrobidDemo = false;
		boolean runParsCitDemo = false;
		boolean runPdfxDemo = false;
		boolean runCermineMapper = true;
		boolean runGrobidMapper = true;
		boolean runParsCitMapper = true;
		boolean runPdfxMapper = true;
		// List<File> groundTruthFiles = getFileById("226016");
		List<File> groundTruthFiles = getAllGroundTruthFilesByIds(idList);
		List<File> groundTruthFilesOmnipage = getAllGroundTruthFilesAsOmnipage(idList);

		// set directories and clean
		if(!USE_SPECIFIC_OUTPUTDIR)
		{
			if(runDemos)
			{
				cleanOrCreateDirectory(allOutputDir);
			}
			deleteResultAndErrorFiles(allOutputDir);

			cermineOutputDir = allOutputDir;
			grobIdOutputDir = allOutputDir;
			parsCitOutputDir = allOutputDir;
			pdfxOutputDir = allOutputDir;
		}
		else
		{
			if(runDemos)
			{
				if(runCermineDemo) cleanOrCreateDirectory(cermineOutputDir);
				if(runGrobidDemo) cleanOrCreateDirectory(grobIdOutputDir);
				// if(runParsCitDemo) cleanOrCreateDirectory(parsCitOutputDir);
				// if(runPdfxDemo) cleanOrCreateDirectory(pdfxOutputDir);
			}
			if(runCermineMapper) deleteResultAndErrorFiles(cermineOutputDir);
			if(runGrobidMapper) deleteResultAndErrorFiles(grobIdOutputDir);
			// if(runParsCitMapper) deleteResultAndErrorFiles(parsCitOutputDir);
			if(runPdfxMapper) deleteResultAndErrorFiles(pdfxOutputDir);
		}
		if(runDemos)
		{
			if(runCermineDemo) new CermineDemo().runDemoList(groundTruthFiles, cermineOutputDir);
			if(runGrobidDemo) new GrobidDemo().runDemoList(groundTruthFiles, grobIdOutputDir);
			if(runParsCitDemo) new ParscitDemo().runDemoList(groundTruthFilesOmnipage, parsCitOutputDir);
			if(runPdfxDemo) new PdfxDemo().runDemoList(groundTruthFiles, pdfxOutputDir);
		}
		// files=(cermineOutputDir, idList);
		// files=(grobIdOutputDir, idList);
		// files=(parsCitOutputDir, idList);
		// files=(pdfxOutputDir, idList);

		if(runCermineMapper) new CermineMapper().unmarshallFiles();
		if(runGrobidMapper) new GROBIDMapper().unmarshallFiles();
		if(runParsCitMapper) new ParsCitMapper().unmarshallFiles();
		if(runPdfxMapper) new PDFXMapper().unmarshallFiles();

	}

	private static List<File> getAllGroundTruthFilesAsOmnipage(List<String> idList)
	{
		List<File> list = new ArrayList<>();

		for(String string : idList)
		{
			String fileName = "TUW-" + string + "-omnipage.xml";
			File file = new File(inputDir, fileName);
			// System.out.println(file + " " + file.exists());
			list.add(file);
		}

		return list;
	}

	static List<File> getAllGroundTruthFilesByIds(List<String> idList)
	{
		List<File> list = new ArrayList<>();

		for(String string : idList)
		{
			String fileName = "TUW-" + string + ".pdf";
			File file = new File(inputDir, fileName);
			// System.out.println(file + " " + file.exists());
			list.add(file);
		}

		return list;
	}

	private static void cleanOrCreateDirectory(File outputDir) throws IOException
	{
		if(outputDir.exists())
		{
			FileUtils.cleanDirectory(outputDir);
		}
		else
		{
			outputDir.mkdirs();
		}

	}

	private static List<File> getFileById(String string)
	{
		List<File> groundTruthFiles = getAllFilesAlsoFromSubDirectories(inputDir, string + ".pdf");
		return groundTruthFiles;
	}

	private static void deleteResultAndErrorFiles(File directory)
	{
		List<File> resultOrErrorFiles = Arrays.stream(directory.listFiles()).filter(file -> file.getName().endsWith("mapping.errxml") || file.getName().endsWith("-xstream.xml")).collect(Collectors.toList());
		for(File file : resultOrErrorFiles)
		{
			file.delete();
		}
	}

	private static List<File> getAllGroundTruthFilesAsOmnipage()
	{
		List<File> groundTruthFiles = getAllFilesFromDirectories(inputDir, "-omnipage.xml");
		return groundTruthFiles;
	}

	public static List<File> getAllGroundTruthFiles()
	{
		List<File> groundTruthFiles = getAllFilesAlsoFromSubDirectories(inputDir, ".pdf");
		return groundTruthFiles;
	}

	// public static List<File> getAllGroundTruthFilesAsTxt()
	// {
	// List<File> groundTruthFiles = getAllFilesFromDirectories(inputDirTxt, ".txt");
	// return groundTruthFiles;
	// }

	public static List<File> getAllFilesAlsoFromSubDirectories(File inputFolder, String extension)
	{
		return Arrays.stream(inputFolder.listFiles()).flatMap(file -> Arrays.stream(file.listFiles())).filter(file -> file.getName().endsWith(extension)).collect(Collectors.toList());
	}

	public static List<File> getAllFilesFromDirectories(File inputFolder, String extension)
	{
		return Arrays.stream(inputFolder.listFiles()).filter(file -> file.getName().endsWith(extension)).collect(Collectors.toList());
	}

}