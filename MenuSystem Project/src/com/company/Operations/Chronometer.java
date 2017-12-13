package com.company.Operations;

interface Chronometer{
	void start();
	long stop();
	void lap();
	long getMillis();
	long getSeconds();
	long getMinutes();
	long getHours();
}