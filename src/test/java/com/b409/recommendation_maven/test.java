package com.b409.recommendation_maven;

import java.util.HashSet;
import java.util.Set;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Set<String> set = new HashSet<String>(); 
		Set<String> set1 = new HashSet<String>(); 
		set.add("s"); 
		set.add("s1"); 
		set.add("s2"); 
		set.add("s3"); 
		        set1.add("s3");
		set.retainAll(set1); 
		System.out.println(set); 
	}

}
