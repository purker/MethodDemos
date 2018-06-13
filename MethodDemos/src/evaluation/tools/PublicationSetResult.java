package evaluation.tools;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import evaluation.EvaluationMode;
import mapping.result.Publication;
import method.Method;

public class PublicationSetResult extends AbstractSetResult<Publication>
{
	public PublicationSetResult(List<EvaluationMode> modes, Method method, Collection<EvalInformationType> types) throws IOException
	{
		super(modes, method, types);
	}

	@Override
	protected Comparator<String> getComparator()
	{
		return Comparator.comparing(String::toString);
	}

	@Override
	protected SetResultEnum getSetResultType()
	{
		return SetResultEnum.PUBLICATION;
	}

}
