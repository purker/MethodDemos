package mapping.parscit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import demos.Demos;
import mapping.Mapper;
import mapping.ReferenceSetPublicationWorker;
import mapping.Worker;
import method.Method;

public class ParsCitMapper extends Mapper
{
	private static final Method METHOD = Method.PARSCIT;
	private static final File DIRECTORY_NAME = Demos.parsCitOutputDir;
	public static final String BINDINGFILE = "bindingfiles/binding_parscit.xml";

	@Override
	protected String getBindingFile()
	{
		return BINDINGFILE;
	}

	public static void main(String[] args) throws Exception
	{
		ParsCitMapper parscitMapper = new ParsCitMapper();
		File inputFile = new File("D:/output/all/parscit-TUW-138011.xml");

		parscitMapper.unmarshallFile(inputFile);

		// parscitMapper.marshall(PublicationFactory.createPublication(), System.out);
	}

	@Override
	protected Method getMethod()
	{
		return METHOD;
	}

	@Override
	protected List<? extends Worker> getWorkers()
	{
		List<Worker> workers = new ArrayList<>();
		workers.add(new ReferenceSetPublicationWorker());
		workers.add(new ReferenceIdInitializerParscit());
		workers.add(new SectionTypeNormalizerWorker());

		return workers;
	}

	@Override
	protected File getDirectory()
	{
		return DIRECTORY_NAME;
	}
}