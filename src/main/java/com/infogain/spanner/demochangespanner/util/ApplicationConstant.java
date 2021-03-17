package com.infogain.spanner.demochangespanner.util;

public final class ApplicationConstant {

	private ApplicationConstant() {
	}

	public static final String LAST_POLLER_EXECUTION_TIMESTAMP_PLACE_HOLDER = "lastPollerExecutionCommitTimestamp";
	public static final String LAST_COMMIT_TIMESTAMP_SAVE_QUERY = "INSERT INTO Singers (SingerId, FirstName, LastName) VALUES(@"
			+ LAST_POLLER_EXECUTION_TIMESTAMP_PLACE_HOLDER + ")";

}