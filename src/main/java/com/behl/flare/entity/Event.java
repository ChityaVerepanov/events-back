package com.behl.flare.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Deprecated
@Getter
@Setter
public class Event {

	private static final String ENTITY_NAME = "tasks";

	public static String name() {
		return ENTITY_NAME;
	}

	private String title;
	private String description;
	private TaskStatus status;
	private Date dueDate;
	private String createdBy;

}
