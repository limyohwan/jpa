package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
        int n = Integer.parseInt(br.readLine());

        for(int i = 0; i < n; i++){
            StringTokenizer st = new StringTokenizer(br.readLine());
            int M = Integer.parseInt(st.nextToken());
            int N = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;

            int cnt = -1;
            boolean checker = false;
            for(int j = x; j < (M * N); j += M){
                if(j % N == y) {
                    System.out.println(j+1);
                    checker = true;
                    break;
                }
            }
            if(!checker){
                System.out.println(-1);
            }
        }            
	}

    public static int gcd(int num1, int num2){
        if(num1 % num2 == 0){
            return num2;
        }else{
            return gcd(num2, num1 % num2);
        }


    }
}