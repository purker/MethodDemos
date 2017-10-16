package mapping.result;

import java.util.ArrayList;
import java.util.List;

public class Section
{
	private String id;
	private String title;
	private String type;
	private SectionType typeEnum;
	private List<String> referenceIds = new ArrayList<>();

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

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public SectionType getTypeEnum()
	{
		return typeEnum;
	}

	public void setTypeEnum(SectionType typeEnum)
	{
		this.typeEnum = typeEnum;
	}

	public List<String> getReferenceIds()
	{
		return referenceIds;
	}

	public void setReferenceIds(List<String> referenceIds)
	{
		this.referenceIds = referenceIds;
	}

}
