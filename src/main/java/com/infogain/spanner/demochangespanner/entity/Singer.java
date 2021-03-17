package com.infogain.spanner.demochangespanner.entity;

import org.springframework.cloud.gcp.data.spanner.core.mapping.PrimaryKey;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Table(name = "Singers")
public class Singer {
	
  @PrimaryKey
  long SingerId;

  String FirstName;

  String LastName;
  
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  java.sql.Timestamp LastUpdateTime;
  
  public Singer() {
	}

public Singer(long singerId, String firstName, String lastName) {
	super();
	SingerId = singerId;
	FirstName = firstName;
	LastName = lastName;
}

public long getSingerId() {
	return SingerId;
}

public void setSingerId(long singerId) {
	SingerId = singerId;
}

public String getFirstName() {
	return FirstName;
}

public void setFirstName(String firstName) {
	FirstName = firstName;
}

public String getLastName() {
	return LastName;
}

public void setLastName(String lastName) {
	LastName = lastName;
}

public java.sql.Timestamp getLastUpdateTime() {
	return LastUpdateTime;
}

public void setLastUpdateTime(java.sql.Timestamp lastUpdateTime) {
	LastUpdateTime = lastUpdateTime;
}


  
  

}
