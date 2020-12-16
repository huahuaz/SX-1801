package com.yc.bean;

public class Film {
	
	Integer num;//±àºÅ
	Integer fid;
	String fname;
	String ftype;
	String fyear;
	Integer flength;
	String fdate;
	String director;
	String actors;
	String farea;
	String fimage;
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getFtype() {
		return ftype;
	}
	public void setFtype(String ftype) {
		this.ftype = ftype;
	}
	public String getFyear() {
		return fyear;
	}
	public void setFyear(String fyear) {
		this.fyear = fyear;
	}
	public Integer getFlength() {
		return flength;
	}
	public void setFlength(Integer flength) {
		this.flength = flength;
	}
	public String getFdate() {
		return fdate;
	}
	public void setFdate(String fdate) {
		this.fdate = fdate;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getActors() {
		return actors;
	}
	public void setActors(String actors) {
		this.actors = actors;
	}
	public String getFarea() {
		return farea;
	}
	public void setFarea(String farea) {
		this.farea = farea;
	}
	public String getFimage() {
		return fimage;
	}
	public void setFimage(String fimage) {
		this.fimage = fimage;
	}
	
	
	@Override
	public String toString() {
		return "Film [num=" + num + ", fid=" + fid + ", fname=" + fname + ", ftype=" + ftype + ", fyear=" + fyear
				+ ", flength=" + flength + ", fdate=" + fdate + ", director=" + director + ", actors=" + actors
				+ ", farea=" + farea + ", fimage=" + fimage + "]";
	}
	
	
	
}
