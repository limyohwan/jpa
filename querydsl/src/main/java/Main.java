import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
        
        int N = Integer.parseInt(br.readLine());
        StringTokenizer st = new StringTokenizer(br.readLine());

        int[] intArr = new int[N];
        for(int i =0; i < N; i++){
            intArr[i] = Integer.parseInt(st.nextToken());
        }

        boolean checker = true;
        for(int i = N-1; i >0; i--){
            if(intArr[i] > intArr[i-1]){
                if(i != 1){
                    int tmp = intArr[i];
                    intArr[i] = intArr[i-1];
                    intArr[i-1] = tmp;

                    for(int k = 0; k < i; k++){
                        sb.append(intArr[k] + " ");
                    }

                    int[] newArr = new int[N-i];
                    int imsi = 0;
                    for(int j = i; j <N; j++){
                        newArr[imsi] = intArr[j];
                        imsi+=1;
                    }
                    Arrays.sort(newArr);
                    for(int j = 0; j <newArr.length; j++){
                        sb.append(newArr[j] + " ");
                    }
                    checker = false;
                    break;
                }else{
                    int num = intArr[i-1] + 1;
                    sb.append(num + " ");
                    for(int j= 1; j <= N; j++){
                        if(j != num){
                            sb.append(j + " ");
                        }
                    }
                    checker = false;
                }
            }
        }

        if(checker){
            sb.append(-1);
        }
		
		System.out.println(sb);
	}
}