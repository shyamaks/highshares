
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CompanyList {
	private List<Company> companies;
	private Company[] highShareCompanies ;
	private int lstIndex = 0;
	private String FilePath;

	public String getFilePath() {
		return FilePath;
	}



	public void setFilePath(String filePath) {
		FilePath = filePath;
	}



	public CompanyList() {
	}

	

	private Company getNextColumnData() {
		if (companies == null) {
			lstIndex = 0;
			try {
				loadCompanies();
			} catch (Exception e) {
			}
		}
		if (companies == null)
			return null;
		if (lstIndex < companies.size())
			return companies.get(lstIndex++);
		return null;
	}

	public void loadCompanies() throws Exception {
		Scanner s = null;
		try {
			companies = new ArrayList<Company>();
			File f = new File(FilePath);
			System.out.println(f.getAbsolutePath());
			s = new Scanner(new FileInputStream(f));
			String[] headers = readLine(s);
			System.out.println("headers: " + Arrays.toString(headers));
			if (headers != null && headers.length > 0) {
				String[] data = null;
				while ((data = readLine(s)) != null) {
					System.out.println("data: " + Arrays.toString(data));
					if (data.length != headers.length) {
						companies = null;
						throw new Exception("Invalid Data - headers count "
								+ headers.length
								+ " does not match with data count "
								+ data.length);
					}
					String year = data[0];
					String month = data[1];
					for (int x = 2; x < data.length; x++) {
						double price = new Double(data[x]).doubleValue();
						Company company = new Company(headers[x], year, month,
								price);
						companies.add(company);
					}
				}
			}
		} finally {
			if (s != null)
				s.close();
		}
	}

	private String[] readLine(Scanner s) {
		if (s.hasNextLine()) {
			return s.nextLine().trim().split(",");
		}
		return null;
	}

	public void findHighShares() {
		
		Map<String, Company> companies = new HashMap<String, Company>();
		Company newCompany = null;
		
		while ((newCompany = getNextColumnData()) != null) {
			Company oldCompany = companies.get(newCompany.getName());
			if (oldCompany == null
					|| newCompany.getPrice() > oldCompany.getPrice())
				companies.put(newCompany.getName(), newCompany);
		}
		int i=0;
		highShareCompanies=new Company[companies.size()];
		for (String name : companies.keySet()) {
			Company company = companies.get(name);
			highShareCompanies[i]=company;
			System.out.println(company.getName() + " in year  "
					+ company.getYear() + " for month " + company.getMonth()
					+ " Maximum price " + company.getPrice());
			i++;
		}
	}

	public Company[] getHighShareCompanies() {
		return highShareCompanies;
	}



	public void setHighShareCompanies(Company[] highShareCompanies) {
		this.highShareCompanies = highShareCompanies;
	}



	public static void main(String[] args) {
		CompanyList companyList = new CompanyList();
		companyList.findHighShares();
	}

}
