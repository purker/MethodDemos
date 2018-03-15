package utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import config.Config;
import method.Method;

public class FileCollectionUtil
{

	public static List<File> getResultFiles()
	{
		File directory = Config.groundTruthResults;

		checkIfContainsFiles(directory);

		return Arrays.asList(directory.listFiles());
	}

	public static List<File> getResultFilesByMethod(Method method)
	{
		File directory = method.getResultDirectory();

		checkIfContainsFiles(directory);

		List<File> files = Arrays.asList(directory.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.endsWith(Config.xStreamFileExtension);
			}
		}));
		return files;
	}

	public static List<File> getCermineResultFiles()
	{
		return getResultFilesByMethod(Method.CERMINE);
	}

	public static List<File> getGrobidResultFiles()
	{
		return getResultFilesByMethod(Method.GROBID);
	}

	public static List<File> getParscitResultFiles()
	{
		return getResultFilesByMethod(Method.PARSCIT);
	}

	public static List<File> getPdfxResultFiles()
	{
		return getResultFilesByMethod(Method.PDFX);
	}

	// TODO brauch ich?
	public static List<File> getParscitXmlFiles()
	{
		File directory = Config.parsCitOutputDir;

		checkIfContainsFiles(directory);

		List<File> files = Arrays.asList(directory.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				return name.endsWith(Config.parscitFileExtension);
			}
		}));
		return files;
	}

	public static List<File> getResultFilesById(String pubId)
	{
		File directory = Config.groundTruthResults;

		checkIfContainsFiles(directory);

		return Arrays.asList(directory.listFiles());
	}

	public static File getCermineResultFileById(String pubId)
	{
		return getResultFilesByMethodAndId(Method.CERMINE, pubId);
	}

	public static File getGrobidResultFileById(String pubId)
	{
		return getResultFilesByMethodAndId(Method.GROBID, pubId);
	}

	public static File getParscitResultFileById(String pubId)
	{
		return getResultFilesByMethodAndId(Method.PARSCIT, pubId);
	}

	public static File getPdfxResultFileById(String pubId)
	{
		return getResultFilesByMethodAndId(Method.PDFX, pubId);
	}

	public static File getResultFilesByMethodAndId(Method method, String pubId)
	{
		File file = new File(method.getResultDirectory(), FileNameUtil.getResultFileNameByMethodAndId(method, pubId));
		return file;
	}

	public static File getPdfFileById(String pubId)
	{
		File file = new File(Config.groundTruth, FileNameUtil.getPdfFileNameFromID(pubId));
		return file;
	}

	private static void checkIfContainsFiles(File directory)
	{
		if(!directory.exists())
		{
			throw new IllegalArgumentException("provided input file must exist: " + directory);
		}
		if(!directory.isDirectory())
		{
			throw new IllegalArgumentException("provided input file must be a directory: " + directory);
		}
		if(directory.list().length == 0)
		{
			throw new IllegalArgumentException("provided directory can't be empty: " + directory);
		}

	}

	public static List<String> getFilesIdsWithoutPrefix(List<File> files)
	{
		List<String> ids = files.stream().map(f -> PublicationUtil.getIdFromFileWithoutPrefix(f)).collect(Collectors.toList());

		return ids;
	}

	public static File getFileByMethod(String file, Method method)
	{
		return new File(file.replace("<method>", method.getName()));
	}
}
