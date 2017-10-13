package mapping.result;

public class Affiliation
{
	private String id;
	private String institution;
	private String country;
	private String countryCode;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getInstitution()
	{
		return institution;
	}

	public void setInstitution(String institution)
	{
		this.institution = institution;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getCountryCode()
	{
		return countryCode;
	}

	public void setCountryCode(String countryCode)
	{
		this.countryCode = countryCode;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((countryCode == null) ? 0 : countryCode.hashCode());
		result = prime * result + ((institution == null) ? 0 : institution.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		Affiliation other = (Affiliation)obj;
		if(country == null)
		{
			if(other.country != null) return false;
		}
		else if(!country.equals(other.country)) return false;
		if(countryCode == null)
		{
			if(other.countryCode != null) return false;
		}
		else if(!countryCode.equals(other.countryCode)) return false;
		if(institution == null)
		{
			if(other.institution != null) return false;
		}
		else if(!institution.equals(other.institution)) return false;
		return true;
	}

}
