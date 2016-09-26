package com.garansimu;

public class Kategori {
	private int Id       = 0;
	private String Name  = "";
    private String Image = "";

	// constructor tanpa parameter
    public Kategori() {
    
    }
    
    public Kategori(int Id, String Name, String Image){
        this.Id = Id;
        this.Name = Name;
        this.Image = Image;
    }

	// constructor dengan parameter
    public Kategori(String Name){
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}