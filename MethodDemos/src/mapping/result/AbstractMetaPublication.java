package mapping.result;

import java.util.Date;

public class AbstractMetaPublication extends BaseEntity
{
	private static final long serialVersionUID = 5037061578663513484L;

	protected String id;
	protected String title;
	protected String source;
	protected String edition;
	protected String volume;
	protected String issue;
	protected String chapter;

	protected String pageFrom;
	protected String pageTo;

	protected String publicationDateString; // as in the extraction xml
	protected String publicationDay;
	protected String publicationMonth;
	protected String publicationYear; // if null, information found in date
	protected Date publicationDate;

	protected String url;
	protected String doi;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public String getEdition()
	{
		return edition;
	}

	public void setEdition(String edition)
	{
		this.edition = edition;
	}

	public String getVolume()
	{
		return volume;
	}

	public void setVolume(String volume)
	{
		this.volume = volume;
	}

	public String getIssue()
	{
		return issue;
	}

	public void setIssue(String issue)
	{
		this.issue = issue;
	}

	public String getChapter()
	{
		return chapter;
	}

	public void setChapter(String chapter)
	{
		this.chapter = chapter;
	}

	public String getPageFrom()
	{
		return pageFrom;
	}

	public void setPageFrom(String pageFrom)
	{
		this.pageFrom = pageFrom;
	}

	public String getPageTo()
	{
		return pageTo;
	}

	public void setPageTo(String pageTo)
	{
		this.pageTo = pageTo;
	}

	public String getPublicationDateString()
	{
		return publicationDateString;
	}

	public void setPublicationDateString(String publicationDateString)
	{
		this.publicationDateString = publicationDateString;
	}

	public String getPublicationDay()
	{
		return publicationDay;
	}

	public void setPublicationDay(String publicationDay)
	{
		this.publicationDay = publicationDay;
	}

	public String getPublicationMonth()
	{
		return publicationMonth;
	}

	public void setPublicationMonth(String publicationMonth)
	{
		this.publicationMonth = publicationMonth;
	}

	public String getPublicationYear()
	{
		return publicationYear;
	}

	public void setPublicationYear(String publicationYear)
	{
		this.publicationYear = publicationYear;
	}

	public Date getPublicationDate()
	{
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate)
	{
		this.publicationDate = publicationDate;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getDoi()
	{
		return doi;
	}

	public void setDoi(String doi)
	{
		this.doi = doi;
	}

}