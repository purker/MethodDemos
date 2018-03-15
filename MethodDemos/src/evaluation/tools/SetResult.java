package evaluation.tools;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.opencsv.CSVWriter;

import method.Method;
import utils.CSVWriterUtil;
import utils.FileCollectionUtil;
import utils.FormatingUtil;

public class SetResult<T>
{
	private Map<T, EvaluationResult> map = new HashMap<>();

	private Double averagePrecision;
	private Double averageRecall;
	private Double averageF1;

	private CSVWriter csvWriter;

	public SetResult(String methodFile, Method method, String keyLabel) throws IOException
	{
		File file = FileCollectionUtil.getFileByMethod(methodFile, method);
		this.csvWriter = CSVWriterUtil.createCSVWriter(file);

		String[] headers = {keyLabel, "Precision", "Recall", "F1"};
		csvWriter.writeNext(headers);
	}

	void addResult(T key, SingleInformationDocResult<?> sResult)
	{
		EvaluationResult result = map.get(key);
		if(result == null)
		{
			result = new EvaluationResult();
			map.put(key, result);
		}
		result.addResult(sResult);
	}

	void evaluate()
	{
		for(EvaluationResult result : map.values())
		{
			result.evaluate();
		}

		averagePrecision = getAverage(map.values().stream().map(f -> f.getAveragePrecision()).collect(Collectors.toList()));
		averageRecall = getAverage(map.values().stream().map(f -> f.getAverageRecall()).collect(Collectors.toList()));
		averageF1 = getAverage(map.values().stream().map(f -> f.getAverageF1()).collect(Collectors.toList()));
	}

	private Double getAverage(Collection<Double> values)
	{
		double avgSum = 0;
		int avgCount = 0;
		for(Double f : values)
		{
			if(!f.isNaN())
			{
				avgSum += f;
				avgCount++;
			}
		}
		return FormatingUtil.round(avgSum / avgCount);
	}

	public EvaluationResult getResultForKey(T key)
	{
		return map.get(key);
	}

	public Double getAveragePrecision()
	{
		return averagePrecision;
	}

	public Double getAverageRecall()
	{
		return averageRecall;
	}

	public Double getAverageF1()
	{
		return averageF1;
	}

	public String getAveragePrecisionFormated()
	{
		return FormatingUtil.formatDouble(getAveragePrecision());
	}

	public String getAverageRecallFormated()
	{
		return FormatingUtil.formatDouble(getAverageRecall());
	}

	public String getAverageF1Formated()
	{
		return FormatingUtil.formatDouble(getAverageF1());
	}

	public void printKeyEntry(T key)
	{
		String precision = getResultForKey(key).getAveragePrecisionFormatted();
		String recall = getResultForKey(key).getAverageRecallFormatted();
		String f1 = getResultForKey(key).getAverageF1Formatted();

		System.out.printf("%28s: precision: %6s" + ", recall: %6s" + ", F1: %6s\n", key, precision, recall, f1);
		// System.out.printf("%20s: precision: %6.2f" + ", recall: %6.2f" + ", F1: %6.2f\n", type, getPrecisionFormated(type), getRecallFormated(type), getF1Formated(type));
	}

	public void printKeyEntryCSV(T key)
	{
		String precision = getResultForKey(key).getAveragePrecisionFormatted();
		String recall = getResultForKey(key).getAverageRecallFormatted();
		String f1 = getResultForKey(key).getAverageF1Formatted();
		String[] s = {key.toString(), precision, recall, f1};

		csvWriter.writeNext(s);
	}

	public void printSummaryCSV() throws IOException
	{
		String[] s = {"", getAveragePrecisionFormated(), getAverageRecallFormated(), getAverageF1Formated()};
		csvWriter.writeNext(s);

		csvWriter.flush();
	}

}
