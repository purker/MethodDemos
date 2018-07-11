package evaluation;
/**
 * This file is part of CERMINE project.
 * Copyright (c) 2011-2016 ICM-UW
 *
 * CERMINE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CERMINE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with CERMINE. If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import evaluation.informationresults.AbstractSingleInformationResult;
import evaluation.informationresults.ListInformationResult;
import evaluation.informationresults.ReferenceInformationResult;
import evaluation.informationresults.RelationInformationResult;
import evaluation.informationresults.RelationInformationResult.StringRelation;
import evaluation.informationresults.SimpleInformationResult;
import evaluation.tools.AbstractCollectionResult;
import evaluation.tools.EvalInformationType;
import evaluation.tools.EvaluationResult;
import evaluation.tools.PublicationIterator;
import evaluation.tools.PublicationPair;
import evaluation.tools.PublicationCollectionResult;
import evaluation.tools.ReferenceCollectionResult;
import evaluation.tools.SetResult;
import evaluation.tools.WriterWrapper;
import evaluation.tools.CollectionEnum;
import mapping.result.Affiliation;
import mapping.result.Author;
import mapping.result.FileId;
import mapping.result.KeyStringInterface;
import mapping.result.Publication;
import mapping.result.Reference;
import mapping.result.ReferenceAuthor;
import mapping.result.Section;
import method.Method;
import pl.edu.icm.cermine.evaluation.exception.EvaluationException;
import utils.CollectionUtil;
import utils.FailureUtil;
import utils.FileCollectionUtil;

/**
 * @author Angela
 *
 */
public abstract class SystemEvaluator
{
	protected PublicationIterator iter;

	protected PublicationCollectionResult results;
	protected ReferenceCollectionResult refResults;
	protected Collection<EvaluationMode> modes;

	public SystemEvaluator(Collection<EvalInformationType> types, Collection<EvalInformationType> referenceTypes, List<EvaluationMode> modes) throws IOException
	{
		System.out.println("Starting Evaluation: " + getMethod());
		this.iter = new PublicationIterator(getOriginalFiles(), getExtractedFiles());

		this.results = new PublicationCollectionResult(modes, getMethod(), types);
		this.refResults = new ReferenceCollectionResult(modes, getMethod(), referenceTypes);
		this.modes = modes;
	}

	protected List<File> getOriginalFiles()
	{
		return FileCollectionUtil.getResultFiles();
	}

	protected abstract List<File> getExtractedFiles();

	protected abstract Method getMethod();

	public void evaluate() throws EvaluationException, IOException
	{
		evaluate(iter);
	}

	protected Collection<EvalInformationType> getTypes()
	{
		return results.getEvalTypes();
	}

	protected Collection<EvalInformationType> getReferenceTypes()
	{
		return refResults.getEvalTypes();
	}

	/**
	 * @param modes
	 *            which outputs should be generated, if empty -> no output (for test purposes)
	 * @param files
	 * @return
	 * @throws EvaluationException
	 * @throws IOException
	 */
	public AbstractCollectionResult<?> evaluate(PublicationIterator files) throws EvaluationException, IOException
	{

		int i = 0;
		for(PublicationPair pair : files)
		{
			i++;

			Publication origPub = pair.getOriginalPub();
			Publication testPub = pair.getExtractedPub();
			File originalFile = pair.getOriginalFile();

			KeyStringInterface id; // for identification of the result
			if(originalFile != null)
			{
				id = new FileId(origPub, pair.getOriginalFile());
			}
			else
			{
				id = origPub;
			}

			if(modes.contains(EvaluationMode.SYSOUT_DETAILED))
			{
				results.printDocument(id, i);
			}

			for(EvalInformationType type : getTypes())
			{
				AbstractSingleInformationResult<?> result = getResultFromType(type, origPub, testPub);
				result.evaluate();
				results.addResult(origPub, result);

				if(type.equals(EvalInformationType.REFERENCES))
				{
					List<Pair<Reference, Reference>> matchingReferences = ((ReferenceInformationResult)result).getMatchingReferences();

					for(Pair<Reference, Reference> refPair : matchingReferences)
					{
						for(EvalInformationType refType : getReferenceTypes())
						{
							try
							{
								AbstractSingleInformationResult<?> refResult = getResultFromReferenceType(refType, refPair.getLeft(), refPair.getRight());
								// todo l�schen if(refResult == null) continue;
								refResult.evaluate();
								refPair.getLeft().setPublicationType(origPub.getPublicationType()); // TODO eventuell sch�ner, wenn geht
								refResults.addResult(refPair.getLeft(), refResult);

								// System.out.println(refPair.getLeft());
								// System.out.println(refPair.getRight());
								// System.out.println();
							}
							catch(Exception e)
							{
								FailureUtil.failureExit(e, System.err, "reference " + refPair.getKey(), true);
							}
						}
					}
				}
			}
		}

		System.out.println("Evaluation of PUBLICATIONS");
		results.evaluate();
		results.printResults();

		System.out.println("Evaluation of REFERENCES");
		refResults.evaluate();
		refResults.printResults();

		return results;
	}

	private AbstractSingleInformationResult<?> getResultFromReferenceType(EvalInformationType type, Reference reference, Reference reference2) throws EvaluationException
	{
		switch(type)
		{
			case REFERENCE_MARKER:
				return new SimpleInformationResult(type, reference, reference2, Reference::getMarker);

			case REFERENCE_TITLE:
				return new SimpleInformationResult(type, reference, reference2, Reference::getTitle);

			// TODO case REFERENCE_PUBLICATIONTYPE:
			// return new SimpleInformationResult(type, reference, reference2, )Reference::getPublication));
			case REFERENCE_SOURCE:
				return new SimpleInformationResult(type, reference, reference2, Reference::getSource);
			case REFERENCE_PUBLISHER:
				return new SimpleInformationResult(type, reference, reference2, Reference::getPublisher);
			case REFERENCE_EDITOR:
				return new SimpleInformationResult(type, reference, reference2, Reference::getEditors);
			case REFERENCE_AUTHORS:
				return new ListInformationResult(type, reference, reference2, Reference::getAuthors, ReferenceAuthor::toString);

			case REFERENCE_EDITION:
				return new SimpleInformationResult(type, reference, reference2, Reference::getEdition);
			case REFERENCE_LOCATION:
				return new SimpleInformationResult(type, reference, reference2, Reference::getLocation);
			case REFERENCE_VOLUME:
				return new SimpleInformationResult(type, reference, reference2, Reference::getVolume);
			case REFERENCE_ISSUE:
				return new SimpleInformationResult(type, reference, reference2, Reference::getIssue);
			case REFERENCE_CHAPTER:
				return new SimpleInformationResult(type, reference, reference2, Reference::getChapter);
			case REFERENCE_NOTE:
				return new SimpleInformationResult(type, reference, reference2, Reference::getNote);
			// TODO case REFERENCE_PAGES:
			// return new SimpleInformationResult(type, reference, reference2, Reference::getPage);
			case REFERENCE_PAGEFROM:
				return new SimpleInformationResult(type, reference, reference2, Reference::getPageFrom);
			case REFERENCE_PAGETO:
				return new SimpleInformationResult(type, reference, reference2, Reference::getPageTo);
			case REFERENCE_DATE:
				return new SimpleInformationResult(type, reference, reference2, Reference::getPublicationDateString); // TODO?
			case REFERENCE_DOI:
				return new SimpleInformationResult(type, reference, reference2, Reference::getDoi);
			case REFERENCE_URL:
				return new SimpleInformationResult(type, reference, reference2, Reference::getUrl);

			default:
				throw new EvaluationException("referencetype " + type + " not known");
		}
	}

	private AbstractSingleInformationResult<?> getResultFromType(EvalInformationType type, Publication origPub, Publication testPub) throws EvaluationException
	{
		switch(type)
		{
			case TITLE:
				return new SimpleInformationResult(type, origPub, testPub, Publication::getTitle);

			case ABSTRACT:
				return new SimpleInformationResult(type, origPub, testPub, Publication::getAbstractText);

			case ABSTRACTGERMAN:
				return new SimpleInformationResult(type, origPub, testPub, Publication::getAbstractTextGerman);

			case KEYWORDS:
				return new ListInformationResult(type, origPub, testPub, Publication::getKeywords, String::toString);

			case AUTHORS:
				return new ListInformationResult(type, origPub, testPub, Publication::getAuthors, Author::toString);

			case AFFILIATIONS:
				return new ListInformationResult(type, origPub, testPub, Publication::getAffiliations, Affiliation::toString);

			case AUTHOR_AFFILIATIONS:
				Set<StringRelation> relOrig = new HashSet<>();
				for(Author author : CollectionUtil.emptyIfNull(origPub.getAuthors()))
				{
					for(Affiliation aff : CollectionUtil.emptyIfNull(author.getAffiliations()))
					{
						relOrig.add(new StringRelation(author.toString(), aff.toString()));
					}
				}
				Set<StringRelation> relTest = new HashSet<>();
				for(Author author : CollectionUtil.emptyIfNull(testPub.getAuthors()))
				{
					for(Affiliation aff : CollectionUtil.emptyIfNull(author.getAffiliations()))
					{
						relTest.add(new StringRelation(author.toString(), aff.toString()));
					}

				}
				return new RelationInformationResult(type, relOrig, relTest);

			case EMAILS:
			{
				return new ListInformationResult(type, origPub, testPub, Publication::getAuthors, Author::getEmail);
			}
			case AUTHOR_EMAILS:
				Set<StringRelation> emailsOrig = new HashSet<>();
				for(Author author : origPub.getAuthors())
				{
					if(author.toString() != null && author.getEmail() != null)
					{
						emailsOrig.add(new StringRelation(author.toString(), author.getEmail()));
					}
				}
				Set<StringRelation> emailsTest = new HashSet<>();
				for(Author author : testPub.getAuthors())
				{
					if(author.toString() != null && author.getEmail() != null)
					{
						emailsTest.add(new StringRelation(author.toString(), author.getEmail()));
					}
				}
				return new RelationInformationResult(type, emailsOrig, emailsTest);

			case SOURCE:
				return new SimpleInformationResult(type, origPub, testPub, Publication::getSource);

			case VOLUME:
				return new SimpleInformationResult(type, origPub, testPub, Publication::getVolume);

			case ISSUE:
				return new SimpleInformationResult(type, origPub, testPub, Publication::getIssue);

			case PAGE_FROM:
				return new SimpleInformationResult(type, origPub, testPub, Publication::getPageFrom);

			case PAGE_TO:
				return new SimpleInformationResult(type, origPub, testPub, Publication::getPageTo);

			case YEAR:
				return new SimpleInformationResult(type, origPub, testPub, Publication::getPublicationYear);

			case DOI:
				return new SimpleInformationResult(type, origPub, testPub, Publication::getDoi);

			case SECTIONS:
				return new ListInformationResult(type, origPub, testPub, Publication::getSections, Section::getTitle);

			case SECTION_LEVELS:
				Set<StringRelation> headersOrig = new HashSet<>();
				for(Section section : origPub.getSections())
				{
					if(section.getTitle() != null && section.getLevel() != null)
					{
						headersOrig.add(new StringRelation(section.getTitle(), section.getLevel()));
					}
				}
				Set<StringRelation> headersTest = new HashSet<>();
				for(Section section : testPub.getSections())
				{
					if(section.getTitle() != null && section.getLevel() != null)
					{
						headersTest.add(new StringRelation(section.getTitle(), section.getLevel()));
					}
				}
				return new RelationInformationResult(type, headersOrig, headersTest);

			case SECTION_REFERENCES:
				Set<StringRelation> sectionReferencesOrig = new HashSet<>();
				for(Section section : origPub.getSections())
				{
					if(CollectionUtil.isNotEmpty(section.getReferenceIds()))
					{
						String referenceIds = String.join(" ", section.getReferenceIds());
						sectionReferencesOrig.add(new StringRelation(section.getTitle(), referenceIds));
					}
				}
				Set<StringRelation> sectionReferencesTest = new HashSet<>();
				for(Section section : testPub.getSections())
				{
					if(CollectionUtil.isNotEmpty(section.getReferenceIds()))
					{
						String referenceIds = String.join(" ", section.getReferenceIds());
						sectionReferencesTest.add(new StringRelation(section.getTitle(), referenceIds));
					}
				}
				return new RelationInformationResult(type, sectionReferencesOrig, sectionReferencesTest);

			case REFERENCES:
				return new ReferenceInformationResult(type, origPub.getReferences(), testPub.getReferences(), origPub);

			default:
				throw new EvaluationException("type not known");
		}
	}

	public static void printOverallStatistics(List<EvaluationMode> modes, SystemEvaluator... evaluators) throws IOException
	{
		AbstractCollectionResult<?> s0 = evaluators[0].getSetResultBySetResultEnum(CollectionEnum.REFERENCE);
		SetResult<String> setResult0 = (SetResult<String>)s0.getSetResultByMode(EvaluationMode.CSV_PER_FILE);
		AbstractCollectionResult<?> s1 = evaluators[1].getSetResultBySetResultEnum(CollectionEnum.REFERENCE);
		SetResult<String> setResult1 = (SetResult<String>)s1.getSetResultByMode(EvaluationMode.CSV_PER_FILE);

		System.out.println(setResult0.map.keySet());
		System.out.println(setResult1.map.keySet());
		if(modes.contains(EvaluationMode.CSV_PER_FILE))
		{
			EvaluationMode mode = EvaluationMode.CSV_PER_FILE;
			System.out.println(EvaluationMode.CSV_PER_FILE);
			for(CollectionEnum setResultEnum : CollectionEnum.values())
			{
				String file = FileCollectionUtil.getFileByMethodAndSetResultType(mode.getStatisticsFile(), setResultEnum, Method.ALL);
				WriterWrapper writer = new WriterWrapper(file);

				AbstractCollectionResult<?> setResultOfFirstEvaluator = evaluators[0].getSetResultBySetResultEnum(setResultEnum);

				// System.out.println(setResultOfFirstEvaluator.getIdSet());
				for(String key : setResultOfFirstEvaluator.getIdSet())
				{
					System.out.println(key);

					List<String> columns = new ArrayList<>();
					columns.add(key);
					for(SystemEvaluator evaluator : evaluators)
					{
						// System.out.println(evaluator.getClass().getSimpleName() + evaluator.getSetResultBySetResultEnum(setResultEnum, mode).getIdSet());
						AbstractCollectionResult<?> abstractSetResult = evaluator.getSetResultBySetResultEnum(setResultEnum);
						SetResult<String> setResult = (SetResult<String>)abstractSetResult.getSetResultByMode(mode);
						EvaluationResult evaluationResult = setResult.getResultForKey(key);

						if(evaluationResult == null)
						{
							System.out.println(abstractSetResult.getIdSet());
							System.err.println("empty:" + key);
							System.err.println("mode:" + mode);
							System.err.println("evaluator:" + evaluator.getClass().getSimpleName());

						}
						List<String> statisticValues = setResult.getStatisticValues(evaluationResult);
						columns.addAll(statisticValues);
					}
					writer.writeNext(columns);
				}
				writer.close();
			}
		}
		// if(modes.contains(EvaluationMode.CSV_PER_PUBLICATIONTYPE))
		// {
		// System.out.println(EvaluationMode.CSV_PER_PUBLICATIONTYPE);
		// getPerPublicationType().printCSVStatistics();
		// }
		// if(modes.contains(EvaluationMode.CSV_PER_FILE))
		// {
		// System.out.println(EvaluationMode.CSV_PER_FILE);
		// getPerId().printCSVStatistics();
		// }

	}

	private AbstractCollectionResult<?> getSetResultBySetResultEnum(CollectionEnum setResultEnum)
	{
		AbstractCollectionResult<?> abstractSetResult = null;
		if(setResultEnum.equals(CollectionEnum.PUBLICATION))
		{
			abstractSetResult = this.results;
		}
		else
			if(setResultEnum.equals(CollectionEnum.REFERENCE))
			{
				abstractSetResult = this.refResults;
			}
			else
			{
				FailureUtil.exit("SetResultEnum not supported");
			}

		return abstractSetResult;
	}

	// public void process(String[] args) throws EvaluationException
	// {
	// if(args.length != 4 && args.length != 5)
	// {
	// System.out.println("Usage: " + this.getClass().getSimpleName() + " <input orig dir> <input extract dir> <orig extension> <extract extension> <mode>");
	// return;
	// }
	// File originalDirectory = new File(args[0]);
	// File extractedDirectory = new File(args[1]);
	// String origExt = args[2];
	// String extrExt = args[3];
	//
	// EvaluationMode mode = EvaluationMode.SYSOUT_DETAILED;
	// if(args.length == 5 && args[4].equals("csv"))
	// {
	// mode = EvaluationMode.CSV;
	// }
	// if(args.length == 5 && args[4].equals("q"))
	// {
	// mode = EvaluationMode.SYSOUT_SUMMARY;
	// }
	//
	// PublicationIterator2 iter = new PublicationIterator2(originalDirectory, extractedDirectory, origExt, extrExt);
	// evaluate(mode, iter);
	// }

}
