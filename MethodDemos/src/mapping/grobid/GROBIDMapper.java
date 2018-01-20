package mapping.grobid;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import demos.Demos;
import factory.PublicationFactory;
import mapping.Mapper;
import mapping.Worker;
import method.Method;
import utils.XStreamUtil;

public class GROBIDMapper extends Mapper
{
	private static final Method METHOD = Method.GROBID;
	private static final File DIRECTORY_NAME = Demos.grobIdOutputDir;
	public static final String BINDINGFILE = "bindingfiles/binding_grobid.xml";

	@Override
	protected String getBindingFile()
	{
		return BINDINGFILE;
	}

	public static void main(String[] args) throws Exception
	{
		GROBIDMapper grobidMapper = new GROBIDMapper();
		File inputFile = new File("D:/output/methods/grobid/grobid-TUW-198408.xml");
		File outputFile = new File(inputFile.getParentFile(), inputFile.getName().replace(".xml", "-xstream.xml"));

		// grobidMapper.unmarshall(inputFile, outputFile);

		// grobidMapper.marshall(PublicationFactory.createPublication(), System.out);

		XStreamUtil.convertToXmL(PublicationFactory.createPublication(), System.out, System.out, true);
	}

	@Override
	protected Method getMethod()
	{
		return METHOD;
	}

	@Override
	protected List<? extends Worker> getWorkers()
	{
		return Arrays.asList(new AffiliationWorker(), /* dort auch wieder einkommentieren new AuthorNameConcatenationWorker(), */new ReferenceIdReplaceWorker(), new ReferenceEditionWorker(), new ReferenceDateWorker());
	}

	@Override
	protected File getDirectory()
	{
		return DIRECTORY_NAME;
	}

}