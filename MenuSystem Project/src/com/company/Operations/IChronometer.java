package com.company.Operations;

public interface IChronometer extends IPrintable {
	void start();
	long stop();
	void lap();
	long getMillis();
	long getSeconds();
	long getMinutes();
	long getHours();
}