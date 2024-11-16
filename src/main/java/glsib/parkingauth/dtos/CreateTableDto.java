package glsib.parkingauth.dtos;

import java.util.Date;

public class CreateTableDto {
    private double latitude;
    private double longitude;
    private int availableSpots;
    private Date createTime;
    private Date updateTime;
    private String description;
    private double prix;
    private int totalSpots;

    public double getLatitude() {return latitude;}

    public void setLatitude(double latitude) {this.latitude = latitude;}

    public double getLongitude() {return longitude;}

    public void setLongitude(double longitude) {this.longitude = longitude;}

    public int getAvailableSpots() {return availableSpots;}

    public void setAvailableSpots(int availableSpots) {this.availableSpots = availableSpots;}

    public Date getCreateTime() {return createTime;}

    public void setCreateTime(Date createTime) {this.createTime = createTime;}

    public Date getUpdateTime() {return updateTime;}

    public void setUpdateTime(Date updateTime) {this.updateTime = updateTime;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public double getPrix() {return prix;}

    public void setPrix(double prix) {this.prix = prix;}

    public int getTotalSpots() {return totalSpots;}

    public void setTotalSpots(int totalSpots) {this.totalSpots = totalSpots;}
}
