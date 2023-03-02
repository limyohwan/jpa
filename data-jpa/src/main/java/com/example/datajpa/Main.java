package com.example.datajpa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        
        int n = Integer.parseInt(br.readLine());
        int m = Integer.parseInt(br.readLine());
        StringTokenizer st = new StringTokenizer(br.readLine());

        String str = "0123456789";

        
        for(int i =0; i < m; i++){
            str = str.replaceFirst(st.nextToken(), "");
        }

        System.out.println(str);


        System.out.println(sb);
		br.close();
	}
    
}
