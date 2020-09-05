package mapping.grobid;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import mapping.result.PublicationType;

public class PublicationTypeAdapter extends XmlAdapter<String, PublicationType>
{
	/*
	 * 	https://tei-c.org/release/doc/tei-p5-doc/de/html/ref-title.html
a
    (unselbst�ndig) der Titel geh�rt zu einer unselbst�ndigen Publikation, wie einem Artikel, Gedicht oder einem anderen Werk, das als Teil einer umfangreicheren Einheit publiziert wurde.
m
    (Monografie) der Titel bezieht sich auf Monografien wie z.B. ein B�cher oder andere selbst�ndige Publikationen, also auch auf einzelne B�nde in einem mehrb�ndigen Werk.
j
    (Zeitschrift) der Titel bezieht sich auf jede Art fortlaufender oder periodischer Ver�ffentlichungen wie z. B. Zeitschriften, Magazine oder Zeitungen.
s
    (Reihe) der Titel bezeichnet eine Reihe von ansonsten selbst�ndig publizierten Ver�ffentlichungen, wie z. B. eine Buchreihe.
u
    (unver�ffentlicht) der Titel bezieht sich auf unver�ffentliches Material (incl. universit�re Qualifikationsarbeiten, soweit sie nicht von einem Verlag ver�ffentlicht worden sind).
	 */
	
	private static Map<String, PublicationType> stringToType = new HashMap<String, PublicationType>();
	
	{
		stringToType.put("a", PublicationType.BEITRAEGE_AUS_TAGUNGSBAENDEN);
		stringToType.put("m", PublicationType.BUCHBEITRAEGE);
		stringToType.put("j", PublicationType.ZEITSCHRIFTENARTIKEL);
		stringToType.put("s", PublicationType.BUCHBEITRAEGE);
		stringToType.put("u", PublicationType.DIPLOM_UND_MASTERARBEITEN);
	}

	/*
	 * Java => XML Given the unmappable Java object, return the desired XML representation.
	 */
	@Override
	public String marshal(PublicationType publicationType) throws Exception
	{
		// not implemented
		return null;
	}

	/*
	 * XML => Java Given an XML string, use it to build an instance of the unmappable class.
	 */
	@Override
	public PublicationType unmarshal(String string) throws Exception
	{
		if(string == null) {
			return null;
		} else {
			PublicationType publicationType = stringToType.get(string);
			return publicationType;
		}
	}

}