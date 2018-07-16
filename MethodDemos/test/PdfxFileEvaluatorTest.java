import evaluation.PdfxXStreamFileEvaluator;
import evaluation.SystemEvaluator;

class PdfxFileEvaluatorTest extends AbstractFileEvaluatorTest
{
	private static PdfxXStreamFileEvaluator e = new PdfxXStreamFileEvaluator();

	@Override
	protected SystemEvaluator getEvalutator()
	{
		return e;
	}
}
