import evaluation.ParscitXStreamFileEvaluator;
import evaluation.SystemEvaluator;

class ParscitFileEvaluatorTest extends AbstractFileEvaluatorTest
{
	private static ParscitXStreamFileEvaluator e = new ParscitXStreamFileEvaluator();

	@Override
	protected SystemEvaluator getEvalutator()
	{
		return e;
	}
}
