package poly.dto;

public class TicketDTO {
	private String departure_Place;	// 출발지
	private String arrival_Place;	// 도착지
	private String departure_Date;	// 출발일
	private String arrival_Date;	// 반환일
	
	private String flight_Type;	// 여행구분
	private int adult;	// 성인
	private int teenager;	// 청소년
	private int child;	// 어린이
	private int baby;	// 유아
	private String class_Type;	// 좌석
	
	
	public String getDeparture_Place() {
		return departure_Place;
	}
	public void setDeparture_Place(String departure_Place) {
		this.departure_Place = departure_Place;
	}
	public String getArrival_Place() {
		return arrival_Place;
	}
	public void setArrival_Place(String arrival_Place) {
		this.arrival_Place = arrival_Place;
	}
	public String getDeparture_Date() {
		return departure_Date;
	}
	public void setDeparture_Date(String departure_Date) {
		this.departure_Date = departure_Date;
	}
	public String getArrival_Date() {
		return arrival_Date;
	}
	public void setArrival_Date(String arrival_Date) {
		this.arrival_Date = arrival_Date;
	}
	public String getFlight_Type() {
		return flight_Type;
	}
	public void setFlight_Type(String flight_Type) {
		this.flight_Type = flight_Type;
	}
	public int getAdult() {
		return adult;
	}
	public void setAdult(int adult) {
		this.adult = adult;
	}
	public int getTeenager() {
		return teenager;
	}
	public void setTeenager(int teenager) {
		this.teenager = teenager;
	}
	public int getChild() {
		return child;
	}
	public void setChild(int child) {
		this.child = child;
	}
	public int getBaby() {
		return baby;
	}
	public void setBaby(int baby) {
		this.baby = baby;
	}
	public String getClass_Type() {
		return class_Type;
	}
	public void setClass_Type(String class_Type) {
		this.class_Type = class_Type;
	}
	
	
}
