package lk.ucsc.marketsl;

public class Crop {
	
	private String name, location, price;
	
	public Crop(String name, String location, String price){
		this.name = name;
		this.location = location;
		this.price = price;
	}
	
	public Crop(){
		this.name = this.location = this.price = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
