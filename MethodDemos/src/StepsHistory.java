import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.eclipse.persistence.jaxb.JAXBContext;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.JAXBUnmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.itextpdf.text.pdf.PdfReader;

import demos.Demos;
import factory.PublicationFactory;
import mapping.cermine.ReferenceAuthorNameConcatenationWorker;
import mapping.grobid.AuthorNameConcatenationWorker;
import mapping.result.Affiliation;
import mapping.result.Publication;
import mapping.result.Reference;
import mapping.result.Section;
import utils.XStreamUtil;

public class StepsHistory
{
	static File file1 = new File("D:/Java/git/MethodDemosGit/MethodDemos/output/result/result-TUW-137078-xstream.xml");
	static File file2 = new File("D:/Java/git/MethodDemosGit/MethodDemos/output/result/result-TUW-137078-xstream2.xml");
	static File file3 = new File(Demos.grobIdOutputDir, "grobid-TUW-137078-xstream.xml");
	static File file4 = new File("D:/Java/git/MethodDemosGit/MethodDemos/output/result/result-TUW-139761-xstream.xml");
	static File file5 = new File(Demos.pdfxOutputDir, "pdfx-TUW-139761.xml");
	static File file6 = new File("D:/Java/git/MethodDemosGit/MethodDemos/output/result/result-TUW-139785-xstream.xml");
	static File file7 = new File(Demos.pdfxOutputDir, "pdfx-TUW-140048.xml");
	static File file8 = new File("D:/output/methods/result/result-TUW-140048-xstream.xml");
	static File file9 = new File("D:/output/methods/result/result-TUW-140253-xstream.xml");

	public static void main(String[] args) throws Exception
	{
		// Publication p = XStreamUtil.convertFromXML(file, Publication.class);
		//

		// publicationToFile();
		// getPublicationFromFile2();

		// addPackageNames();
		// removePackageNames();
		// getPublicationFromFile3();

		// checkSerialization(file);

		// setRefCounter(file4, (1));

		// setRefMarker(file5, file4);

		// concatNames(file4);

		// xpathSampleStackoverflowQuestion();

		// countPagesGroundTruth();

		// setRefCounter(file6, (-40));

		// setRefMarker(file7, file8);

		// setRefCounter(file9, (-1));

		printRefAuthorYearAndNumber(file9);
	}

	private static void printRefAuthorYearAndNumber(File file)
	{
		Publication publication = XStreamUtil.convertFromXML(file, Publication.class);

		TreeMap<String, String> map = new TreeMap<>();
		Integer x = 1;
		for(Reference reference : publication.getReferences())
		{
			String ref = reference.getAuthors().get(0).getLastName() + " " + reference.getPublicationYear();
			map.put(ref, "[" + x.toString() + "] ");
			x++;
		}
		for(Entry<String, String> entry : map.entrySet())
		{
			System.out.println(entry.getKey() + " " + entry.getValue());
		}

	}

	private static void countPagesGroundTruth() throws IOException
	{
		List<File> files = Demos.getAllGroundTruthFiles();
		int pagesCount = 0;
		Map<String, Integer> map = new TreeMap<>();

		for(File file : files)
		{
			PdfReader reader = new PdfReader(file.getPath());
			int pages = reader.getNumberOfPages();

			map.put(file.getName(), pages);
			pagesCount += pages;

		}
		for(Entry<String, Integer> entry : map.entrySet())
		{
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
		System.out.println(pagesCount);
	}

	private static void xpathSampleStackoverflowQuestion() throws UnsupportedEncodingException
	{
		String xml = "<affiliation key='aff0'>" + "	<orgName type='department'>Institut f�r Computer Graphik und Algorithmen</orgName>" + "	<orgName type='institution'>Technischen Universit�t Wien</orgName>" + "</affiliation>";
		String binding = "<?xml version=\"1.0\"?><xml-bindings    xmlns=\"http://www.eclipse.org/eclipselink/xsds/persistence/oxm\"    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"    xsi:schemaLocation=\"http://www.eclipse.org/eclipselink/xsds/eclipselink_oxm_2_4.xsd\"    package-name=\"mapping.result\">    <xml-schema        namespace=\"\"        element-form-default=\"QUALIFIED\"        prefix=\"\"/>    <java-types>        <java-type name=\"Affiliation\">            <xml-root-element name=\"affiliation\"/>            <java-attributes>                <xml-attribute java-attribute=\"id\" name=\"key\"/>                <xml-element java-attribute=\"institution\" xml-path=\"orgName[@type='institution']/text()\" />	           	<xml-element java-attribute=\"department\" xml-path=\"orgName[@type='department']/text()\" />                <xml-element java-attribute=\"country\" xml-path=\"address/country/text()\" />                <xml-element java-attribute=\"countryCode\" xml-path=\"address/country/@key\" />            </java-attributes>        </java-type>    </java-types></xml-bindings>";

		Map<String, Object> properties = new HashMap<String, Object>(1);
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, new StringReader(binding));

		try
		{
			JAXBContext jc = (JAXBContext)JAXBContextFactory.createContext(new Class[]
			{Publication.class}, properties);
			JAXBUnmarshaller unmarshaller = jc.createUnmarshaller();
			Affiliation affiliation = (Affiliation)unmarshaller.unmarshal(new StringReader(xml));
			System.out.println(affiliation);
		}
		catch(JAXBException e)
		{
			e.printStackTrace();
		}

	}

	private static void concatNames(File file4)
	{
		Publication publication = XStreamUtil.convertFromXML(file4, Publication.class);

		new AuthorNameConcatenationWorker().doWork(publication);
		new ReferenceAuthorNameConcatenationWorker().doWork(publication);

		XStreamUtil.convertToXmL(publication, file4, System.out, true);

	}

	private static void setRefMarker(File pdfxFileWithMarkers, File xStream) throws Exception
	{
		List<String> markers = new ArrayList<>();

		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.parse(pdfxFileWithMarkers);
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile("//ref/text()");
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList)result;
		for(int i = 0; i < nodes.getLength(); i++)
		{
			String s = nodes.item(i).getTextContent();
			String marker = s.substring(0, s.indexOf(" "));
			System.out.println(s);
			markers.add(marker);
		}

		Publication publication = XStreamUtil.convertFromXML(xStream, Publication.class);

		List<Reference> references = publication.getReferences();
		references.forEach(p -> p.setMarker(markers.get(new Integer(p.getId().replace("ref", "")) - 1)));

		XStreamUtil.convertToXmL(publication, xStream, System.out, true);

	}

	private static void setRefCounter(File file, Integer i)
	{
		Publication publication = XStreamUtil.convertFromXML(file, Publication.class);

		// references
		List<Reference> references = publication.getReferences();
		references.forEach(p -> p.setId(setId(p.getId(), i)));

		// section references
		for(Section section : publication.getSections())
		{
			for(final ListIterator<String> iterator = section.getReferenceIds().listIterator(); iterator.hasNext();)
			{
				final String refString = iterator.next();
				iterator.set(setId(refString, i));
			}
		}

		XStreamUtil.convertToXmL(publication, file, System.out, true);
	}

	private static String setId(String id, Integer i)
	{
		Integer idAsInteger = new Integer(id.replaceFirst("ref", ""));
		String s = "ref" + (idAsInteger + i);
		return s;
	}

	private static void checkSerialization(File file) throws IOException
	{
		Publication p = XStreamUtil.convertFromXML(file, Publication.class);
		System.out.println(p);
	}

	private static void removePackageNames() throws IOException
	{
		List<File> files = Demos.getAllFilesFromDirectories(Demos.grobIdOutputDir, "-xstream.xml");
		for(File file : files)
		{
			List<String> strings = FileUtils.readLines(file, StandardCharsets.UTF_8);
			List<String> stringsNew = new ArrayList<>();

			for(String string : strings)
			{
				string = string.replace("mapping.result", "");

				stringsNew.add(string);
			}
			FileUtils.writeLines(file, stringsNew, false);
		}
	}

	private static void getPublicationFromFile3()
	{
		Publication p = XStreamUtil.convertFromXML(file3, Publication.class);

		System.out.println(p);
	}

	private static void addPackageNames() throws IOException
	{
		List<File> files = Demos.getAllFilesFromDirectories(Demos.grobIdOutputDir, "-xstream.xml");
		for(File file : files)
		{
			List<String> strings = FileUtils.readLines(file, StandardCharsets.UTF_8);
			List<String> stringsNew = new ArrayList<>();

			for(String string : strings)
			{
				string = string.replace("Publication>", "mapping.result.Publication>");
				string = string.replace("Author>", "mapping.result.Author>");
				string = string.replace("Affiliation>", "mapping.result.Affiliation>");
				string = string.replace("Section>", "mapping.result.Section>");
				string = string.replace("Reference>", "mapping.result.Reference>");

				stringsNew.add(string);
			}
			FileUtils.writeLines(file, stringsNew, false);
		}

	}

	private static void publicationToFile()
	{
		Publication p2 = PublicationFactory.createPublication();

		XStreamUtil.convertToXmL(p2, file2, System.out, true);

	}

	private static void getPublicationFromFile2()
	{
		Publication p = XStreamUtil.convertFromXML(file2, Publication.class);

	}
}
